package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("products")
public class ProductEntity implements Persistable<UUID> {
    @Id
    private final UUID id;
    @Column("branch_id")
    private final UUID branchId;
    private String name;
    private int stock;
    @Transient
    private final boolean isNewEntity;

    @PersistenceCreator
    public ProductEntity(UUID id, UUID branchId, String name, int stock) {
        this.id = id;
        this.branchId = branchId;
        this.name = name;
        this.stock = stock;
        this.isNewEntity = false;
    }

    private ProductEntity(UUID id, UUID branchId, String name, int stock, boolean isNewEntity) {
        this.id = id;
        this.branchId = branchId;
        this.name = name;
        this.stock = stock;
        this.isNewEntity = isNewEntity;
    }

    public static ProductEntity createNew(UUID id, UUID branchId, String name, int stock) {
        return new ProductEntity(id, branchId, name, stock, true);
    }

    public static ProductEntity createExisting(UUID id, UUID branchId, String name, int stock) {
        return new ProductEntity(id, branchId, name, stock, false);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }
}
