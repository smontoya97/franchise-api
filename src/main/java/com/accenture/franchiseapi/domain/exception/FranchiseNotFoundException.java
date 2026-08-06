package com.accenture.franchiseapi.domain.exception;

import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;

public class FranchiseNotFoundException extends DomainException {
    public FranchiseNotFoundException(FranchiseId id) {
        super("Franchise not found with id: " + id.value());
    }
}
