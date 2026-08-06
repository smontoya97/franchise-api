package com.accenture.franchiseapi.domain.model;

import com.accenture.franchiseapi.domain.exception.ProductNotFoundException;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Branch {
    private final BranchId id;
    private String name;
    private final List<Product> products;

    private Branch(BranchId id, String name, List<Product> products) {
        this.id = id;
        this.name = validateName(name);
        this.products = new ArrayList<>(products);
    }

    public static Branch create(String name) {
        return new Branch(
                BranchId.newId(),
                name,
                new ArrayList<>()
        );
    }

    public static Branch reconstitute(BranchId id, String name, List<Product> products) {
        return new Branch(
                id,
                name,
                products
        );
    }

    public void rename(String newName) {
        this.name = validateName(newName);
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(ProductId productId) {
        boolean removed = this.products.removeIf(
                product -> product.getId().equals(productId)
        );

        if (!removed) {
            throw new ProductNotFoundException(productId);
        }
    }

    public Product findProduct(ProductId productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Optional<Product> findProductWithMostStock() {
        return products.stream()
                .max(Comparator.comparingInt(Product::getStock));
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Branch name cannot be blank");
        }
        return name;
    }

    public BranchId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return List.copyOf(products);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Branch branch)) return false;
        return Objects.equals(id, branch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
