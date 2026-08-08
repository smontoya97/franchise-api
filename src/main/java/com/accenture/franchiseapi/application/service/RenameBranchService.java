package com.accenture.franchiseapi.application.service;

import com.accenture.franchiseapi.application.command.branch.RenameBranchCommand;
import com.accenture.franchiseapi.application.port.in.branch.RenameBranchUseCase;
import com.accenture.franchiseapi.application.port.out.BranchRepositoryPort;
import com.accenture.franchiseapi.domain.exception.BranchNotFoundException;
import com.accenture.franchiseapi.domain.model.Branch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class RenameBranchService implements RenameBranchUseCase {

    private final BranchRepositoryPort branchRepositoryPort;

    @Override
    public Mono<Branch> execute(RenameBranchCommand command) {
        return branchRepositoryPort.existsByIdAndFranchiseId(command.branchId(), command.franchiseId())
                .flatMap(exists -> exists
                        ? branchRepositoryPort.findById(command.branchId())
                        : Mono.error(new BranchNotFoundException(command.branchId())))
                .map(branch -> {
                    branch.rename(command.newName());
                    return branch;
                })
                .flatMap(branchRepositoryPort::update);
    }
}
