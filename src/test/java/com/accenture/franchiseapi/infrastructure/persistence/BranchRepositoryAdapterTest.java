package com.accenture.franchiseapi.infrastructure.persistence;

import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BranchRepositoryAdapterTest extends AbstractPersistenceTest {

    @Test
    void shouldSaveBranchUnderExistingFranchise() {
        String franchiseName = "Franquicia Medellín";
        String branchName = "Sucursal Poblado";
        Franchise franchise = Franchise.create(franchiseName);
        Franchise saved = franchiseRepositoryAdapter.save(franchise).block();
        assert saved != null;
        FranchiseId franchiseId = saved.getId();
        Branch branch = Branch.create(branchName);
        Branch savedBranch = branchRepositoryAdapter.save(branch, franchiseId).block();
        assert savedBranch != null;
        BranchId branchId = savedBranch.getId();

        StepVerifier.create(branchRepositoryAdapter.findById(branchId))
                .assertNext(result -> assertEquals(branchName, result.getName()))
                .verifyComplete();
    }
}
