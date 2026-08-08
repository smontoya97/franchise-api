package com.accenture.franchiseapi.infrastructure.persistence;

import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FranchiseRepositoryAdapterTest extends AbstractPersistenceTest {

    @Test
    void shouldSaveAndRetrieveFranchiseWithBranchesAndProducts() {
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
        Product product = Product.create(productName, productStock);
        assert savedBranch != null;
        productRepositoryAdapter.save(product, savedBranch.getId()).block();
        int expectedSize = 1;

        StepVerifier.create(franchiseRepositoryAdapter.findById(franchiseId))
                .assertNext(result -> {
                    assertEquals(franchiseName, result.getName());
                    assertEquals(expectedSize, result.getBranches().size());
                    Branch resultBranch = result.getBranches().getFirst();
                    assertEquals(branchName, resultBranch.getName());
                    assertEquals(expectedSize, resultBranch.getProducts().size());
                    assertEquals(productName, resultBranch.getProducts().getFirst().getName());
                    assertEquals(productStock, resultBranch.getProducts().getFirst().getStock());
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyMonoWhenFranchiseDoesNotExist() {
        StepVerifier.create(franchiseRepositoryAdapter.findById(FranchiseId.newId()))
                .verifyComplete();
    }

    @Test
    void shouldRenameBranchWithoutCreatingDuplicateRow() {
        String franchiseName = "Franquicia Medellín";
        String branchName = "Sucursal Poblado";
        String newName = "Sucursal Poblado Norte";
        Franchise franchise = franchiseRepositoryAdapter.save(Franchise.create(franchiseName)).block();
        Branch branch = branchRepositoryAdapter.save(Branch.create(branchName), franchise.getId()).block();

        branch.rename(newName);
        branchRepositoryAdapter.update(branch).block();

        StepVerifier.create(branchRepositoryAdapter.findByFranchiseId(franchise.getId()))
                .assertNext(result -> assertEquals(newName, result.getName()))
                .verifyComplete();
    }
}
