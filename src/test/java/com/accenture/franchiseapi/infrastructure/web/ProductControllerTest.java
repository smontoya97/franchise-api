package com.accenture.franchiseapi.infrastructure.web;

import com.accenture.franchiseapi.application.port.in.product.AddProductUseCase;
import com.accenture.franchiseapi.domain.exception.BranchNotFoundException;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.controller.ProductController;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.AddProductRequest;
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

@WebFluxTest(ProductController.class)
@Import({WebMapper.class, GlobalExceptionHandler.class})
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private AddProductUseCase addProductUseCase;

    @Test
    void shouldAddProductAndReturn201() {
        UUID branchId = UUID.randomUUID();
        String productName = "Coca-Cola";
        int productStock = 20;
        when(addProductUseCase.execute(any())).thenReturn(Mono.just(Product.create(productName, productStock)));

        webTestClient.post()
                .uri("/branches/{branchId}/products", branchId)
                .bodyValue(new AddProductRequest(productName, productStock))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(productName)
                .jsonPath("$.stock").isEqualTo(productStock);
    }

    @Test
    void shouldReturn400WhenInitialStockIsNegativeAtRequestLevel() {
        UUID branchId = UUID.randomUUID();
        String productName = "Coca-Cola";
        int productStock = -5;
        webTestClient.post()
                .uri("/branches/{branchId}/products", branchId)
                .bodyValue(new AddProductRequest(productName, productStock))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturn404WhenBranchDoesNotExist() {
        UUID branchId = UUID.randomUUID();
        String productName = "Coca-Cola";
        int productStock = 20;
        when(addProductUseCase.execute(any()))
                .thenReturn(Mono.error(new BranchNotFoundException(BranchId.of(branchId))));

        webTestClient.post()
                .uri("/branches/{branchId}/products", branchId)
                .bodyValue(new AddProductRequest(productName, productStock))
                .exchange()
                .expectStatus().isNotFound();
    }
}
