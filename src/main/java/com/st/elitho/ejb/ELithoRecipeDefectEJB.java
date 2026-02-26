package com.st.elitho.ejb;


import com.st.elitho.dto.ELithoRecipeDefectDTO;
import com.st.elitho.jpa.ELithoRecipeDefect;

import jakarta.ejb.Stateless;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Stateless
public class ELithoRecipeDefectEJB extends AbstractTableEJB<ELithoRecipeDefect, ELithoRecipeDefectDTO> {

	@Override
	protected Class<ELithoRecipeDefect> getEntityClass() {
		return ELithoRecipeDefect.class;
	}

}
