package com.st.elitho.jsf;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.st.elitho.dto.LotDTO;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Named
@SessionScoped
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class LotBean implements Serializable {

	private static final long serialVersionUID = -5895065530297045121L;
	private List<LotDTO> lots;
	private LotDTO selectedLot;
	private LocalDate fromDate;
	private LocalDate toDate;
	private List<String> tools;
	private List<String> selectedTools;
	private List<String> technos;
	private List<String> selectedTechnos;
	private List<String> masksets;
	private List<String> selectedMasksets;
	private List<String> layers;
	private List<String> selectedLayers;
	private List<String> lotIds;
	private List<String> selectedLotIds;
	private List<String> lotStarts;
	private List<String> selectedLotStarts;

	@PostConstruct
    public void init() {
		// TBI
    }

	@SuppressWarnings("static-method")
	public String getExcelExtension() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYMMdd-HH.mm"));
	}

	@SuppressWarnings("static-method")
	public void postProcessXLS(final Object document) {
		if (document != null) {
			//TBI
		}
	}

	public void apply() {
		//TBI
	}

	public void initTechnos() {

	}

	public void initMasksets() {

	}

	public void initLayers() {

	}

	public void initLotIds() {

	}

	public void initLotStarts() {

	}

}