package com.accenture.franchiseapi.application.port.in.franchise;

import com.accenture.franchiseapi.domain.model.Franchise;
import reactor.core.publisher.Flux;

public interface GetAllFranchisesUseCase {
    Flux<Franchise> execute();
}
