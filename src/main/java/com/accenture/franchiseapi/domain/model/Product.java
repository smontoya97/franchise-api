package com.accenture.franchiseapi.domain.model;

import com.accenture.franchiseapi.domain.exception.InvalidNameException;
import com.accenture.franchiseapi.domain.exception.InvalidStockException;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;

import java.util.Objects;

public class Product {
    private final ProductId id;
    private String name;
    private int stock;

    private Product(ProductId id, String name, int stock) {
        this.id = id;
        this.name = validateName(name);
        this.stock = validateStock(stock);
    }

    public static Product create(String name, int stock) {
        return new Product(
                ProductId.newId(),
                name,
                stock
        );
    }

    public static Product reconstitute(ProductId id, String name, int stock) {
        return new Product(
                id,
                name,
                stock
        );
    }

    public void rename(String newName) {
        this.name = validateName(newName);
    }

    public void updateStock(int newStock) {
        this.stock = validateStock(newStock);
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNameException("Product name cannot be blank");
        }

        return name;
    }

    private int validateStock(int stock) {
        if (stock < 0) {
            throw new InvalidStockException("Stock cannot be negative");
        }
        return stock;
    }

    public ProductId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
