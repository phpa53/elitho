package com.st.elitho.jpa;

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
	permits ELithoJob {

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

}
