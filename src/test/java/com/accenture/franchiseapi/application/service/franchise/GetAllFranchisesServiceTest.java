package com.accenture.franchiseapi.application.service.franchise;

import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.model.Franchise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllFranchisesServiceTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;
    @InjectMocks
    private GetAllFranchisesService getAllFranchisesService;

    @Test
    void shouldListAllFranchises() {
        String franchiseMedellinName = "Franquicia Medellín";
        Franchise franchiseMedellin = Franchise.create(franchiseMedellinName);
        String franchiseBogotaName = "Franquicia Bogotá";
        Franchise franchiseBogota= Franchise.create(franchiseBogotaName);
        int expectedSize = 2;
        when(franchiseRepositoryPort.getAll())
                .thenReturn(Flux.just(franchiseMedellin, franchiseBogota));

        Flux<Franchise> result = getAllFranchisesService.execute();

        StepVerifier.create(result)
                .expectNextCount(expectedSize)
                .verifyComplete();
    }
}
