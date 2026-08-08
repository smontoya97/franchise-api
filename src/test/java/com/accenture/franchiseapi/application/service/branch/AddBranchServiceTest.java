package com.accenture.franchiseapi.application.service.branch;

import com.accenture.franchiseapi.application.command.branch.AddBranchCommand;
import com.accenture.franchiseapi.application.port.out.BranchRepositoryPort;
import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.exception.FranchiseNotFoundException;
import com.accenture.franchiseapi.domain.model.Branch;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddBranchServiceTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;
    @Mock
    private BranchRepositoryPort branchRepositoryPort;
    @InjectMocks
    private AddBranchService addBranchService;

    @Test
    void shouldAddBranchWhenFranchiseExists() {
        FranchiseId franchiseId = FranchiseId.newId();
        String branchName = "Sucursal Poblado";
        AddBranchCommand command = new AddBranchCommand(franchiseId, branchName);
        when(franchiseRepositoryPort.existsById(franchiseId)).thenReturn(Mono.just(true));
        when(branchRepositoryPort.save(any(Branch.class), eq(franchiseId)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Branch> result = addBranchService.execute(command);

        StepVerifier.create(result)
                .assertNext(branch -> assertEquals(branchName, branch.getName()))
                .verifyComplete();

        verify(franchiseRepositoryPort).existsById(franchiseId);
        verify(branchRepositoryPort).save(any(Branch.class), eq(franchiseId));
    }

    @Test
    void shouldFailWhenFranchiseDoesNotExist() {
        FranchiseId franchiseId = FranchiseId.newId();
        String branchName = "Sucursal Poblado";
        AddBranchCommand command = new AddBranchCommand(franchiseId, branchName);
        when(franchiseRepositoryPort.existsById(franchiseId)).thenReturn(Mono.just(false));

        Mono<Branch> result = addBranchService.execute(command);

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepositoryPort).existsById(franchiseId);
    }
}
