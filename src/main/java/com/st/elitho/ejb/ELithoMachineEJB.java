package com.st.elitho.ejb;


import com.st.elitho.dto.ELithoMachineDTO;
import com.st.elitho.jpa.ELithoMachine;

import jakarta.ejb.Stateless;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Stateless
public class ELithoMachineEJB extends AbstractTableEJB<ELithoMachine, ELithoMachineDTO> {

	@Override
	protected Class<ELithoMachine> getEntityClass() {
		return ELithoMachine.class;
	}

}
