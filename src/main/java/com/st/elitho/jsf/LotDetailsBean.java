package com.st.elitho.jsf;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.ejb.LotDetailsEJB;
import com.st.elitho.ejb.LotException;
import com.st.elitho.uti.LoggerUtils;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
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
public class LotDetailsBean implements Serializable {

    private static final long serialVersionUID = -2413130791061025040L;
	private String lotId;
    private String startDate;
    private LotDTO selectedLot;
    private LotDTO lot;
    private String mode;
    private String scope;
    private List<Integer> row0;
    private List<Integer> row1;
    private List<Integer> row2;
    private List<Integer> row3;
    private List<Integer> row4;
    private boolean firstDisabled;
    private boolean previousDisabled;
    private boolean nextDisabled;
    private boolean lastDisabled;
    private final List<LotDTO> similarLots = new ArrayList<>();
    private final Map<String, Path> waferImagePaths = new HashMap<>();
    private AtomicInteger lotIndex = new AtomicInteger();
    @EJB
    private transient LotDetailsEJB lotDetailsEJB;

    @PostConstruct
    public void init() {

        final var context = FacesContext.getCurrentInstance();
        final var params  = context.getExternalContext().getRequestParameterMap();

        this.lotId = params.get("lotId");
        this.startDate = params.get("startDate");

        if (this.lotId != null && !this.lotId.isBlank() && this.startDate != null && !this.startDate.isBlank()) {
            this.selectedLot = this.lotDetailsEJB.getLot(this.lotId, this.startDate);
        } else {
        	this.selectedLot = LotDTO.NULL;
        	LoggerUtils.warn(log, String.format("Unable to load lot details for lot %s and start date %s",
        		this.lotId, this.startDate));
        }

        this.mode = LotDetailsEJB.MODE_LOT;
        this.scope = LotDetailsEJB.SCOPE_WAFER;
        initLots();
        updateImages();

    }

    private void initLots() {

    	this.similarLots.clear();
    	this.similarLots.addAll(this.selectedLot.getLotId().isEmpty()
    		? new ArrayList<>() : this.lotDetailsEJB.getSimilarLots(this.selectedLot, this.mode));
        this.lotIndex.set(this.similarLots.indexOf(this.selectedLot));
        updateNavigationState(true, this.lotIndex.intValue());

    }

    public boolean isPreviousDisabled() {
    	return this.previousDisabled;
    }

    public boolean isFirstDisabled() {
    	return this.firstDisabled;
    }

    public boolean isNextDisabled() {
    	return this.nextDisabled;
    }

    public boolean isLastDisabled() {
    	return this.lastDisabled;
    }

    private void updateNavigationState(final boolean condition, final int index) {

    	if (condition) {

	    	this.lotIndex.set(index);
	    	this.lot = this.similarLots.get(this.lotIndex.intValue());
	    	this.firstDisabled = this.lotIndex.intValue() <= 0;
	    	this.previousDisabled = this.lotIndex.intValue() <= 0;
	        this.nextDisabled = this.lotIndex.intValue() >= this.similarLots.size() - 1;
	        this.lastDisabled = this.lotIndex.intValue() >= this.similarLots.size() - 1;

    	}

    }

    public void first() {
    	updateNavigationState(this.lotIndex.intValue() > 0, 0);
    }

    public void previous() {
    	updateNavigationState(this.lotIndex.intValue() > 0, this.lotIndex.decrementAndGet());
    }

    public void next() {
    	updateNavigationState(this.lotIndex.intValue() < this.similarLots.size() - 1, this.lotIndex.incrementAndGet());
    }

    public void last() {
    	updateNavigationState(this.lotIndex.intValue() < this.similarLots.size() - 1, this.similarLots.size() - 1);
    }

    public String getIndexLabel() {
    	return String.format("%d of %d", this.lotIndex.get() + 1, this.similarLots.size());
    }

    public void modeChanged() {
    	initLots();
    }

    public void scopeChanged() {
    	updateImages();
    }

    public String getSelectedStartDate() {
    	return Optional.ofNullable(this.lot).orElse(LotDTO.NULL).getLotId().isEmpty()
    		? "" : this.lot.getFormattedStart();
    }

    public void updateImages() {

        if (Optional.ofNullable(this.lot).orElse(LotDTO.NULL).getLotId().isEmpty()) {
        	LoggerUtils.warn(log, "No valid lot");
            return;
        }
        if (!List.of(LotDetailsEJB.MODE_LOT, LotDetailsEJB.MODE_CLUSTER).contains(
        	Optional.ofNullable(this.mode).orElse(""))) {
        	LoggerUtils.warn(log, String.format("Mode value %s not valid", this.mode));
            return;
        }
        if (!List.of(LotDetailsEJB.SCOPE_WAFER, LotDetailsEJB.SCOPE_CHUCK).contains(
        	Optional.ofNullable(this.scope).orElse(""))) {
        	LoggerUtils.warn(log, String.format("Scope value %s not valid", this.scope));
        }
        try {
			this.lotDetailsEJB.loadWaferImages(this.lot, this.mode, this.scope);
		} catch (final LotException e) {
			LoggerUtils.warn(log, e.getMessage());
		}

    }

    public List<String> getWaferImageNames() {
        return new ArrayList<>(this.lotDetailsEJB.getWaferImageBytes().keySet());
    }

    public boolean isWaferValid(final String name) {
    	return this.lotDetailsEJB.isWaferValid(name);
    }


    public StreamedContent getWaferImage(final String name) {

        final var bytes = this.lotDetailsEJB.getWaferImageBytes().getOrDefault(name, new byte[0]);

        if (Optional.ofNullable(bytes).orElse(new byte[0]).length == 0) {
            return null;
        }

        return DefaultStreamedContent.builder()
            .name(name)
            .contentType("image/png")
            .contentLength((long) bytes.length)
            .stream(() -> new ByteArrayInputStream(bytes))
            .build();
    }

}