package com.accenture.franchiseapi.application.service.franchise;

import com.accenture.franchiseapi.application.port.in.franchise.GetAllFranchisesUseCase;
import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.model.Franchise;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class GetAllFranchisesService implements GetAllFranchisesUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    @Override
    public Flux<Franchise> execute() {
        return franchiseRepositoryPort.getAll();
    }
}
