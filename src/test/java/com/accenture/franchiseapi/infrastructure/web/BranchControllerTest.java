package com.accenture.franchiseapi.infrastructure.web;

import com.accenture.franchiseapi.application.port.in.branch.AddBranchUseCase;
import com.accenture.franchiseapi.domain.exception.FranchiseNotFoundException;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.controller.BranchController;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.AddBranchRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.mapper.WebMapper;
import com.accenture.franchiseapi.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(BranchController.class)
@Import({WebMapper.class, GlobalExceptionHandler.class})
class BranchControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private AddBranchUseCase addBranchUseCase;

    @Test
    void shouldAddBranchAndReturn201() {
        UUID franchiseId = UUID.randomUUID();
        String branchName = "Sucursal Poblado";
        Branch branch = Branch.create(branchName);
        when(addBranchUseCase.execute(any())).thenReturn(Mono.just(branch));

        webTestClient.post()
                .uri("/franchises/{franchiseId}/branches", franchiseId)
                .bodyValue(new AddBranchRequest(branchName))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(branchName);
    }

    @Test
    void shouldReturn404WhenFranchiseDoesNotExist() {
        UUID franchiseId = UUID.randomUUID();
        String branchName = "Sucursal Poblado";
        when(addBranchUseCase.execute(any()))
                .thenReturn(Mono.error(new FranchiseNotFoundException(FranchiseId.of(franchiseId))));

        webTestClient.post()
                .uri("/franchises/{franchiseId}/branches", franchiseId)
                .bodyValue(new AddBranchRequest(branchName))
                .exchange()
                .expectStatus().isNotFound();
    }
}
