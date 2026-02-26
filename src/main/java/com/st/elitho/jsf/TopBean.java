package com.st.elitho.jsf;

import java.io.IOException;
import java.io.Serializable;

import com.st.elitho.uti.AppContext;
import com.st.elitho.uti.AppProperties;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
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
	private String userName;
	private String keycloakUrl;
	private String homeUrl;
	private String appLabel;
	private String appVersion;
	private String appEnv;

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

	    this.userName = AppContext.getUserName();
	    this.homeUrl = AppProperties.HOME_URL;
	    this.keycloakUrl = AppProperties.KEYCLOAK_URL;
	    this.appLabel = AppProperties.getAppLabel();
	    this.appVersion = AppProperties.getAppVersion();
	    this.appEnv = AppProperties.APP_ENV;

	}

	@SuppressWarnings("static-method")
	public void logout() throws IOException {
        final var ctx = FacesContext.getCurrentInstance();
        final var request =
                (HttpServletRequest) ctx.getExternalContext().getRequest();

        // Just redirect to servlet
        ctx.getExternalContext().redirect(request.getContextPath() + "/app-logout");
    }

}
