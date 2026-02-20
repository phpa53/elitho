package com.st.elitho.jpa;

import java.io.Serializable;
import java.util.Optional;

import com.st.elitho.dto.ELithoJobDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "elithojob")
@IdClass(ELithoJobPK.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public final class ELithoJob extends AbstractTable<ELithoJobDTO, ELithoJobPK> implements Serializable {

	private static final long serialVersionUID = 5308047162342829196L;
	@Id
	@Column(name = "machineuid")
	private int machineId;
	@Column(name = "recipedetection")
	private String recipeDetection;
	@Column(name = "recipedefect")
	private String recipeDefect;
	@Column(name = "recipestorage")
	private String recipeStorage;
	@Column(name = "recipeexport")
	private String recipeExport;
	@Column(name = "recipenotification")
	private String recipeNotification;

	@Override
	public ELithoJobDTO toDTO() {
		return ELithoJobDTO.builder()
			.machineId(Optional.ofNullable(this.machineId).orElse(0))
			.recipeDetections(getStringAsList(this.recipeDetection))
			.recipeDefects(getStringAsList(this.recipeDefect))
			.recipeStorage(Optional.ofNullable(this.recipeStorage).orElse(""))
			.recipeExport(Optional.ofNullable(this.recipeExport).orElse(""))
			.recipeNotifications(getStringAsList(this.recipeNotification))
			/*
			.createdDate(Optional.ofNullable(getCreated()).orElse(LocalDateTime.MIN))
			.createdBy(getCreatedBy())
			.lastModifiedDate(Optional.ofNullable(getLastmodified()).orElse(LocalDateTime.MIN))
			.lastModifiedBy(getLastmodifiedby())
			*/
			.build();
	}

	@Override
	public ELithoJobPK getPK() {
		return ELithoJobPK.builder()
			.machineId(this.machineId)
			.build();
	}

}
