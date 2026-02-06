package com.st.elitho.jsf;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
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
public class TopBean implements Serializable {

	private static final long serialVersionUID = 6059241280500903094L;
	private int activeTab;
	private String homeUrl;

	@PostConstruct
	public void init() {

	    final var params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	    final var tabParam = params.get("tab");

	    if (tabParam != null) {
	        try {
	        	this.activeTab = Integer.parseInt(tabParam);
	        } catch (final NumberFormatException e) {
	        	this.activeTab = 0;
	        	log.warn(e.getMessage());
	        }

	    }


	    this.homeUrl = System.getProperty("litho.home.url");

	}

}
