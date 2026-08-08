package com.accenture.franchiseapi.infrastructure.web;

import com.accenture.franchiseapi.application.port.in.product.AddProductUseCase;
import com.accenture.franchiseapi.application.port.in.product.RemoveProductUseCase;
import com.accenture.franchiseapi.application.port.in.product.UpdateProductStockUseCase;
import com.accenture.franchiseapi.domain.exception.BranchNotFoundException;
import com.accenture.franchiseapi.domain.exception.ProductNotFoundException;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.controller.ProductController;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.AddProductRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.UpdateProductStockRequest;
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
    @MockitoBean
    private RemoveProductUseCase removeProductUseCase;
    @MockitoBean
    private UpdateProductStockUseCase updateProductStockUseCase;

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

    @Test
    void shouldRemoveProductAndReturn204() {
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        when(removeProductUseCase.execute(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/branches/{branchId}/products/{productId}", branchId, productId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void shouldReturn404WhenProductDoesNotBelongToBranch() {
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        when(removeProductUseCase.execute(any()))
                .thenReturn(Mono.error(new ProductNotFoundException(ProductId.of(productId))));

        webTestClient.delete()
                .uri("/branches/{branchId}/products/{productId}", branchId, productId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldUpdateStockAndReturn200() {
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        String productName = "Coca-Cola";
        int productNewStock = 30;
        Product updated = Product.create(productName, productNewStock);
        when(updateProductStockUseCase.execute(any())).thenReturn(Mono.just(updated));

        webTestClient.patch()
                .uri("/branches/{branchId}/products/{productId}/stock", branchId, productId)
                .bodyValue(new UpdateProductStockRequest(productNewStock))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.stock").isEqualTo(productNewStock);
    }

    @Test
    void shouldReturn400WhenNewStockIsNegative() {
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        int productNewStock = -3;

        webTestClient.patch()
                .uri("/branches/{branchId}/products/{productId}/stock", branchId, productId)
                .bodyValue(new UpdateProductStockRequest(productNewStock))
                .exchange()
                .expectStatus().isBadRequest();
    }
}
