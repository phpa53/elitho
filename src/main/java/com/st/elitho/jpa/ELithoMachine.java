package com.st.elitho.jpa;

import java.io.Serializable;
import java.util.Optional;

import com.st.elitho.dto.ELithoMachineDTO;

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
@Table(name = "elithomachine")
@IdClass(ELithoMachinePK.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public final class ELithoMachine extends AbstractTable<ELithoMachineDTO, ELithoMachinePK> implements Serializable {

	private static final long serialVersionUID = 6792167929219045675L;
	@Id
	@Column(name = "machineuid")
	private int machineId;
	@Column(name = "enabled")
	private Boolean enabled;
	@Column(name = "machinenr")
	private Integer machineNr;
	@Column(name = "ip")
	private String ip;
	@Column(name = "serverid")
	private String serverId;
	@Column(name = "serverthreadnr")
	private Integer serverThreadNr;
	@Column(name = "batchsize")
	private Integer batchSize;
	@Column(name = "maxhourscatchup")
	private Float maxHoursCatchup;
	@Column(name = "mdlfilesizemin")
	private Integer mdlFileSizeMin;
	@Column(name = "mdlsourcepath")
	private String mdlSourcePath;
	@Column(name = "lithoclusterid")
	private String lithoClusterId;

	@Override
	public ELithoMachineDTO toDTO() {
		return ELithoMachineDTO.builder()
			.machineId(this.machineId)
			.enabled(Optional.ofNullable(this.enabled).orElse(false))
			.machineNr(Optional.ofNullable(this.machineNr).orElse(0))
			.ip(Optional.ofNullable(this.ip).orElse(""))
			.serverId(Optional.ofNullable(this.serverId).orElse(""))
			.serverThreadNr(Optional.ofNullable(this.serverThreadNr).orElse(0))
			.batchSize(Optional.ofNullable(this.batchSize).orElse(0))
			.maxHoursCatchup(Optional.ofNullable(this.maxHoursCatchup).orElse(0.0f))
			.mdlFileSizeMin(Optional.ofNullable(this.mdlFileSizeMin).orElse(0))
			.mdlSourcePath(Optional.ofNullable(this.mdlSourcePath).orElse(""))
			.lithoClusterId(Optional.ofNullable(this.lithoClusterId).orElse(""))
		/*
		.createdDate(Optional.ofNullable(getCreated()).orElse(LocalDateTime.MIN))
		.createdBy(getCreatedBy())
		.lastModifiedDate(Optional.ofNullable(getLastmodified()).orElse(LocalDateTime.MIN))
		.lastModifiedBy(getLastmodifiedby())
		*/
		.build();
	}

	@Override
	public ELithoMachinePK getPK() {
		return ELithoMachinePK.builder()
			.machineId(this.machineId)
			.build();
	}

}
