package com.accenture.franchiseapi.application.port.in.franchise;

import com.accenture.franchiseapi.application.command.franchise.CreateFranchiseCommand;
import com.accenture.franchiseapi.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface CreateFranchiseUseCase {
    Mono<Franchise> execute(CreateFranchiseCommand command);
}
