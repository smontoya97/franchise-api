package com.accenture.franchiseapi.domain;

import com.accenture.franchiseapi.domain.exception.BranchNotFoundException;
import com.accenture.franchiseapi.domain.exception.InvalidNameException;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FranchiseTest {
    private final String franchiseName = "Franquicia Medellín";

    @Test
    void shouldCreateFranchiseWithValidName() {
        Franchise franchise = Franchise.create(franchiseName);

        assertEquals(franchiseName, franchise.getName());
    }

    @Test
    void shouldRejectBlankName() {
        String emptyFranchiseName = "";

        assertThrows(InvalidNameException.class, () -> Branch.create(emptyFranchiseName));
    }

    @Test
    void shouldAddBranchToFranchise() {
        Franchise franchise = Franchise.create(franchiseName);
        Branch branch = getBranch();
        int expectedSize = 1;

        franchise.addBranch(branch);

        assertEquals(expectedSize, franchise.getBranches().size());
        assertEquals(branch, franchise.getBranches().getFirst());
    }

    @Test
    void shouldFindBranchById() {
        Franchise franchise = Franchise.create(franchiseName);
        Branch branch = getBranch();
        franchise.addBranch(branch);

        Branch found = franchise.findBranch(branch.getId());

        assertEquals(branch, found);
    }

    @Test
    void shouldThrowWhenBranchNotFound() {
        Franchise franchise = Franchise.create(franchiseName);
        BranchId branchId = BranchId.newId();

        assertThrows(BranchNotFoundException.class, () -> franchise.findBranch(branchId));
    }

    @Test
    void shouldRenameFranchise() {
        String newName = "Franquicia Medellín Sur";
        Franchise franchise = Franchise.create(franchiseName);

        franchise.rename(newName);

        assertEquals(newName, franchise.getName());
    }

    private Branch getBranch() {
        String branchName = "Sucursal Poblado";
        return Branch.create(branchName);
    }

}
