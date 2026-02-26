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
public final class ELithoRecipeDefectPK extends AbstractTablePK implements Serializable {

	private static final long serialVersionUID = 5339914403950181064L;
	private String recipeName;

}
