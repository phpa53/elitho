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
public final class ExposureTwinscanWaferPK implements Serializable {

	private static final long serialVersionUID = 151930478689619995L;
	private Integer epoch;
	private Integer machineuid;
	private Integer wafernr;


}
