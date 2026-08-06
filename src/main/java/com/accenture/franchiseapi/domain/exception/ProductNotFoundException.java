package com.accenture.franchiseapi.domain.exception;

import com.accenture.franchiseapi.domain.model.valueobject.ProductId;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(ProductId id) {
        super("Product not found with id: " + id.value());
    }
}
