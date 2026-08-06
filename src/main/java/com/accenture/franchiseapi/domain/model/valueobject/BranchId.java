package com.accenture.franchiseapi.domain.model.valueobject;

import java.util.UUID;

public record BranchId(UUID value) {
    public static BranchId newId() {
        return new BranchId(UUID.randomUUID());
    }

    public static BranchId of(UUID value) {
        return new BranchId(value);
    }
}
