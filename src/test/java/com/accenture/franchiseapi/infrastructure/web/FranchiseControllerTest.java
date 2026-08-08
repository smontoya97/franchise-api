package com.accenture.franchiseapi.infrastructure.web;

import com.accenture.franchiseapi.application.port.in.franchise.CreateFranchiseUseCase;
import com.accenture.franchiseapi.application.port.in.franchise.TopStockPerBranchUseCase;
import com.accenture.franchiseapi.application.view.TopStockProductView;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.controller.FranchiseController;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.CreateFranchiseRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.TopStockProductResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.mapper.WebMapper;
import com.accenture.franchiseapi.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(FranchiseController.class)
@Import({WebMapper.class, GlobalExceptionHandler.class})
public class FranchiseControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @MockitoBean
    private CreateFranchiseUseCase createFranchiseUseCase;
    @MockitoBean
    private TopStockPerBranchUseCase topStockPerBranchUseCase;

    @Test
    void shouldCreateFranchiseAndReturn201() {
        String franchiseName = "Franquicia Medellín";
        Franchise franchise = Franchise.create(franchiseName);
        when(createFranchiseUseCase.execute(any())).thenReturn(Mono.just(franchise));

        webTestClient.post()
                .uri("/franchises")
                .bodyValue(new CreateFranchiseRequest(franchiseName))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(franchiseName)
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.branches").isArray();

        verify(createFranchiseUseCase).execute(any());
    }

    @Test
    void shouldReturn400WhenNameIsBlank() {
        webTestClient.post()
                .uri("/franchises")
                .bodyValue(new CreateFranchiseRequest(""))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnTopStockListForFranchise() {
        String branchName = "Sucursal Poblado";
        String productName = "Coca-Cola";
        int productStock = 50;
        int expectedSize = 1;
        UUID franchiseId = UUID.randomUUID();
        TopStockProductView view = new TopStockProductView(
                UUID.randomUUID().toString(), branchName,
                UUID.randomUUID().toString(), productName, productStock);

        when(topStockPerBranchUseCase.execute(any())).thenReturn(Flux.just(view));

        webTestClient.get()
                .uri("/franchises/{franchiseId}/top-stock-products", franchiseId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TopStockProductResponse.class)
                .hasSize(expectedSize);
    }
}
