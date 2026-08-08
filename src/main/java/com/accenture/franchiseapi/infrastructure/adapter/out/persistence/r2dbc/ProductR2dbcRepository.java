package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.r2dbc;

import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, UUID> {
    Flux<ProductEntity> findByBranchId(UUID branchId);

    Mono<Boolean> existsByIdAndBranchId(UUID id, UUID branchId);
}
