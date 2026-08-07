package com.accenture.franchiseapi.application.port.out;

import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepositoryPort {
    Mono<Branch> save(Branch branch, FranchiseId franchiseId);
    Mono<Branch> findById(BranchId id);
    Flux<Branch> findByFranchiseId(FranchiseId franchiseId);
    Mono<Boolean> existsbyId(BranchId id);
}
