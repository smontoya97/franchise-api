package com.accenture.franchiseapi.application.command.branch;

import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;

public record RenameBranchCommand(FranchiseId franchiseId, BranchId branchId, String newName) {
}
