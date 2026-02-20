package com.st.elitho.jpa;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public final class ELithoMachinePK extends AbstractTablePK implements Serializable {

	private static final long serialVersionUID = -4170518443987146389L;
	private int machineId;

}
