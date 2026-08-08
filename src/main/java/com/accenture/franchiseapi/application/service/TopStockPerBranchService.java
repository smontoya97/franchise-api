package com.accenture.franchiseapi.application.service;

import com.accenture.franchiseapi.application.command.branch.TopStockPerBranch;
import com.accenture.franchiseapi.application.dto.TopStockProductView;
import com.accenture.franchiseapi.application.port.in.branch.TopStockPerBranchUseCase;
import com.accenture.franchiseapi.application.port.out.BranchRepositoryPort;
import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.exception.FranchiseNotFoundException;
import com.accenture.franchiseapi.domain.model.Branch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TopStockPerBranchService implements TopStockPerBranchUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;
    private final BranchRepositoryPort branchRepositoryPort;

    @Override
    public Flux<TopStockProductView> execute(TopStockPerBranch command) {
        return franchiseRepositoryPort.existsbyId(command.franchiseId())
                .flatMapMany(exists -> exists
                    ? branchRepositoryPort.findByFranchiseId(command.franchiseId())
                    : Flux.error(new FranchiseNotFoundException(command.franchiseId())))
                .flatMap(this::toTopStockProductView);
    }

    private Mono<TopStockProductView> toTopStockProductView(Branch branch) {
        return Mono.justOrEmpty(branch.findProductWithMostStock())
                .map(product -> new TopStockProductView(
                        branch.getId().value().toString(),
                        branch.getName(),
                        product.getId().value().toString(),
                        product.getName(),
                        product.getStock()
                ));
    }
}
