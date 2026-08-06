package com.accenture.franchiseapi.domain.exception;

import com.accenture.franchiseapi.domain.model.valueobject.BranchId;

public class BranchNotFoundException extends DomainException {
    public BranchNotFoundException(BranchId id) {
        super("Branch not found with id: " + id.value());
    }
}
