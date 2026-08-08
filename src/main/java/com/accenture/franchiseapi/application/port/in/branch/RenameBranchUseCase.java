package com.accenture.franchiseapi.application.port.in.branch;

import com.accenture.franchiseapi.application.command.branch.RenameBranchCommand;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import reactor.core.publisher.Mono;

public interface RenameBranchUseCase {
    Mono<Branch> execute(RenameBranchCommand command);
}
