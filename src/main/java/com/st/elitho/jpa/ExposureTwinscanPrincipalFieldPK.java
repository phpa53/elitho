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
public final class ExposureTwinscanPrincipalFieldPK implements Serializable {

	private static final long serialVersionUID = -4069479591989424424L;
	private Integer epoch;
	private Integer machineuid;
	private Integer exposurenr;


}
