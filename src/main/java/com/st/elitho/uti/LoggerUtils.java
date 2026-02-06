package com.st.elitho.uti;

import org.slf4j.Logger;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public final class LoggerUtils {

	private LoggerUtils() {
		//Do not instantiate
	}

	public static void info(final Logger logger, final String message) {

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
			message, ""));
		LoggerWrapper.info(logger, message);

	}

	public static void warn(final Logger logger, final String message) {

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
			message, ""));
		LoggerWrapper.warn(logger, message);

	}

	public static void error(final Logger logger, final String message) {

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
			message, ""));
		LoggerWrapper.warn(logger, message);

	}

}
