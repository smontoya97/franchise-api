package com.accenture.franchiseapi.application.service.franchise;

import com.accenture.franchiseapi.application.command.franchise.CreateFranchiseCommand;
import com.accenture.franchiseapi.application.port.in.franchise.CreateFranchiseUseCase;
import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.model.Franchise;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CreateFranchiseService implements CreateFranchiseUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    @Override
    public Mono<Franchise> execute(CreateFranchiseCommand command) {
        return Mono.defer(() -> Mono.just(Franchise.create(command.name())))
                .flatMap(franchiseRepositoryPort::save);
    }
}
