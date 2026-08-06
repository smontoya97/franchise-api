package com.accenture.franchiseapi.application.command.branch;

import com.accenture.franchiseapi.domain.model.valueobject.BranchId;

public record RenameBranchCommand(BranchId branchId, String newName) {
}
