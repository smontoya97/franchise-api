package com.accenture.franchiseapi.infrastructure.adapter.in.web.controller;

import com.accenture.franchiseapi.application.port.in.branch.AddBranchUseCase;
import com.accenture.franchiseapi.application.port.in.branch.RenameBranchUseCase;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.AddBranchRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.RenameRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.BranchResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/franchises/{franchiseId}/branches")
@AllArgsConstructor
public class BranchController {

    private final AddBranchUseCase addBranchUseCase;
    private final RenameBranchUseCase renameBranchUseCase;
    private final WebMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BranchResponse> addBranch(
            @PathVariable UUID franchiseId,
            @Valid @RequestBody AddBranchRequest request
    ) {
        return addBranchUseCase.execute(mapper.toCommand(franchiseId, request))
                .map(mapper::toResponse);
    }

    @PatchMapping("/{branchId}/name")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BranchResponse> renameBranch(
            @PathVariable UUID franchiseId,
            @PathVariable UUID branchId,
            @Valid @RequestBody RenameRequest request
    ) {
        return renameBranchUseCase.execute(
                        mapper.toRenameBranchCommand(franchiseId, branchId, request))
                .map(mapper::toResponse);
    }
}
