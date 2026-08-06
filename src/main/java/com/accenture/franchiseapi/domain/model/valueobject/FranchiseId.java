package com.accenture.franchiseapi.domain.model.valueobject;

import java.util.UUID;

public record FranchiseId(UUID value) {
    public static FranchiseId newId() {
        return new FranchiseId(UUID.randomUUID());
    }
}
