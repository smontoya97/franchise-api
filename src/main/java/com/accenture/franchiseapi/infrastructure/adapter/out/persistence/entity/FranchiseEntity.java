package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("franchises")
public class FranchiseEntity implements Persistable<UUID> {
    @Id
    private final UUID id;
    private String name;
    @Transient
    private final boolean isNewEntity;

    @PersistenceCreator
    public FranchiseEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.isNewEntity = false;
    }

    private FranchiseEntity(UUID id, String name, boolean isNewEntity) {
        this.id = id;
        this.name = name;
        this.isNewEntity = isNewEntity;
    }

    public static FranchiseEntity createNew(UUID id, String name) {
        return new FranchiseEntity(id, name, true);
    }

    public static FranchiseEntity createExisting(UUID id, String name) {
        return new FranchiseEntity(id, name, false);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    public String getName() {
        return name;
    }
}
