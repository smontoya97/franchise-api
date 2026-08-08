package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.r2dbc;

import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface FranchiseR2dbcRepository extends ReactiveCrudRepository<FranchiseEntity, UUID> {
}
