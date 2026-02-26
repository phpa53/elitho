package com.st.elitho.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.st.elitho.dto.ELithoRecipeDefectClassDTO;
import com.st.elitho.dto.ELithoRecipeDefectDTO;
import com.st.elitho.uti.LoggerWrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "elithorecipedefect")
@IdClass(ELithoRecipeDefectPK.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Slf4j
public final class ELithoRecipeDefect
	extends AbstractTable<ELithoRecipeDefectDTO, ELithoRecipeDefectPK> implements Serializable {

	public static final String LIST_SEP = ";";
	private static final long serialVersionUID = 7960642141292267371L;
	@Id
	@Column(name = "recipename")
	private String recipeName;
	@Column(name = "twinscantechnology")
	private String twinscanTechnology;
	@Column(name = "twinscanmaskset")
	private String twinscanMaskset;
	@Column(name = "twinscanlayer")
	private String twinscanLayer;
	@Column(name = "noisefactor")
	private Float noiseFactor;
	@Column(name = "defectclusterminpixels")
	private Integer defectClusterMinPixels;
	@Column(name = "defectclusterpixelproximity")
	private Float defectClusterPixelProximity;
	@Column(name = "defectclasses")
	private String defectClasses;
	@Column(name = "defectclassminheight")
	private String defectClassMinHeight;
	@Column(name = "defectclassminsize")
	private String defectClassMinSize;
	@Column(name = "defectclassmindensity")
	private String defectClassMinDensity;
	@Column(name = "waferedgeringstart")
	private Integer waferEdgeRingStart;
	@Column(name = "waferedgescenario")
	private String waferEdgeScenario;

	@Override
	public ELithoRecipeDefectDTO toDTO() {
		return ELithoRecipeDefectDTO.builder()
			.recipeName(this.recipeName)
			.tsTechnology(this.twinscanTechnology)
			.tsMaskset(this.twinscanMaskset)
			.tsLayer(this.twinscanLayer)
			.noiseFactor(Optional.ofNullable(this.noiseFactor).orElse(0.0f))
			.defectClasses(getDefectClasses())
			.waferEdgeRingStart(Optional.ofNullable(this.waferEdgeRingStart).orElse(0))
			.waferEdgeScenario(Optional.ofNullable(this.waferEdgeScenario).orElse(""))
		/*
		.createdDate(Optional.ofNullable(getCreated()).orElse(LocalDateTime.MIN))
		.createdBy(getCreatedBy())
		.lastModifiedDate(Optional.ofNullable(getLastmodified()).orElse(LocalDateTime.MIN))
		.lastModifiedBy(getLastmodifiedby())
		*/
		.build();
	}

	private List<ELithoRecipeDefectClassDTO> getDefectClasses() {

		final List<ELithoRecipeDefectClassDTO> list = new ArrayList<>();

		try {

			final List<String> classes = Arrays.asList(this.defectClasses.split(LIST_SEP));
			final var heights = Arrays.asList(this.defectClassMinHeight.split(LIST_SEP)).stream()
				.map(Integer::parseInt).toList();
			final var sizes = Arrays.asList(this.defectClassMinSize.split(LIST_SEP)).stream()
				.map(Integer::parseInt).toList();
			final var densities = Arrays.asList(this.defectClassMinDensity.split(LIST_SEP)).stream()
				.map(Integer::parseInt).toList();

			if (classes.size() == heights.size() && classes.size() == sizes.size()
				&& classes.size() == densities.size()) {
				IntStream.range(0, classes.size()).forEach(index -> list.add(ELithoRecipeDefectClassDTO.builder()
					.name(classes.get(index))
					.minHeight(heights.get(index))
					.minSize(sizes.get(index))
					.minDensity(densities.get(index)).build()));
			}

		} catch(final NumberFormatException e) {
			LoggerWrapper.warn(log, String.format("Bad number format (%s)", e.getMessage()));
		}

		return list;

	}

	@Override
	public ELithoRecipeDefectPK getPK() {
		return ELithoRecipeDefectPK.builder()
			.recipeName(this.recipeName)
			.build();
	}

}
