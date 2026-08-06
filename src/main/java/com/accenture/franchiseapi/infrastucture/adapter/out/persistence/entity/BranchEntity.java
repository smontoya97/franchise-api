package com.accenture.franchiseapi.infrastucture.adapter.out.persistence.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("branches")
public class BranchEntity implements Persistable<UUID> {
    @Id
    private final UUID id;
    @Column("franchise_id")
    private final UUID franchiseId;
    private String name;
    @Transient
    private final boolean isNewEntity;

    @PersistenceCreator
    public BranchEntity(UUID id, UUID franchiseId, String name) {
        this.id = id;
        this.franchiseId = franchiseId;
        this.name = name;
        this.isNewEntity = false;
    }

    private BranchEntity(UUID id, UUID franchiseId, String name, boolean isNewEntity) {
        this.id = id;
        this.franchiseId = franchiseId;
        this.name = name;
        this.isNewEntity = isNewEntity;
    }

    public BranchEntity createNew(UUID id, UUID franchiseId, String name) {
        return new BranchEntity(id, franchiseId, name, true);
    }

    public BranchEntity createExisting(UUID id, UUID franchiseId, String name) {
        return new BranchEntity(id, franchiseId, name, false);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    public UUID getFranchiseId() {
        return franchiseId;
    }

    public String getName() {
        return name;
    }
}
