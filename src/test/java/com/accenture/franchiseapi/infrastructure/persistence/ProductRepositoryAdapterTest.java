package com.accenture.franchiseapi.infrastructure.persistence;

import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductRepositoryAdapterTest extends AbstractPersistenceTest {

    @Test
    void shouldSaveProductUnderExistingBranch() {
        String franchiseName = "Franquicia Medellín";
        String branchName = "Sucursal Poblado";
        String productName = "Coca-Cola";
        int productStock = 10;
        Franchise franchise = Franchise.create(franchiseName);
        Franchise saved = franchiseRepositoryAdapter.save(franchise).block();
        assert saved != null;
        FranchiseId franchiseId = saved.getId();
        Branch branch = Branch.create(branchName);
        Branch savedBranch = branchRepositoryAdapter.save(branch, franchiseId).block();
        assert savedBranch != null;
        BranchId branchId = savedBranch.getId();

        productRepositoryAdapter.save(Product.create(productName, productStock), branchId).block();

        StepVerifier.create(productRepositoryAdapter.findByBranchId(branchId))
                .assertNext(product -> {
                    assertEquals(productName, product.getName());
                    assertEquals(productStock, product.getStock());
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyMonoWhenProductDoesNotExist() {
        StepVerifier.create(productRepositoryAdapter.findById(ProductId.newId()))
                .verifyComplete();
    }

    @Test
    void shouldDeleteProductAndNoLongerAppearInBranch() {
        String franchiseName = "Franquicia Medellín";
        String branchName = "Sucursal Poblado";
        String productName = "Coca-Cola";
        int productStock = 10;
        Franchise franchise = franchiseRepositoryAdapter.save(Franchise.create(franchiseName)).block();
        Branch branch = branchRepositoryAdapter.save(Branch.create(branchName), franchise.getId()).block();
        Product product = productRepositoryAdapter.save(Product.create(productName, productStock), branch.getId()).block();

        StepVerifier.create(productRepositoryAdapter.existsByIdAndBranchId(product.getId(), branch.getId()))
                .expectNext(true)
                .verifyComplete();

        productRepositoryAdapter.deleteById(product.getId()).block();

        StepVerifier.create(productRepositoryAdapter.findByBranchId(branch.getId()))
                .verifyComplete();
    }
}
