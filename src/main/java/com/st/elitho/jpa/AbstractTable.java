package com.st.elitho.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.st.elitho.dto.AbstractTableDTO;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("rawtypes")
@NoArgsConstructor
//@AllArgsConstructor
@Data
@SuperBuilder
@MappedSuperclass
public sealed abstract class AbstractTable<T extends AbstractTableDTO, U extends AbstractTablePK>
	permits ELithoJob, ELithoMachine, ELithoMissingNotification {

	/*
	@Column(name = "created")
	private LocalDateTime created;
	@Column(name = "createdBy")
	private String createdBy;
	@Column(name = "lastmodified")
	private LocalDateTime lastmodified;
	@Column(name = "lastmodifiedby")
	private String lastmodifiedby;
	*/

	public abstract T toDTO();
	public abstract U getPK();

	public static List<String> getStringAsList(final String value) {
		return Optional.ofNullable(value).orElse("").isEmpty() ? new ArrayList<>()
			: Arrays.asList(value.trim().split(";")).stream().map(String::trim).toList();
	}

}
