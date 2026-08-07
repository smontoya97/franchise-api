package com.accenture.franchiseapi.infrastucture.adapter.out.persistence.r2dbc;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.accenture.franchiseapi.infrastucture.adapter.out.persistence.entity.BranchEntity;

import reactor.core.publisher.Flux;

public interface BranchR2dbcRepository extends ReactiveCrudRepository<BranchEntity, UUID> {
    Flux<BranchEntity> findByFranchiseId(UUID franchiseId);
}
