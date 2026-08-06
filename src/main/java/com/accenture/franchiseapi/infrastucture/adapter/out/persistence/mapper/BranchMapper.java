package com.accenture.franchiseapi.infrastucture.adapter.out.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import com.accenture.franchiseapi.infrastucture.adapter.out.persistence.entity.BranchEntity;

@Component
public class BranchMapper {

    public BranchEntity toNewEntity(Branch branch, FranchiseId franchiseId) {
        return BranchEntity.createNew(
                branch.getId().value(),
                franchiseId.value(),
                branch.getName());
    }

    public Branch toDomain(BranchEntity entity, List<Product> products) {
        return Branch.reconstitute(
                BranchId.of(entity.getFranchiseId()),
                entity.getName(),
                products);
    }
}
