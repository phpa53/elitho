package com.st.elitho.ejb;


import com.st.elitho.dto.ELithoRecipeDetectionDTO;
import com.st.elitho.jpa.ELithoRecipeDetection;

import jakarta.ejb.Stateless;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Stateless
public class ELithoRecipeDetectionEJB extends AbstractTableEJB<ELithoRecipeDetection, ELithoRecipeDetectionDTO> {

	@Override
	protected Class<ELithoRecipeDetection> getEntityClass() {
		return ELithoRecipeDetection.class;
	}

}
