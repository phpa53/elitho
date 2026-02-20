package com.st.elitho.dto;

import java.io.Serializable;

import com.st.elitho.jpa.ELithoMachine;
import com.st.elitho.jpa.ELithoMachinePK;

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
public final class ELithoMachineDTO
	extends AbstractTableDTO<ELithoMachine, ELithoMachinePK, ELithoMachineDTO>
	implements Serializable {

	private static final long serialVersionUID = -7761148924100025484L;
	private int machineId;
	private boolean enabled;
	private int machineNr;
	private String ip;
	private String serverId;
	private int serverThreadNr;
	private int batchSize;
	private float maxHoursCatchup;
	private int mdlFileSizeMin;
	private String mdlSourcePath;
	private String lithoClusterId;

	@Override
	public ELithoMachine toEntity() {
		return ELithoMachine.builder()
			.machineId(this.machineId)
			.enabled(this.enabled)
			.machineNr(this.machineNr)
			.ip(this.ip)
			.serverId(this.serverId)
			.serverThreadNr(this.serverThreadNr)
			.batchSize(this.batchSize)
			.maxHoursCatchup(this.maxHoursCatchup)
			.mdlFileSizeMin(this.mdlFileSizeMin)
			.mdlSourcePath(this.mdlSourcePath)
			.lithoClusterId(this.lithoClusterId)
			.build();
	}

	@Override
	public ELithoMachinePK getPK() {
		return ELithoMachinePK.builder()
			.machineId(this.machineId)
			.build();
	}

	@Override
	public String getKey() {
		return String.format("%d", this.machineId);
	}

	@Override
	public ELithoMachineDTO getDefault() {
		return ELithoMachineDTO.builder()
			.machineId(0)
			.enabled(false)
			.machineNr(0)
			.ip(VALUE_DEFAULT)
			.serverId(VALUE_DEFAULT)
			.serverThreadNr(0)
			.batchSize(0)
			.maxHoursCatchup(0.0f)
			.mdlFileSizeMin(0)
			.mdlSourcePath(VALUE_DEFAULT)
			.lithoClusterId(VALUE_DEFAULT)
			.build();
	}

	@Override
	public ELithoMachineDTO getCopy() {
		return ELithoMachineDTO.builder()
			.machineId(0)
			.enabled(this.enabled)
			.machineNr(this.machineNr)
			.ip(this.ip)
			.serverId(this.serverId)
			.serverThreadNr(this.serverThreadNr)
			.batchSize(this.batchSize)
			.maxHoursCatchup(this.maxHoursCatchup)
			.mdlFileSizeMin(this.mdlFileSizeMin)
			.mdlSourcePath(this.mdlSourcePath)
			.lithoClusterId(this.lithoClusterId)
			.build();
	}

	@Override
	public boolean toBeFilled() {
		return this.machineId == 0
			|| VALUE_DEFAULT.equals(this.ip)
			|| VALUE_DEFAULT.equals(this.serverId)
			|| VALUE_DEFAULT.equals(this.mdlSourcePath)
			|| VALUE_DEFAULT.equals(this.lithoClusterId);
	}

}
