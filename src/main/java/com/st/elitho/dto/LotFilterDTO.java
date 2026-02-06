package com.st.elitho.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class LotFilterDTO {

	private String lotId;
	private String techno;
	private String maskset;
	private String layer;

}
