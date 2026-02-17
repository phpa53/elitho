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
import java.util.Locale;
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
	private LotFilterDTO lotFilter;
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

			this.lots.addAll(this.lotEJB.getLots(this.lotFilter));
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

		this.lotFilter = new LotFilterDTO();
		this.lotDates = new LotDateDTO();
		this.lotDates.init();
		this.lotStarts = new ArrayList<>();
		initClusters();

	}

	public void initClusters() {
		this.clusters = this.lotEJB.getAttributeList(this.lotDates, LotDTO::getCluster);
		this.lotFilter.setClusters(new ArrayList<>());
		initTechnos();
	}
	private List<String> getFilteredClusters() {
		return Optional.ofNullable(this.lotFilter.getClusters()).orElse(List.of()).isEmpty() ? this.clusters
			: this.lotFilter.getClusters();
	}
	public void initTechnos() {
		this.technos = this.lotEJB.getFilteredTechnos(getFilteredClusters());
		initMasksets();
	}
	private List<String> getFilteredTechnos() {
		return Optional.ofNullable(this.lotFilter.getTechnos()).orElse(List.of()).isEmpty() ? this.technos
			: this.lotFilter.getTechnos();
	}
	public void initMasksets() {
		this.masksets = this.lotEJB.getFilteredMasksets(getFilteredClusters(), getFilteredTechnos());
		initLayers();
	}
	private List<String> getFilteredMasksets() {
		return Optional.ofNullable(this.lotFilter.getMasksets()).orElse(List.of()).isEmpty() ? this.masksets
			: this.lotFilter.getMasksets();
	}
	public void initLayers() {
		this.layers = this.lotEJB.getFilteredLayers(getFilteredClusters(), getFilteredTechnos(), getFilteredMasksets());
		initLotIds();
	}
	private List<String> getFilteredLayers() {
		return Optional.ofNullable(this.lotFilter.getLayers()).orElse(List.of()).isEmpty() ? this.layers
			: this.lotFilter.getLayers();
	}
	public void initLotIds() {
		this.lotIds = this.lotEJB.getFilteredLotIds(getFilteredClusters(), getFilteredTechnos(), getFilteredMasksets(),
			getFilteredLayers());
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

    @SuppressWarnings("static-method")
	public boolean stringFilter(final Object value, final Object filter, final Locale locale) {
    	return filter == null || filter.toString().isBlank() ||
    		value != null && value.toString().toLowerCase(locale).contains(filter.toString().toLowerCase(locale));
    }

}