package com.accenture.franchiseapi.application.service.branch;

import com.accenture.franchiseapi.application.command.branch.AddBranchCommand;
import com.accenture.franchiseapi.application.port.in.branch.AddBranchUseCase;
import com.accenture.franchiseapi.application.port.out.BranchRepositoryPort;
import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.exception.FranchiseNotFoundException;
import com.accenture.franchiseapi.domain.model.Branch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AddBranchService implements AddBranchUseCase {

    private FranchiseRepositoryPort franchiseRepositoryPort;
    private BranchRepositoryPort branchRepositoryPort;

    @Override
    public Mono<Branch> execute(AddBranchCommand command) {
        return franchiseRepositoryPort.existsById(command.franchiseId())
                .flatMap(exists -> exists
                    ? Mono.defer(() -> Mono.just(Branch.create(command.branchName())))
                    : Mono.error(new FranchiseNotFoundException(command.franchiseId())))
                .flatMap(branch -> branchRepositoryPort.save(branch, command.franchiseId()));
    }
}
