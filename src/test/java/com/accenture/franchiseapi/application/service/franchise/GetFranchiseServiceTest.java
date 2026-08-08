package com.accenture.franchiseapi.application.service.franchise;

import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.exception.FranchiseNotFoundException;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFranchiseServiceTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;
    @InjectMocks
    private GetFranchiseService getFranchiseService;

    @Test
    void shouldReturnFranchiseWhenExists() {
        String franchiseName = "Franquicia Medellín";
        Franchise existing = Franchise.create(franchiseName);
        when(franchiseRepositoryPort.findById(existing.getId())).thenReturn(Mono.just(existing));

        Mono<Franchise> result = getFranchiseService.execute(existing.getId());
        StepVerifier.create(result)
                .assertNext(franchise -> assertEquals(franchiseName, franchise.getName()))
                .verifyComplete();

        verify(franchiseRepositoryPort).findById(existing.getId());
    }

    @Test
    void shouldFailWhenFranchiseDoesNotExist() {
        FranchiseId franchiseId = FranchiseId.newId();
        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.empty());

        Mono<Franchise> result = getFranchiseService.execute(franchiseId);
        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepositoryPort).findById(franchiseId);
    }
}
