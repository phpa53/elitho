package com.st.elitho.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.st.elitho.jpa.ELithoRecipeDefect;
import com.st.elitho.jpa.ELithoRecipeDefectPK;

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
public final class ELithoRecipeDefectDTO
	extends AbstractTableDTO<ELithoRecipeDefect, ELithoRecipeDefectPK, ELithoRecipeDefectDTO>
	implements Serializable {

	private static final long serialVersionUID = -7356654076920735749L;
	private String recipeName;
	private String tsTechnology;
	private String tsMaskset;
	private String tsLayer;
	private Float noiseFactor;
	private Integer defectClusterMinPixels;
	private Float defectClusterPixelProximity;
	private List<ELithoRecipeDefectClassDTO> defectClasses;
	private Integer waferEdgeRingStart;
	private String waferEdgeScenario;

	@Override
	public ELithoRecipeDefect toEntity() {
		return ELithoRecipeDefect.builder()
			.recipeName(this.recipeName)
			.twinscanTechnology(this.tsTechnology)
			.twinscanMaskset(this.tsMaskset)
			.twinscanLayer(this.tsLayer)
			.noiseFactor(this.noiseFactor)
			.defectClusterMinPixels(this.defectClusterMinPixels)
			.defectClusterPixelProximity(this.defectClusterPixelProximity)
			.defectClasses(String.join(ELithoRecipeDefect.LIST_SEP, this.defectClasses.stream()
				.map(ELithoRecipeDefectClassDTO::getName).toList()))
			.defectClassMinHeight(String.join(ELithoRecipeDefect.LIST_SEP, this.defectClasses.stream()
				.map(dto -> Integer.toString(dto.getMinHeight())).toList()))
			.defectClassMinSize(String.join(ELithoRecipeDefect.LIST_SEP, this.defectClasses.stream()
				.map(dto -> Integer.toString(dto.getMinSize())).toList()))
			.defectClassMinDensity(String.join(ELithoRecipeDefect.LIST_SEP, this.defectClasses.stream()
				.map(dto -> Integer.toString(dto.getMinDensity())).toList()))
			.waferEdgeRingStart(this.waferEdgeRingStart)
			.waferEdgeScenario(this.waferEdgeScenario)
			.build();
	}

	@Override
	public ELithoRecipeDefectPK getPK() {
		return ELithoRecipeDefectPK.builder()
			.recipeName(this.recipeName)
			.build();
	}

	@Override
	public String getKey() {
		return String.format("%s", this.recipeName);
	}

	@Override
	public ELithoRecipeDefectDTO getDefault() {
		return ELithoRecipeDefectDTO.builder()
			.recipeName(this.recipeName)
			.tsTechnology(VALUE_DEFAULT)
			.tsMaskset(VALUE_DEFAULT)
			.tsLayer(VALUE_DEFAULT)
			.noiseFactor(0.0f)
			.defectClusterMinPixels(0)
			.defectClusterPixelProximity(0.0f)
			.defectClasses(new ArrayList<>(List.of(ELithoRecipeDefectClassDTO.builder()
				.name(VALUE_DEFAULT).build())))
			.waferEdgeRingStart(0)
			.waferEdgeScenario(VALUE_DEFAULT)
			.build();
	}

	@Override
	public ELithoRecipeDefectDTO getCopy() {
		return ELithoRecipeDefectDTO.builder()
			.recipeName(this.recipeName)
			.tsTechnology(this.tsTechnology)
			.tsMaskset(this.tsMaskset)
			.tsLayer(this.tsLayer)
			.noiseFactor(this.noiseFactor)
			.defectClasses(new ArrayList<>(this.defectClasses.stream().map(dto -> ELithoRecipeDefectClassDTO.builder()
				.name(dto.getName()).minHeight(dto.getMinHeight()).minSize(dto.getMinSize())
				.minDensity(dto.getMinDensity()).build()).toList()))
			.waferEdgeRingStart(this.waferEdgeRingStart)
			.waferEdgeScenario(this.waferEdgeScenario)
			.build();
	}

	@Override
	public boolean toBeFilled() {
		return VALUE_DEFAULT.equals(this.recipeName)
			|| VALUE_DEFAULT.equals(this.tsTechnology)
			|| VALUE_DEFAULT.equals(this.tsMaskset)
			|| VALUE_DEFAULT.equals(this.tsLayer)
			|| Optional.ofNullable(this.defectClasses).orElse(new ArrayList<>()).isEmpty();
	}

}