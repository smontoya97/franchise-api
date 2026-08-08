package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper;

import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FranchiseMapper {

    public FranchiseEntity toNewEntity(Franchise franchise) {
        return FranchiseEntity.createNew(
                franchise.getId().value(), franchise.getName());
    }

    public Franchise toDomain(FranchiseEntity entity, List<Branch> branches) {
        return Franchise.reconstitute(
                FranchiseId.of(entity.getId()),
                entity.getName(),
                branches);
    }
}
