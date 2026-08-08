package com.accenture.franchiseapi.application.service;

import com.accenture.franchiseapi.application.command.franchise.RenameFranchiseCommand;
import com.accenture.franchiseapi.application.port.in.franchise.RenameFranchiseUseCase;
import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.exception.FranchiseNotFoundException;
import com.accenture.franchiseapi.domain.model.Franchise;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class RenameFranchiseService implements RenameFranchiseUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    @Override
    public Mono<Franchise> execute(RenameFranchiseCommand command) {
        return franchiseRepositoryPort.findById(command.franchiseId())
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(command.franchiseId())))
                .map(franchise -> {
                    franchise.rename(command.newName());
                    return franchise;
                })
                .flatMap(franchiseRepositoryPort::update);
    }
}
