package com.st.elitho.uti;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public final class AppProperties {

	public static final String APP_ENV = System.getProperty("litho.app.env");
	public static final String APP_NAME = "application.name";
	public static final String APP_VERSION = "application.version";
	public static final String APP_LABEL = "application.label";
	public static final String HOME_URL = System.getProperty("litho.home.url");
	public static final String KEYCLOAK_URL = System.getProperty("litho.keycloak.url");
	private static final String PROP_FILENAME = "/application.properties";
	private static final AppProperties INSTANCE = new AppProperties();
	private transient AbstractConfiguration appConfiguration;
	private static final Properties PROPERTIES = new Properties();

	static {

		if (PROPERTIES.isEmpty()) {

			try (var stream = AppProperties.class.getResourceAsStream(PROP_FILENAME)) {
				PROPERTIES.load(stream);
			} catch (final IOException e) {
				LoggerWrapper.error(log, String.format("Cannot load app properties (%s)", e.getMessage()));
			}
			System.getProperties().stringPropertyNames().forEach(key ->
				PROPERTIES.setProperty(key, System.getProperties().getProperty(key)));

		}

	}


	public static AppProperties getInstance() {
		return INSTANCE;
	}

	public static String getFileContent(final String name) {

		final var content = new StringBuilder();

		try {
			content.append(new String(Files.readAllBytes(Paths.get(name)), Charset.defaultCharset()));
		} catch (final IOException e) {
			LoggerWrapper.error(log, "Cannot read: " + name + " (message: " + e.getMessage() + ")");
		}

		return content.toString();

	}

	public static String getAppName() {
		return PROPERTIES.getProperty(APP_NAME, AppContext.UNKNOWN);
	}

	public static String getAppVersion() {
		return PROPERTIES.getProperty(APP_VERSION, AppContext.UNKNOWN);
	}

	public static String getAppLabel() {
		return PROPERTIES.getProperty(APP_LABEL, AppContext.UNKNOWN);
	}

	public AbstractConfiguration getAppConfiguration() {

		if (this.appConfiguration == null) {

			this.appConfiguration = new PropertiesConfiguration();
			PROPERTIES.entrySet().stream().forEach(entry ->
				this.appConfiguration.setProperty((String) entry.getKey(), (String) entry.getValue()));

		}

		return this.appConfiguration;

	}

}
