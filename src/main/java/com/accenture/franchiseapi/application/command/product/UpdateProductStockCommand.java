package com.accenture.franchiseapi.application.command.product;

import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;

public record UpdateProductStockCommand(BranchId branchId, ProductId productId, int newStock) {
}
