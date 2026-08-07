package com.accenture.franchiseapi.infrastucture.adapter.out.persistence.r2dbc;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.accenture.franchiseapi.infrastucture.adapter.out.persistence.entity.ProductEntity;

import reactor.core.publisher.Flux;

public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, UUID> {
    Flux<ProductEntity> findByBranchId(UUID branchId);
}
