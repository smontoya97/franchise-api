package com.accenture.franchiseapi.infrastructure.exception;

import com.accenture.franchiseapi.application.port.in.franchise.CreateFranchiseUseCase;
import com.accenture.franchiseapi.application.port.in.franchise.GetAllFranchisesUseCase;
import com.accenture.franchiseapi.application.port.in.franchise.GetFranchiseUseCase;
import com.accenture.franchiseapi.application.port.in.franchise.RenameFranchiseUseCase;
import com.accenture.franchiseapi.application.port.in.franchise.TopStockPerBranchUseCase;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.controller.FranchiseController;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.mapper.WebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(FranchiseController.class)
@Import({WebMapper.class, GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private CreateFranchiseUseCase createFranchiseUseCase;
    @MockitoBean
    private GetFranchiseUseCase getFranchiseUseCase;
    @MockitoBean
    private TopStockPerBranchUseCase topStockPerBranchUseCase;
    @MockitoBean
    private RenameFranchiseUseCase renameFranchiseUseCase;
    @MockitoBean
    private GetAllFranchisesUseCase getAllFranchisesUseCase;

    @Test
    void shouldReturnDetailedValidationErrorsWhenNameIsBlank() {
        webTestClient.post()
                .uri("/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("ValidationError")
                .jsonPath("$.details[0]").exists();
    }

    @Test
    void shouldReturn400ForMalformedJson() {
        String malformedJson = "{ this is not valid json}";

        webTestClient.post()
                .uri("/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(malformedJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("InvalidRequest");
    }

    @Test
    void shouldReturn400ForInvalidPathVariableType() {
        webTestClient.get()
                .uri("/franchises/not-a-valid-uuid/top-stock-products")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("InvalidRequest");
    }
}
