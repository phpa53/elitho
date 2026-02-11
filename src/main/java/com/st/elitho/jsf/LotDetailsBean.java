package com.st.elitho.jsf;

import java.io.Serializable;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.ejb.LotEJB;
import com.st.elitho.uti.LoggerUtils;

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
	private String lotId;
    private String startDate;
    private LotDTO lot;

    @EJB
    private transient LotEJB lotEJB;   // adjust type to your EJB/service

    @PostConstruct
    public void init() {

        final var context = FacesContext.getCurrentInstance();
        final var params  = context.getExternalContext().getRequestParameterMap();

        this.lotId = params.get("lotId");
        this.startDate = params.get("startDate");

        System.out.println("---------> "+this.lotId+" " +this.startDate);

        if (this.lotId != null && !this.lotId.isBlank() && this.startDate != null && !this.startDate.isBlank()) {

                this.lot = this.lotEJB.getLot(this.lotId, this.startDate);

        } else {

        	LoggerUtils.warn(log, String.format("Unable to load lot details for lot %s and start date %s",
        		this.lotId, this.startDate));

        }

    }
}