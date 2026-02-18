package com.st.elitho.ejb;


import com.st.elitho.dto.ELithoJobDTO;
import com.st.elitho.jpa.ELithoJob;

import jakarta.ejb.Stateless;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Stateless
public class ELithoJobEJB extends AbstractTableEJB<ELithoJob, ELithoJobDTO> {

	@Override
	protected Class<ELithoJob> getEntityClass() {
		return ELithoJob.class;
	}

}
