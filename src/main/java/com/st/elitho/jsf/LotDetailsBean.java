package com.st.elitho.jsf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.ejb.LotDetailsEJB;
import com.st.elitho.ejb.LotException;
import com.st.elitho.uti.LoggerUtils;
import com.st.elitho.uti.LoggerWrapper;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
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
public class LotDetailsBean implements Serializable {

    private static final long serialVersionUID = -2413130791061025040L;
    private static final int WAFER_NB = 25;
	private String lotId;
    private String startDate;
    private LotDTO selectedLot;
    private LotDTO lot;
    private List<StreamedContent> waferImages;
    private String mode;
    private String scope;
    private List<Integer> row0;
    private List<Integer> row1;
    private List<Integer> row2;
    private List<Integer> row3;
    private List<Integer> row4;
    private boolean previousDisabled;
    private boolean nextDisabled;
    private boolean backDisabled;
    private final List<LotDTO> similarLots = new ArrayList<>();
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
        this.scope = LotDetailsEJB.SCOPE_DETECTION;
        initLots();

    }

    private void initLots() {

    	this.similarLots.clear();
    	this.similarLots.addAll(this.selectedLot.getLotId().isEmpty()
    		? new ArrayList<>() : this.lotDetailsEJB.getSimilarLots(this.selectedLot, this.mode));
    	back();

    }

    public boolean isPreviousDisabled() {
    	return this.previousDisabled;
    }

    public boolean isNextDisabled() {
    	return this.nextDisabled;
    }

    public boolean isBackDisabled() {
    	return this.backDisabled;
    }

    public void previous() {

    	if (this.lotIndex.intValue() > 0) {

    		this.lot = this.similarLots.get(this.lotIndex.decrementAndGet());
            this.previousDisabled = this.lotIndex.intValue() <= 0;
            this.nextDisabled = this.lotIndex.intValue() >= this.similarLots.size() - 1;

    	}

    }

    public void next() {

    	if (this.lotIndex.intValue() < this.similarLots.size() - 1) {

    		this.lot = this.similarLots.get(this.lotIndex.incrementAndGet());
            this.previousDisabled = this.lotIndex.intValue() <= 0;
            this.nextDisabled = this.lotIndex.intValue() >= this.similarLots.size() - 1;

    	}

    }

    public void back() {

    	this.lot = this.selectedLot;
        this.lotIndex.set(this.similarLots.indexOf(this.lot));
        this.previousDisabled = this.lotIndex.intValue() <= 0;
        this.nextDisabled = this.lotIndex.intValue() >= this.similarLots.size() - 1;
        this.backDisabled = this.lotIndex.intValue() < 0;

    }

    public String getIndexLabel() {
    	return String.format("%d of %d", this.lotIndex.get() + 1, this.similarLots.size());
    }

    public void modeChanged() {
    	initLots();
    }

    public void scopeChanged() {

    }

    public String getSelectedStartDate() {
    	return Optional.ofNullable(this.lot).orElse(LotDTO.NULL).getLotId().isEmpty()
    		? "" : this.lot.getFormattedStart();
    }

    public void updateImages() {

    	this.waferImages = new ArrayList<>();

        if (this.lot == null) {
        	LoggerUtils.warn(log, "No selected lot, no images to load");
            return;
        }

        for (var wafer = 1; wafer <= WAFER_NB; wafer++) {
            try {
                final var bytes = LotDetailsEJB.loadImage(this.lot, this.mode, this.scope, wafer);
                if (bytes != null && bytes.length > 0) {
                    final StreamedContent img = DefaultStreamedContent.builder()
                            .contentType("image/png")
                            .stream(() -> new ByteArrayInputStream(bytes))
                            .build();
                    this.waferImages.add(img);
                }
            } catch (LotException | IOException e) {
            	LoggerWrapper.warn(log, String.format("Cannot load image for wafer %s of lot %s: %s",
            		wafer, this.lot.getLotId(), e.getMessage()));
            }
        }
    }

    public List<StreamedContent> getWaferImages() {
        return this.waferImages;
    }

}