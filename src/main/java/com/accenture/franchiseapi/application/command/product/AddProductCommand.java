package com.accenture.franchiseapi.application.command.product;

import com.accenture.franchiseapi.domain.model.valueobject.BranchId;

public record AddProductCommand(
        BranchId branchId,
        String productName,
        int initialStock
) {
}
