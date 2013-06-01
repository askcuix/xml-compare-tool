package com.easeframe.tool.parser;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class FullXmlDataHanlderImplTest {

	@Test
	public void testParse() throws Exception {
		String path = FullXmlDataHanlderImplTest.class.getResource("/").getPath();
		String sourceFile = path + "data/source.xml";

		FullXmlDataHanlderImpl srcXmlHandler = new FullXmlDataHanlderImpl();
		srcXmlHandler.processFile(sourceFile);

		assertEquals(1, srcXmlHandler.getRecordCount());
		assertNotNull(srcXmlHandler.getXmlData());

		Map<String, String> xmlData = srcXmlHandler.getXmlData();
		for (Map.Entry<String, String> entry : xmlData.entrySet()) {
			System.out.println("Key: " + entry.getKey() + ", Value: "
					+ entry.getValue());
		}
	}
}
