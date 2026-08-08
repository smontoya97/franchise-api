package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.r2dbc;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity.BranchEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchR2dbcRepository extends ReactiveCrudRepository<BranchEntity, UUID> {
    Flux<BranchEntity> findByFranchiseId(UUID franchiseId);
    Mono<Boolean> existsByIdAndFranchiseId(UUID id, UUID franchiseId);
}
