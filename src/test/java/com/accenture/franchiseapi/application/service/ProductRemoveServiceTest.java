package com.accenture.franchiseapi.application.service;

import com.accenture.franchiseapi.application.command.product.RemoveProductCommand;
import com.accenture.franchiseapi.application.port.out.BranchRepositoryPort;
import com.accenture.franchiseapi.application.port.out.ProductRepositoryPort;
import com.accenture.franchiseapi.domain.exception.ProductNotFoundException;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRemoveServiceTest {

    @Mock
    private BranchRepositoryPort branchRepositoryPort;
    @Mock
    private ProductRepositoryPort productRepositoryPort;
    @InjectMocks
    private RemoveProductService removeProductService;

    @Test
    void shouldRemoveProductWhenItBelongsToBranch() {
        BranchId branchId = BranchId.newId();
        ProductId productId = ProductId.newId();
        when(productRepositoryPort.existsByIdAndBranchId(productId, branchId)).thenReturn(Mono.just(true));
        when(productRepositoryPort.deleteById(productId)).thenReturn(Mono.empty());

        Mono<Void> result = removeProductService.execute(new RemoveProductCommand(branchId, productId));

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepositoryPort).existsByIdAndBranchId(productId, branchId);
        verify(productRepositoryPort).deleteById(productId);
    }

    @Test
    void shouldFailWhenProductDoesNotBelongToBranch() {
        BranchId branchId = BranchId.newId();
        ProductId productId = ProductId.newId();
        when(productRepositoryPort.existsByIdAndBranchId(productId, branchId)).thenReturn(Mono.just(false));

        Mono<Void> result = removeProductService.execute(new RemoveProductCommand(branchId, productId));

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productRepositoryPort).existsByIdAndBranchId(productId, branchId);
    }
}
