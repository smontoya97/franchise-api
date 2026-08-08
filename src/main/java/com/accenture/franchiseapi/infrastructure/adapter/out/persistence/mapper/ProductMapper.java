package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper;

import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity toNewEntity(Product product, BranchId branchId) {
        return ProductEntity.createNew(
                product.getId().value(),
                branchId.value(),
                product.getName(),
                product.getStock());
    }

    public Product toDomain(ProductEntity entity) {
        return Product.reconstitute(
                ProductId.of(entity.getId()),
                entity.getName(),
                entity.getStock());
    }
}
