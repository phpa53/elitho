package com.st.elitho.jpa;

import java.io.Serializable;
import java.util.Optional;

import com.st.elitho.dto.ELithoRecipeDetectionDTO;

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
@Table(name = "elithorecipedetection")
@IdClass(ELithoRecipeDetectionPK.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Slf4j
public final class ELithoRecipeDetection
	extends AbstractTable<ELithoRecipeDetectionDTO, ELithoRecipeDetectionPK> implements Serializable {

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
	@Column(name = "scenario")
	private String scenario;

	@Override
	public ELithoRecipeDetectionDTO toDTO() {
		return ELithoRecipeDetectionDTO.builder()
			.recipeName(this.recipeName)
			.tsTechnology(this.twinscanTechnology)
			.tsMaskset(this.twinscanMaskset)
			.tsLayer(this.twinscanLayer)
			.scenario(Optional.ofNullable(this.scenario).orElse(""))
		/*
		.createdDate(Optional.ofNullable(getCreated()).orElse(LocalDateTime.MIN))
		.createdBy(getCreatedBy())
		.lastModifiedDate(Optional.ofNullable(getLastmodified()).orElse(LocalDateTime.MIN))
		.lastModifiedBy(getLastmodifiedby())
		*/
		.build();
	}

	@Override
	public ELithoRecipeDetectionPK getPK() {
		return ELithoRecipeDetectionPK.builder()
			.recipeName(this.recipeName)
			.build();
	}

}
