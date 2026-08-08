package com.accenture.franchiseapi.application.port.in.franchise;

import com.accenture.franchiseapi.application.view.TopStockProductView;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import reactor.core.publisher.Flux;

public interface TopStockPerBranchUseCase {
    Flux<TopStockProductView> execute(FranchiseId franchiseId);
}
