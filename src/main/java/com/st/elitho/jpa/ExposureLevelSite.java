package com.st.elitho.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exposurelevelsite")
@IdClass(ExposureLevelSitePK.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class ExposureLevelSite implements Serializable {

	private static final long serialVersionUID = -4212691681829318518L;
	@Id @Column
	private Integer epoch;
	@Id @Column
	private Integer machineuid;
	@Id @Column
	private Integer wafernr;
	@Id @Column
	private Integer sitenr;
	@Id @Column
	private Integer exposurenr;
	@Column
	private Integer znce;

}
