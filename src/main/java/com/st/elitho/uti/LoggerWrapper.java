package com.st.elitho.uti;

import org.slf4j.Logger;

public final class LoggerWrapper {

	private LoggerWrapper() {
		// Don't instantiate this class
	}

	public static String wrap(final String message) {
		return String.format("[%s|%s|%s] %s", AppContext.getSessionid(), AppContext.getUserName(),
			AppContext.getHostname(),message);
	}

	public static void info(final Logger logger, final String message) {
		if (logger.isInfoEnabled()) {
			logger.info(wrap(message));
		}
	}

	public static void warn(final Logger logger, final String message) {
		if (logger.isWarnEnabled()) {
			logger.warn(wrap(message));
		}
	}

	public static void error(final Logger logger, final String message) {
		if (logger.isErrorEnabled()) {
			logger.error(wrap(message));
		}
	}

	public static void debug(final Logger logger, final String message) {
		if (logger.isDebugEnabled()) {
			logger.info(wrap(message));
		}
	}

	public static void trace(final Logger logger, final String message) {
		if (logger.isTraceEnabled()) {
			logger.trace(wrap(message));
		}
	}

}
