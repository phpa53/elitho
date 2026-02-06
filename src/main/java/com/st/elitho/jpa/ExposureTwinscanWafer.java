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
@Table(name = "exposuretwinscanwafer")
@IdClass(ExposureTwinscanWaferPK.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class ExposureTwinscanWafer implements Serializable {

	private static final long serialVersionUID = 6102012069743838642L;
	@Id @Column
	private Integer epoch;
	@Id @Column
	private Integer machineuid;
	@Id @Column
	private Integer wafernr;

}
