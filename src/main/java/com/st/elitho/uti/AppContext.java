package com.st.elitho.uti;

import java.util.Optional;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;

public final class AppContext {

	public static final String UNKNOWN = "???";

	private AppContext() {
		// Do not instantiate
	}

	public static Optional<HttpServletRequest> getFacesRequest() {
		return FacesContext.getCurrentInstance() == null
			|| FacesContext.getCurrentInstance().getExternalContext() == null ? Optional.empty()
				: Optional.ofNullable(
					(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
	}

	public static String getUserName() {
		return getFacesRequest().isPresent()
			&& Optional.ofNullable(getFacesRequest().get().getUserPrincipal()).isPresent()
			? getFacesRequest().get().getUserPrincipal().getName() : UNKNOWN;
	}

	public static String getSessionid() {
		return getFacesRequest().isPresent() ? getFacesRequest().get().getSession(true).getId() : UNKNOWN;
	}

	public static String getHostname() {

		final String hostname;

		if (getFacesRequest().isPresent()) {

			final Optional<String> rhost = Optional.ofNullable(getFacesRequest().get().getRemoteHost());
			final Optional<String> raddr = Optional.ofNullable(getFacesRequest().get().getRemoteAddr());

			hostname = rhost.isPresent() ? raddr.isPresent() ? raddr.get() : UNKNOWN : rhost.get();

		} else {

			hostname = UNKNOWN;

		}

		return hostname;

	}

}
