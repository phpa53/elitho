package com.st.elitho.ejb;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.dto.LotDateDTO;
import com.st.elitho.dto.LotFilterDTO;

import jakarta.ejb.DependsOn;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@DependsOn(value = "LotRefreshEJB")
@NoArgsConstructor
@Stateless
@Slf4j
public class LotEJB implements Serializable {

	private static final long serialVersionUID = -1296719365992610292L;
	@EJB
	private transient LotRefreshEJB lotRefreshEJB;

	public List<LotDTO> getLots(final LotFilterDTO filter) throws LotException {

		if (filter == null) {
			throw new LotException("Null filter");
		}

		final var lots = this.lotRefreshEJB.getAllLots().stream().filter(lot -> lot.matches(filter)).toList();

		log.info(String.format("Found %d lots", lots.size()));

		return lots;

	}

	public LotDTO getLot(final String lotId, final String startDate) {
		return this.lotRefreshEJB.getLot(lotId, startDate);
	}

	public List<String> getToolsFromDates(final LocalDate fromDate, final LocalDate toDate) throws LotException {

		if (fromDate == null) {
			throw new LotException("No start date");
		}
		if (toDate == null) {
			throw new LotException("No end date");
		}

		return this.lotRefreshEJB.getAllLots().stream().map(LotDTO::getCluster).distinct().sorted().toList();

	}

	public List<String> getFilteredTechnos(final List<String> clusters) {
		return this.lotRefreshEJB.getAllLots().stream()
			.filter(lot -> clusters.isEmpty() || clusters.contains(lot.getCluster()))
			.map(LotDTO::getTechno).distinct().sorted().toList();
	}

	public List<String> getFilteredMasksets(final List<String> clusters, final List<String> technos) {
		return this.lotRefreshEJB.getAllLots().stream()
			.filter(lot -> clusters.isEmpty() || clusters.contains(lot.getCluster()))
			.filter(lot -> technos.isEmpty() || technos.contains(lot.getTechno()))
			.map(LotDTO::getMaskset).distinct().sorted().toList();
	}

	public List<String> getFilteredLayers(final List<String> clusters, final List<String> technos,
		final List<String> masksets) {
		return this.lotRefreshEJB.getAllLots().stream()
			.filter(lot -> clusters.isEmpty() || clusters.contains(lot.getCluster()))
			.filter(lot -> technos.isEmpty() || technos.contains(lot.getTechno()))
			.filter(lot -> masksets.isEmpty() || masksets.contains(lot.getMaskset()))
			.map(LotDTO::getLayer).distinct().sorted().toList();
	}

	public List<String> getFilteredLotIds(final List<String> clusters, final List<String> technos,
		final List<String> masksets, final List<String> layers) {
		return this.lotRefreshEJB.getAllLots().stream()
			.filter(lot -> clusters.isEmpty() || clusters.contains(lot.getCluster()))
			.filter(lot -> technos.isEmpty() || technos.contains(lot.getTechno()))
			.filter(lot -> masksets.isEmpty() || masksets.contains(lot.getMaskset()))
			.filter(lot -> layers.isEmpty() || layers.contains(lot.getLayer()))
			.map(LotDTO::getLotId).distinct().sorted().toList();
	}

	public List<String> getMatchedList(final List<String> values, final Function<LotDTO, String> matchingValue,
		final Function<LotDTO, String> fieldExtractor) {
	    return this.lotRefreshEJB.getAllLots().stream()
	        .filter(lot -> LotDTO.matches(values, matchingValue.apply(lot)))
	        .map(fieldExtractor).distinct().sorted().toList();
	}

	public List<String> getAttributeList(final LotDateDTO lotDate, final Function<LotDTO, String> fieldExtractor) {
	    return this.lotRefreshEJB.getAllLots().stream()
	    	.filter(lot -> lot.getStart() != null
	    		&& lot.getStart().toLocalDate().isAfter(lotDate.getFromDate())
	    		&& lot.getStart().toLocalDate().isBefore(lotDate.getToDate()))
	    	.map(fieldExtractor).distinct().sorted().toList();
	}

	public List<LocalDateTime> getLotStarts(final String lotId) {
		return this.lotRefreshEJB.getAllLots().stream().filter(lot -> lot.getLotId().equals(lotId))
			.map(LotDTO::getStart).toList();
	}

}
