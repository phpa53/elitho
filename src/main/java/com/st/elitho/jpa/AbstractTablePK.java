package com.st.elitho.jpa;

import lombok.Data;

@Data
public sealed abstract class AbstractTablePK permits ELithoJobPK, ELithoMachinePK,
	ELithoMissingNotificationPK, ELithoRecipeDefectPK { // NOPMD
	// No abstract method
}
