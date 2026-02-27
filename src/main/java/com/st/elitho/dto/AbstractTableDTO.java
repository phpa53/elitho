package com.st.elitho.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.st.elitho.jpa.AbstractTable;
import com.st.elitho.jsf.AbstractTableBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Slf4j
public sealed abstract class AbstractTableDTO<T extends AbstractTable<?, ?>, U, V>
	permits ELithoJobDTO, ELithoMachineDTO, ELithoMissingNotificationDTO, ELithoRecipeDefectDTO,
		ELithoRecipeDetectionDTO {

	public static final String VALUE_DEFAULT = "value";
	private LocalDateTime createdDate;
	private String createdBy;
	private LocalDateTime lastModifiedDate;
	private String lastModifiedBy;
	private boolean duplicatedPK;
	private final transient Set<String> changedAttributes = new HashSet<>();

	public abstract T toEntity();
	public abstract U getPK();
	public abstract String getKey();
	public abstract V getDefault();
	public abstract V getCopy();
	public abstract boolean toBeFilled();

	/*
	public void setDates(final T entity) {

		entity.setCreated(Optional.ofNullable(this.createdDate).orElse(LocalDateTime.MIN).equals(LocalDateTime.MIN)
			? null : this.createdDate);
		entity.setCreatedBy(Optional.ofNullable(this.createdBy).orElse(""));
		entity.setLastmodified(Optional.ofNullable(this.createdDate).orElse(LocalDateTime.MIN).equals(LocalDateTime.MIN)
			? null : this.createdDate);
		entity.setLastmodifiedby(Optional.ofNullable(this.createdBy).orElse(""));

	}
	*/

	public static String getListAsString(final List<String> list) {
		return Optional.ofNullable(list).orElse(new ArrayList<>()).stream().collect(Collectors.joining(";"));
	}

	@SuppressWarnings("static-method")
	public String getListAsText(final List<String> list) {
		return Optional.ofNullable(list).orElse(new ArrayList<>()).stream()
			.sorted()
            .map(value -> String.format("%s<br/>", value))
            .collect(Collectors.joining());
	}

	@SuppressWarnings("static-method")
	public String getListCompactList(final List<String> values, final String label) {
		final var list = Optional.ofNullable(values).orElse(new ArrayList<>());
		return list.isEmpty() ? "" : String.format("%d %s%s", list.size(), label, list.size() == 1 ? "" : "s");
	}

	@SuppressWarnings("static-method")
	public String getFormattedEmailList(final List<String> list, final boolean wrap) {
		return Optional.ofNullable(list).orElse(new ArrayList<>()).stream()
			.sorted()
            .map(email -> String.format("%s%s", String.format("%s%s%s",
            	AbstractTableBean.isValidEmail(email) ? "" : "<b><span style=\"color: red;\">",
            		email,
            		AbstractTableBean.isValidEmail(email) ? "" : "</span></b>"),
            	wrap ? "<br/>" : ""))
            .collect(Collectors.joining());
	}

	public void clearChangedAttributes() {
		this.changedAttributes.clear();
	}

	public void setChangedAttributeName(final String name) {
		this.changedAttributes.add(name);
	}

	public boolean hasAttributeChanged(final String name) {
		return this.changedAttributes.contains(name);
	}

	public String getAttributeColor() {
		return this.duplicatedPK ? "red" : "inherit";
	}

	public boolean isCreatedDateValid() {
		return !Optional.ofNullable(this.createdDate).orElse(LocalDateTime.MIN).equals(LocalDateTime.MIN);
	}

	public boolean isLastModifiedDateValid() {
		return !Optional.ofNullable(this.lastModifiedDate).orElse(LocalDateTime.MIN).equals(LocalDateTime.MIN);
	}

	@SuppressWarnings("static-method")
	public String getListTooltip(final List<String> list) {
		return Optional.ofNullable(list).orElse(new ArrayList<>()).stream()
			.sorted()
            .map(email -> String.format("%s<br/>", email))
            .collect(Collectors.joining());
	}

	@SuppressWarnings("static-method")
	public String getEmailListTooltipStyle(final List<String> list) {
		return Optional.ofNullable(list).orElse(new ArrayList<>()).stream()
    		.filter(email -> !AbstractTableBean.isValidEmail(email)).findFirst().isPresent() ? "tooltip-error" : "";
	}

	@SuppressWarnings("static-method")
	public String getEmailListColor(final List<String> list) {
    	return Optional.ofNullable(list).orElse(new ArrayList<>()).stream()
    		.filter(email -> !AbstractTableBean.isValidEmail(email)).findFirst().isPresent() ? "red" : "inherit";
    }

}
