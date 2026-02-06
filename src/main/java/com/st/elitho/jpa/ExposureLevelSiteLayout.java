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
@Table(name = "exposurelevelsitelayout")
@IdClass(ExposureLevelSiteLayoutPK.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class ExposureLevelSiteLayout implements Serializable {

	private static final long serialVersionUID = -4212691681829318518L;
	@Id @Column
	private Integer epoch;
	@Id @Column
	private Integer machineuid;
	@Id @Column
	private Integer sitenr;
	@Column
	private Float sitex;
	@Column
	private Float sitey;

}
