package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.adapter;

import org.springframework.stereotype.Repository;

import com.accenture.franchiseapi.application.port.out.ProductRepositoryPort;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper.ProductMapper;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.r2dbc.ProductR2dbcRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductR2dbcRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public Mono<Product> save(Product product, BranchId branchId) {
        ProductEntity entity = mapper.toNewEntity(product, branchId);
        return productRepository.save(entity)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findById(ProductId id) {
        return productRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findByBranchId(BranchId branchId) {
        return productRepository.findByBranchId(branchId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(ProductId id) {
        return productRepository.deleteById(id.value());
    }

    @Override
    public Mono<Boolean> existsByIdAndBranchId(ProductId productId, BranchId branchId) {
        return productRepository.existsByIdAndBranchId(productId.value(), branchId.value());
    }
}
