package com.accenture.franchiseapi.infrastructure.adapter.in.web.controller;

import com.accenture.franchiseapi.application.port.in.franchise.CreateFranchiseUseCase;
import com.accenture.franchiseapi.application.port.in.franchise.RenameFranchiseUseCase;
import com.accenture.franchiseapi.application.port.in.franchise.TopStockPerBranchUseCase;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.CreateFranchiseRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.RenameRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.FranchiseResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.TopStockProductResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/franchises")
@AllArgsConstructor
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final TopStockPerBranchUseCase topStockPerBranchUseCase;
    private final RenameFranchiseUseCase renameFranchiseUseCase;
    private final WebMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> createFranchise(@Valid @RequestBody CreateFranchiseRequest request) {
        return createFranchiseUseCase.execute(mapper.toCommand(request))
                .map(mapper::toResponse);
    }

    @GetMapping("/{franchiseId}/top-stock-products")
    @ResponseStatus(HttpStatus.OK)
    public Flux<TopStockProductResponse> topStockPerBranch(@PathVariable UUID franchiseId) {
        return topStockPerBranchUseCase.execute(FranchiseId.of(franchiseId))
                .map(mapper::toResponse);
    }

    @PatchMapping("/{franchiseId}/name")
    @ResponseStatus(HttpStatus.OK)
    public Mono<FranchiseResponse> renameFranchise(
            @PathVariable UUID franchiseId,
            @Valid @RequestBody RenameRequest request
    ) {
        return renameFranchiseUseCase.execute(
                    mapper.toRenameFranchiseCommand(franchiseId, request))
                .map(mapper::toResponse);

    }
}
