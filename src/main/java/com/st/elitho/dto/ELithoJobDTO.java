package com.st.elitho.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.st.elitho.jpa.ELithoJob;
import com.st.elitho.jpa.ELithoJobPK;

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
public final class ELithoJobDTO
	extends AbstractTableDTO<ELithoJob, ELithoJobPK, ELithoJobDTO>
	implements Serializable {

	private static final long serialVersionUID = -7761148924100025484L;
	private int machineId;
	private List<String> recipeDetections;
	private List<String> recipeDefects;
	private String recipeStorage;
	private String recipeExport;
	private List<String> recipeNotifications;

	@Override
	public ELithoJob toEntity() {
		return ELithoJob.builder()
			.machineId(this.machineId)
			.recipeDetection(getListAsString(this.recipeDetections))
			.recipeDefect(getListAsString(this.recipeDefects))
			.recipeStorage(this.recipeStorage)
			.recipeExport(this.recipeExport)
			.recipeNotification(getListAsString(this.recipeNotifications))
			.build();
	}

	@Override
	public ELithoJobPK getPK() {
		return ELithoJobPK.builder()
			.machineId(this.machineId)
			.build();
	}

	@Override
	public String getKey() {
		return String.format("%d", this.machineId);
	}

	@Override
	public ELithoJobDTO getDefault() {
		return ELithoJobDTO.builder()
			.machineId(0)
			.recipeDetections(new ArrayList<>())
			.recipeDefects(new ArrayList<>())
			.recipeStorage(VALUE_DEFAULT)
			.recipeExport(VALUE_DEFAULT)
			.recipeNotifications(new ArrayList<>())
			.build();
	}

	@Override
	public ELithoJobDTO getCopy() {
		return ELithoJobDTO.builder()
			.machineId(0)
			.recipeDetections(this.recipeDetections)
			.recipeDefects(this.recipeDefects)
			.recipeStorage(this.recipeStorage)
			.recipeExport(this.recipeExport)
			.recipeNotifications(this.recipeNotifications)
			.build();
	}

	@Override
	public boolean toBeFilled() {
		return this.machineId == 0
			|| Optional.ofNullable(this.recipeDetections).orElse(new ArrayList<>()).isEmpty()
			|| Optional.ofNullable(this.recipeDefects).orElse(new ArrayList<>()).isEmpty()
			|| VALUE_DEFAULT.equals(this.recipeStorage)
			|| VALUE_DEFAULT.equals(this.recipeExport)
			|| Optional.ofNullable(this.recipeNotifications).orElse(new ArrayList<>()).isEmpty();
	}

}
