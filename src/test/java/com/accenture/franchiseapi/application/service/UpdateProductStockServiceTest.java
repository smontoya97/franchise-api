package com.accenture.franchiseapi.application.service;

import com.accenture.franchiseapi.application.command.product.UpdateProductStockCommand;
import com.accenture.franchiseapi.application.port.out.ProductRepositoryPort;
import com.accenture.franchiseapi.domain.exception.ProductNotFoundException;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateProductStockServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;
    @InjectMocks
    private UpdateProductStockService updateProductStockService;

    @Test
    void shouldUpdateStockWhenProductBelongsToBranch() {
        BranchId branchId = BranchId.newId();
        String productName = "Coca-Cola";
        int productInitialStock = 50;
        int productNewStock = 65;
        Product existingProduct = Product.create(productName, productInitialStock);
        when(productRepositoryPort.existsByIdAndBranchId(existingProduct.getId(), branchId))
                .thenReturn(Mono.just(true));
        when(productRepositoryPort.findById(existingProduct.getId()))
                .thenReturn(Mono.just(existingProduct));
        when(productRepositoryPort.update(any(Product.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Product> result = updateProductStockService.execute(
                new UpdateProductStockCommand(branchId, existingProduct.getId(), productNewStock));

        StepVerifier.create(result)
                .assertNext(product -> assertEquals(productNewStock, product.getStock()))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenProductDoesNotBelongToBranch() {
        BranchId branchId = BranchId.newId();
        ProductId productId = ProductId.newId();
        int newStock = 99;
        when(productRepositoryPort.existsByIdAndBranchId(productId, branchId))
                .thenReturn(Mono.just(false));

        Mono<Product> result = updateProductStockService.execute(
                new UpdateProductStockCommand(branchId, productId, newStock));

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();
    }
}
