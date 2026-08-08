package com.accenture.franchiseapi.application.service;

import com.accenture.franchiseapi.application.command.franchise.RenameFranchiseCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RenameFranchiseServiceTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;
    @InjectMocks
    private RenameFranchiseService renameFranchiseService;

    @Test
    void shouldRenameFranchiseWhenItExists() {
        String franchiseName = "Franquicia Medellín";
        Franchise existing = Franchise.create(franchiseName);
        String newName = "Franquicia Medellín - Sur";
        when(franchiseRepositoryPort.findById(existing.getId())).thenReturn(Mono.just(existing));
        when(franchiseRepositoryPort.update(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Franchise> result = renameFranchiseService.execute(new RenameFranchiseCommand(existing.getId(), newName));

        StepVerifier.create(result)
                .assertNext(franchise -> assertEquals(newName, franchise.getName()))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenFranchiseDoesNotExist() {
        FranchiseId franchiseId = FranchiseId.newId();
        String newName = "Franquicia Medellín - Sur";
        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(renameFranchiseService.execute(new RenameFranchiseCommand(franchiseId, newName)))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }
}
