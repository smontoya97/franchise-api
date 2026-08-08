package com.accenture.franchiseapi.application.service.franchise;

import com.accenture.franchiseapi.application.port.out.BranchRepositoryPort;
import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.application.view.TopStockProductView;
import com.accenture.franchiseapi.domain.exception.FranchiseNotFoundException;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopStockPerBranchServiceTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;
    @Mock
    private BranchRepositoryPort branchRepositoryPort;
    @InjectMocks
    private TopStockPerBranchService topStockPerBranchService;

    @Test
    void shouldReturnTopStockProductForEachBranchWithProducts() {
        FranchiseId franchiseId = FranchiseId.newId();
        String branchPoblado = "Suursal Poblado";
        String branchEnvigado = "Sucursal Envigado";
        String waterProduct = "Agua";
        int waterStock = 5;
        String cocaColaProduct = "Coca-Cola";
        int cocaColaStock = 10;
        Branch branchWithProducts = Branch.create(branchPoblado);
        branchWithProducts.addProduct(Product.create(waterProduct, waterStock));
        branchWithProducts.addProduct(Product.create(cocaColaProduct, cocaColaStock));
        Branch branchWithoutProducts = Branch.create(branchEnvigado);
        when(franchiseRepositoryPort.existsById(franchiseId)).thenReturn(Mono.just(true));
        when(branchRepositoryPort.findByFranchiseId(franchiseId))
                .thenReturn(Flux.just(branchWithProducts, branchWithoutProducts));

        Flux<TopStockProductView> result = topStockPerBranchService.execute(franchiseId);

        StepVerifier.create(result)
                .assertNext(view -> {
                    assertEquals(branchPoblado, view.branchName());
                    assertEquals(cocaColaProduct, view.productName());
                    assertEquals(cocaColaStock, view.stock());
                })
                .verifyComplete();

        verify(franchiseRepositoryPort).existsById(franchiseId);
        verify(branchRepositoryPort).findByFranchiseId(franchiseId);
    }

    @Test
    void shouldFailWhenFranchiseDoesNotExist() {
        FranchiseId franchiseId = FranchiseId.newId();
        when(franchiseRepositoryPort.existsById(franchiseId)).thenReturn(Mono.just(false));

        Flux<TopStockProductView> result = topStockPerBranchService.execute(franchiseId);

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }
}
