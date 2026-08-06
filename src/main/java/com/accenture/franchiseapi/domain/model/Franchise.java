package com.accenture.franchiseapi.domain.model;

import com.accenture.franchiseapi.domain.exception.BranchNotFoundException;
import com.accenture.franchiseapi.domain.exception.InvalidNameException;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Franchise {
    private final FranchiseId id;
    private String name;
    private final List<Branch> branches;


    private Franchise(FranchiseId id, String name, List<Branch> branches) {
        this.id = id;
        this.name = validateName(name);
        this.branches = new ArrayList<>(branches);
    }

    public static Franchise create(String name) {
        return new Franchise(
                FranchiseId.newId(),
                name,
                new ArrayList<>()
        );
    }

    public static Franchise reconstitute(FranchiseId id, String name, List<Branch> branches) {
        return new Franchise(
                id,
                name,
                branches
        );
    }

    public void rename(String newName) {
        this.name = validateName(newName);
    }

    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }

    public Branch findBranch(BranchId branchId) {
        return branches.stream()
                .filter(branch -> branch.getId().equals(branchId))
                .findFirst()
                .orElseThrow(() -> new BranchNotFoundException(branchId));
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNameException("Franchise name cannot be null or blank");
        }

        return name;
    }

    public FranchiseId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Branch> getBranches() {
        return List.copyOf(branches);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Franchise franchise)) return false;
        return Objects.equals(id, franchise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
