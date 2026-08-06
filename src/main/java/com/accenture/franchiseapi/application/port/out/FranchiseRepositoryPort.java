package com.accenture.franchiseapi.application.port.out;

import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import reactor.core.publisher.Mono;

public interface FranchiseRepositoryPort {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(FranchiseId id);
}
