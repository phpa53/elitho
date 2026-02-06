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
public final class ExposureTwinscanLotPK implements Serializable {

	private static final long serialVersionUID = -3814957713974567857L;
	private Integer epoch;
	private Integer machineuid;


}
