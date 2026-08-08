package com.accenture.franchiseapi.application.service;

import com.accenture.franchiseapi.application.command.franchise.CreateFranchiseCommand;
import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.exception.InvalidNameException;
import com.accenture.franchiseapi.domain.model.Franchise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFranchiseServiceTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;
    @InjectMocks
    private CreateFranchiseService createFranchiseService;

    @Test
    void shouldCreateFranchiseAndDelegateToRepository() {
        String franchiseName = "Franquicia Medellín";
        when(franchiseRepositoryPort.save(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Franchise> result = createFranchiseService.execute(
                new CreateFranchiseCommand(franchiseName)
        );

        StepVerifier.create(result)
                .assertNext(franchise -> assertEquals(franchiseName, franchise.getName()))
                .verifyComplete();

        verify(franchiseRepositoryPort).save(any(Franchise.class));
    }

    @Test
    void shouldPropagateDomainValidationErrorAsReactiveError() {
        Mono<Franchise> result = createFranchiseService.execute(
                new CreateFranchiseCommand("   ")
        );

        StepVerifier.create(result)
                .expectError(InvalidNameException.class)
                .verify();
    }
}
