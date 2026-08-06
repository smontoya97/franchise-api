package com.accenture.franchiseapi.application.port.in.franchise;

import com.accenture.franchiseapi.application.command.franchise.RenameFranchiseCommand;
import com.accenture.franchiseapi.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface RenameFranchiseUseCase {
    Mono<Franchise> execute(RenameFranchiseCommand command);
}
