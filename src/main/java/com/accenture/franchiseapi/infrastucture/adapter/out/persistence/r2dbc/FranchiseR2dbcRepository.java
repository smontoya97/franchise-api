package com.accenture.franchiseapi.infrastucture.adapter.out.persistence.r2dbc;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.accenture.franchiseapi.infrastucture.adapter.out.persistence.entity.FranchiseEntity;

public interface FranchiseR2dbcRepository extends ReactiveCrudRepository<FranchiseEntity, UUID> {
}
