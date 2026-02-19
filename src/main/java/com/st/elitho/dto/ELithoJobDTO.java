package com.st.elitho.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	private String recipeDefect;
	private String recipeStorage;
	private String recipeExport;
	private String recipeNotification;

	@Override
	public ELithoJob toEntity() {
		return ELithoJob.builder()
			.machineId(this.machineId)
			.recipeDetection(getListAsString(this.recipeDetections))
			.recipeDefect(this.recipeDefect)
			.recipeStorage(this.recipeStorage)
			.recipeExport(this.recipeExport)
			.recipeNotification(this.recipeNotification)
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
		return String.format("%s", this.machineId);
	}

	@Override
	public ELithoJobDTO getDefault() {
		return ELithoJobDTO.builder()
			.machineId(0)
			.recipeDetections(new ArrayList<>())
			.recipeDefect(VALUE_DEFAULT)
			.recipeStorage(VALUE_DEFAULT)
			.recipeExport(VALUE_DEFAULT)
			.recipeNotification(VALUE_DEFAULT)
			.build();
	}

	@Override
	public ELithoJobDTO getCopy() {
		return ELithoJobDTO.builder()
			.machineId(0)
			.recipeDetections(new ArrayList<>())
			.recipeDefect(VALUE_DEFAULT)
			.recipeStorage(VALUE_DEFAULT)
			.recipeExport(VALUE_DEFAULT)
			.recipeNotification(VALUE_DEFAULT)
			.build();
	}

	@Override
	public boolean toBeFilled() {
		return VALUE_DEFAULT.equals(this.machineId)
			|| VALUE_DEFAULT.equals(this.recipeDetections)
			|| VALUE_DEFAULT.equals(this.recipeDefect)
			|| VALUE_DEFAULT.equals(this.recipeStorage)
			|| VALUE_DEFAULT.equals(this.recipeExport)
			|| VALUE_DEFAULT.equals(this.recipeNotification);
	}

	@Override
	public void clearChangedAttributes() {
		getChangedAttributes().clear();
	}

	@Override
	public void setChangedAttributeName(final String name) {
		getChangedAttributes().add(name);
	}

	@Override
	public boolean hasAttributeChanged(final String name) {
		return getChangedAttributes().contains(name);
	}

	@Override
	public String getAttributeColor() {
		return isDuplicatedPK() ? "red" : "inherit";
	}

	@Override
	public boolean isCreatedDateValid() {
		return !Optional.ofNullable(getCreatedDate()).orElse(LocalDateTime.MIN).equals(LocalDateTime.MIN);
	}

	@Override
	public boolean isLastModifiedDateValid() {
		return !Optional.ofNullable(getLastModifiedDate()).orElse(LocalDateTime.MIN).equals(LocalDateTime.MIN);
	}

	private static String getListAsString(final List<String> list) {
		return Optional.ofNullable(list).orElse(new ArrayList<>()).stream().collect(Collectors.joining(";"));
	}

	private static String getListAsText(final List<String> list) {
		return Optional.ofNullable(list).orElse(new ArrayList<>()).stream()
			.sorted()
            .map(value -> String.format("%s<br/>", value))
            .collect(Collectors.joining());
	}

	public String getDetectionAsText() {
		return getListAsText(this.recipeDetections);
	}

	public String getDetectionListCompact() {

		final var list = Optional.ofNullable(this.recipeDetections).orElse(new ArrayList<>());

		return list.isEmpty() ? "" : String.format("%d detection%s", list.size(), list.size() == 1 ? "" : "s");

	}

}
