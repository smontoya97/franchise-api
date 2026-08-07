package com.accenture.franchiseapi.infrastructure.adapter.in.web;

import com.accenture.franchiseapi.application.port.in.franchise.CreateFranchiseUseCase;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.CreateFranchiseRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.FranchiseResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.mapper.FranchiseWebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/franchises")
@AllArgsConstructor
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final FranchiseWebMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> createFranchise(@Valid @RequestBody CreateFranchiseRequest request) {
        return createFranchiseUseCase.execute(mapper.toCommand(request))
                .map(mapper::toResponse);
    }
}
