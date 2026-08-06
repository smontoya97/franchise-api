package com.accenture.franchiseapi.application.port.in.branch;

import com.accenture.franchiseapi.application.command.branch.AddBranchCommand;
import com.accenture.franchiseapi.domain.model.Branch;
import reactor.core.publisher.Mono;

public interface AddBranchUseCase {
    Mono<Branch> execute(AddBranchCommand command);
}
