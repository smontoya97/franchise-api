package com.accenture.franchiseapi.application.port.in.franchise;

import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import reactor.core.publisher.Mono;

public interface GetFranchiseUseCase {
    Mono<Franchise> execute(FranchiseId franchiseId);
}
