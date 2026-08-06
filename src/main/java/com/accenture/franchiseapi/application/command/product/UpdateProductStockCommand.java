package com.accenture.franchiseapi.application.command.product;

import com.accenture.franchiseapi.domain.model.valueobject.ProductId;

public record UpdateProductStockCommand(ProductId productId, int newStock) {
}
