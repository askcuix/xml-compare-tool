package com.easeframe.tool.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easeframe.tool.Constant;

public class PropertiesLoader {

	private static Logger logger = LoggerFactory
			.getLogger(PropertiesLoader.class);

	private static Properties properties = new Properties();

	public static Properties load() {
		if (!properties.isEmpty()) {
			return properties;
		}

		InputStream is = null;

		try {
			is = PropertiesLoader.class
					.getResourceAsStream(Constant.CONFIG_PATH);
			properties.load(is);
		} catch (IOException e) {
			logger.error("Could not load properties file!", e);
			throw new IllegalStateException(e);
		} finally {

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("Close InputStream error!", e);
				}
			}

		}

		return properties;
	}

	public static String getProperty(String key) {
		if (properties.isEmpty()) {
			logger.error("No availabled properties!");
			throw new IllegalStateException("No availabled properties!");
		}

		String property = properties.getProperty(key);
		if (StringUtils.isBlank(property)) {
			logger.error("Could not load property with key[{}].", key);
			return StringUtils.EMPTY;
		}

		return property;
	}

}
