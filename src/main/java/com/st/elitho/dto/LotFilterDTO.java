package com.st.elitho.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class LotFilterDTO {

	private List<String> clusters;
	private List<String> technos;
	private List<String> masksets;
	private List<String> layers;
	private List<String> lotIds;
	private LocalDate lotStart;

	public void init() {

		this.clusters = new ArrayList<>();
		this.technos = new ArrayList<>();
		this.masksets = new ArrayList<>();
		this.layers = new ArrayList<>();
		this.lotIds = new ArrayList<>();
		this.lotStart = LocalDate.MIN;

	}

}
