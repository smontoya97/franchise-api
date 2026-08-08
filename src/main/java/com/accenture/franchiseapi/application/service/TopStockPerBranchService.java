package com.accenture.franchiseapi.application.service;

import com.accenture.franchiseapi.application.view.TopStockProductView;
import com.accenture.franchiseapi.application.port.in.franchise.TopStockPerBranchUseCase;
import com.accenture.franchiseapi.application.port.out.BranchRepositoryPort;
import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.exception.FranchiseNotFoundException;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
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
    public Flux<TopStockProductView> execute(FranchiseId franchiseId) {
        return franchiseRepositoryPort.existsbyId(franchiseId)
                .flatMapMany(exists -> exists
                    ? branchRepositoryPort.findByFranchiseId(franchiseId)
                    : Flux.error(new FranchiseNotFoundException(franchiseId)))
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
