package com.accenture.franchiseapi.application.port.in.branch;

import com.accenture.franchiseapi.application.command.branch.TopStockPerBranch;
import com.accenture.franchiseapi.application.dto.TopStockProductView;
import reactor.core.publisher.Flux;

public interface TopStockPerBranchUseCase {
    Flux<TopStockProductView> execute(TopStockPerBranch command);
}
