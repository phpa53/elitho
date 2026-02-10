package com.st.elitho.jsf;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.dto.LotDateDTO;
import com.st.elitho.dto.LotFilterDTO;
import com.st.elitho.ejb.LotEJB;
import com.st.elitho.ejb.LotException;
import com.st.elitho.uti.LoggerUtils;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.model.SelectItem;
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
	private LotDateDTO lotDates;
	private List<String> tools;
	private List<String> technos;
	private List<String> masksets;
	private List<String> layers;
	private List<String> lotIds;
	private List<LocalDateTime> lotStarts;
	private LotFilterDTO filter;
	private LotDTO selectedLot;
	private final transient List<LotDTO> lots = new ArrayList<>();
	@EJB
	private transient LotEJB lotEJB;

	@PostConstruct
    public void init() {
		initFilter();
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

	public List<LotDTO> getLots() {
		return this.lots;
	}

	public void apply() {

		this.lots.clear();
		try {
			this.lots.addAll(this.lotEJB.getLots(this.filter));
			LoggerUtils.info(log, String.format("Found %d lots", this.lots.size()));
		} catch (final LotException e) {
			LoggerUtils.warn(log, e.getMessage());
		}

	}

	public void initFilter() {

		this.filter = new LotFilterDTO();
		this.lotDates = new LotDateDTO();
		this.lotDates.init();
		initTools();
		this.technos = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getTechno);
		this.masksets = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getMaskset);
		this.layers = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getLayer);
		this.lotIds = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getLotId);
		this.lotStarts = new ArrayList<>();

	}

	public void initTools() {
		this.tools = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getCluster);
	}

	public void initTechnos() {
		this.technos = this.lotEJB.getMatchedList(this.tools, LotDTO::getTechno);
	}

	public void initMasksets() {
		this.masksets = this.lotEJB.getMatchedList(this.technos, LotDTO::getMaskset);
	}

	public void initLayers() {
		this.layers = this.lotEJB.getMatchedList(this.masksets, LotDTO::getLayer);
	}

	public void initLotIds() {
		this.lotIds = this.lotEJB.getMatchedList(this.layers, LotDTO::getLotId);
	}

	public void initLotStarts() {
		this.lotStarts = Optional.ofNullable(this.lotIds).orElseGet(ArrayList::new).size() == 1
			? this.lotEJB.getLotStarts(this.lotIds.get(0)) : List.of();
	}

	public List<SelectItem> getFormattedLotStarts() {
	    return this.lotStarts.stream()
	    	.map(ldt -> new SelectItem(ldt, ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))).toList();
	}

}