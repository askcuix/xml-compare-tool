package com.easeframe.tool.util;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.easeframe.tool.Constant;

import static org.junit.Assert.*;

public class PropertiesLoaderTest {

	@Test
	public void testGetProperty() {
		Properties props = PropertiesLoader.load();
		assertNotNull(props);
		assertFalse(props.isEmpty());

		String root_tag = PropertiesLoader.getProperty(Constant.ROOT_TAG);
		assertNotNull(root_tag);

		String value = PropertiesLoader.getProperty("notexist.prop");
		assertEquals(StringUtils.EMPTY, value);
	}
}
