package com.accenture.franchiseapi.application.command.branch;

import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;

public record AddBranchCommand(FranchiseId franchiseId, String branchName) {
}
