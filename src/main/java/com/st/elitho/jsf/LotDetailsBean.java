package com.st.elitho.jsf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.ejb.LotDetailsEJB;
import com.st.elitho.ejb.LotEJB;
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
    private LotDTO lot;
    private List<StreamedContent> waferImages;
    @EJB
    private transient LotEJB lotEJB;
    @EJB
    private transient LotDetailsEJB lotDetailsEJB;

    @PostConstruct
    public void init() {

        final var context = FacesContext.getCurrentInstance();
        final var params  = context.getExternalContext().getRequestParameterMap();

        this.lotId = params.get("lotId");
        this.startDate = params.get("startDate");

        if (this.lotId != null && !this.lotId.isBlank() && this.startDate != null && !this.startDate.isBlank()) {
                this.lot = this.lotEJB.getLot(this.lotId, this.startDate);

        } else {
        	LoggerUtils.warn(log, String.format("Unable to load lot details for lot %s and start date %s",
        		this.lotId, this.startDate));
        }

    }

    public void updateImages() {

    	this.waferImages = new ArrayList<>();

        if (this.lot == null) {
        	LoggerUtils.warn(log, "No selected lot, no images to load");
            return;
        }

        for (var wafer = 1; wafer <= WAFER_NB; wafer++) {
            try {
                final var bytes = LotDetailsEJB.loadImage(this.lot, wafer);
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