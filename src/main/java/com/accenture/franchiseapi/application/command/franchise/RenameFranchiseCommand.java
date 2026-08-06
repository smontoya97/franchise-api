package com.accenture.franchiseapi.application.command.franchise;

import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;

public record RenameFranchiseCommand(FranchiseId franchiseId, String newName) {
}
