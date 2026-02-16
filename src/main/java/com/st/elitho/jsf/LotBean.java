package com.st.elitho.jsf;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.primefaces.event.SelectEvent;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.dto.LotDateDTO;
import com.st.elitho.dto.LotFilterDTO;
import com.st.elitho.ejb.LotEJB;
import com.st.elitho.ejb.LotException;
import com.st.elitho.uti.LoggerUtils;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Named
@ViewScoped
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class LotBean implements Serializable {

	private static final long serialVersionUID = -5895065530297045121L;
	private LotDateDTO lotDates;
	private List<String> clusters;
	private List<String> technos;
	private List<String> masksets;
	private List<String> layers;
	private List<String> lotIds;
	private List<LocalDateTime> lotStarts;
	private LotFilterDTO filter;
	private LotDTO selectedLot;
	private String lotDetailsUrl;
	private final List<LotDTO> lots = new ArrayList<>();
	@Inject
    private TopBean topBean;
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
			Collections.sort(this.lots, Comparator.comparing(LotDTO::getStart).reversed()
				.thenComparing(LotDTO::getCluster)
				.thenComparing(LotDTO::getTechno)
				.thenComparing(LotDTO::getMaskset)
				.thenComparing(LotDTO::getLayer)
				.thenComparing(LotDTO::getLotId));
			LoggerUtils.info(log, String.format("Found %d lots", this.lots.size()));

		} catch (final LotException e) {

			LoggerUtils.warn(log, e.getMessage());

		}

	}

	public void initFilter() {

		this.filter = new LotFilterDTO();
		this.lotDates = new LotDateDTO();
		this.lotDates.init();
		initClusters();
		this.technos = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getTechno);
		this.masksets = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getMaskset);
		this.layers = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getLayer);
		this.lotIds = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getLotId);
		this.lotStarts = new ArrayList<>();

	}

	public void initClusters() {
		this.clusters = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getCluster);
		initTechnos(this.clusters);
	}
	public void initTechnos() {
		this.technos = this.lotEJB.getMatchedList(this.filter.getClusters(), LotDTO::getCluster, LotDTO::getTechno);
		initMasksets(this.technos);
	}
	public void initTechnos(final List<String> preFilteredTools) {
		this.technos = this.lotEJB.getMatchedList(preFilteredTools, LotDTO::getCluster, LotDTO::getTechno);
		initMasksets(this.technos);
	}
	public void initMasksets() {
		this.masksets = this.lotEJB.getMatchedList(this.filter.getTechnos(), LotDTO::getTechno, LotDTO::getMaskset);
		initLayers(this.masksets);
	}
	public void initMasksets(final List<String> preFilteredTechnos) {
		this.masksets = this.lotEJB.getMatchedList(preFilteredTechnos, LotDTO::getTechno, LotDTO::getMaskset);
		initLayers(this.masksets);
	}
	public void initLayers() {
		this.layers = this.lotEJB.getMatchedList(this.filter.getMasksets(), LotDTO::getMaskset, LotDTO::getLayer);
		initLotIds(this.layers);
	}
	public void initLayers(final List<String> preFilteredMasksets) {
		this.layers = this.lotEJB.getMatchedList(preFilteredMasksets, LotDTO::getMaskset, LotDTO::getLayer);
		initLotIds(this.layers);
	}
	public void initLotIds() {
		this.lotIds = this.lotEJB.getMatchedList(this.filter.getLayers(), LotDTO::getLayer, LotDTO::getLotId);
	}
	public void initLotIds(final List<String> preFilteredLayers) {
		this.lotIds = this.lotEJB.getMatchedList(preFilteredLayers, LotDTO::getLayer, LotDTO::getLotId);
	}
	public void initLotStarts() {
		this.lotStarts = Optional.ofNullable(this.lotIds).orElseGet(ArrayList::new).size() == 1
			? this.lotEJB.getLotStarts(this.lotIds.get(0)) : List.of();
	}

	public List<SelectItem> getFormattedLotStarts() {
	    return this.lotStarts.stream()
	    	.map(ldt -> new SelectItem(ldt, ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))).toList();
	}

	public void onLotRowDblClick(final SelectEvent<LotDTO> event) {

        final var lot = event.getObject();

        if (lot != null) {

        	final var encodedLotId = URLEncoder.encode(lot.getLotId(), StandardCharsets.UTF_8);
            final var encodedStart = URLEncoder.encode(lot.getFormattedStart(), StandardCharsets.UTF_8);

        	this.selectedLot = lot;
            this.lotDetailsUrl = String.format("xhtml/lotdetails.xhtml?lotId=%s&startDate=%s",
            	encodedLotId, encodedStart);

        }

    }

}