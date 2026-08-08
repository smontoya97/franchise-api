package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.r2dbc;

import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BranchR2dbcRepository extends ReactiveCrudRepository<BranchEntity, UUID> {
    Flux<BranchEntity> findByFranchiseId(UUID franchiseId);

    Mono<Boolean> existsByIdAndFranchiseId(UUID id, UUID franchiseId);
}
