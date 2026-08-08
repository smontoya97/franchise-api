package com.accenture.franchiseapi.application.service.product;

import com.accenture.franchiseapi.application.command.product.AddProductCommand;
import com.accenture.franchiseapi.application.port.out.BranchRepositoryPort;
import com.accenture.franchiseapi.application.port.out.ProductRepositoryPort;
import com.accenture.franchiseapi.domain.exception.BranchNotFoundException;
import com.accenture.franchiseapi.domain.exception.InvalidStockException;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddProductServiceTest {

    @Mock
    private BranchRepositoryPort branchRepositoryPort;
    @Mock
    private ProductRepositoryPort productRepositoryPort;
    @InjectMocks
    private AddProductService addProductService;

    @Test
    void shoulAddProductWhenBranchExists() {
        BranchId branchId = BranchId.newId();
        String productName = "Coca-Cola";
        int productStock = 20;
        AddProductCommand command = new AddProductCommand(branchId, productName, productStock);
        when(branchRepositoryPort.existsbyId(branchId)).thenReturn(Mono.just(true));
        when(productRepositoryPort.save(any(Product.class), eq(branchId)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Product> result = addProductService.execute(command);

        StepVerifier.create(result)
                .assertNext(product -> {
                    assertEquals(productName, product.getName());
                    assertEquals(productStock, product.getStock());
                })
                .verifyComplete();

        verify(branchRepositoryPort).existsbyId(branchId);
        verify(productRepositoryPort).save(any(Product.class), eq(branchId));
    }

    @Test
    void shouldFailWhenBranchDoesNotExist() {
        BranchId branchId = BranchId.newId();
        String productName = "Coca-Cola";
        int productStock = 20;
        AddProductCommand command = new AddProductCommand(branchId, productName, productStock);
        when(branchRepositoryPort.existsbyId(branchId)).thenReturn(Mono.just(false));

        Mono<Product> result = addProductService.execute(command);

        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchRepositoryPort).existsbyId(branchId);
    }

    @Test
    void shouldFailWhenInitialStockIsNegative() {
        BranchId branchId = BranchId.newId();
        String productName = "Coca-Cola";
        int productStock = -5;
        AddProductCommand command = new AddProductCommand(branchId, productName, productStock);
        when(branchRepositoryPort.existsbyId(branchId)).thenReturn(Mono.just(true));

        Mono<Product> result = addProductService.execute(command);

        StepVerifier.create(result)
                .expectError(InvalidStockException.class)
                .verify();

        verify(branchRepositoryPort).existsbyId(branchId);
    }
}
