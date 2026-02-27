package com.st.elitho.dto;

import java.io.Serializable;

import com.st.elitho.jpa.ELithoRecipeDetection;
import com.st.elitho.jpa.ELithoRecipeDetectionPK;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public final class ELithoRecipeDetectionDTO
	extends AbstractTableDTO<ELithoRecipeDetection, ELithoRecipeDetectionPK, ELithoRecipeDetectionDTO>
	implements Serializable {

	private static final long serialVersionUID = 7213520799747280100L;
	private String recipeName;
	private String tsTechnology;
	private String tsMaskset;
	private String tsLayer;
	private String scenario;

	@Override
	public ELithoRecipeDetection toEntity() {
		return ELithoRecipeDetection.builder()
			.recipeName(this.recipeName)
			.twinscanTechnology(this.tsTechnology)
			.twinscanMaskset(this.tsMaskset)
			.twinscanLayer(this.tsLayer)
			.scenario(this.scenario)
			.build();
	}

	@Override
	public ELithoRecipeDetectionPK getPK() {
		return ELithoRecipeDetectionPK.builder()
			.recipeName(this.recipeName)
			.build();
	}

	@Override
	public String getKey() {
		return String.format("%s", this.recipeName);
	}

	@Override
	public ELithoRecipeDetectionDTO getDefault() {
		return ELithoRecipeDetectionDTO.builder()
			.recipeName(this.recipeName)
			.tsTechnology(VALUE_DEFAULT)
			.tsMaskset(VALUE_DEFAULT)
			.tsLayer(VALUE_DEFAULT)
			.scenario(VALUE_DEFAULT)
			.build();
	}

	@Override
	public ELithoRecipeDetectionDTO getCopy() {
		return ELithoRecipeDetectionDTO.builder()
			.recipeName(this.recipeName)
			.tsTechnology(this.tsTechnology)
			.tsMaskset(this.tsMaskset)
			.tsLayer(this.tsLayer)
			.scenario(this.scenario)
			.build();
	}

	@Override
	public boolean toBeFilled() {
		return VALUE_DEFAULT.equals(this.recipeName)
			|| VALUE_DEFAULT.equals(this.tsTechnology)
			|| VALUE_DEFAULT.equals(this.tsMaskset)
			|| VALUE_DEFAULT.equals(this.tsLayer);
	}

}