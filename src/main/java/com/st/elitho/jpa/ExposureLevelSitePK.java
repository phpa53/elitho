package com.st.elitho.jpa;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public final class ExposureLevelSitePK implements Serializable {

	private static final long serialVersionUID = 6853595981694823203L;
	private Integer epoch;
	private Integer machineuid;
	private Integer exposurenr;
	private Integer wafernr;
	private Integer sitenr;


}
