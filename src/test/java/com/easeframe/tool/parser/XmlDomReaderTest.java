package com.easeframe.tool.parser;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

import com.easeframe.tool.parser.XmlDomReader;

public class XmlDomReaderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testRead() {
		String xml = "<User><Id>1</Id><Name>ADMIN</Name><Age>30</Age><Address><Country>CN</Country><City>GZ</City></Address><Address><Country>TW</Country><City>TW</City></Address></User>";

		XmlDomReader reader = new XmlDomReader();
		Map<String, Object> xmlData = reader.read(xml);

		assertEquals(4, xmlData.size());
		assertEquals("1", xmlData.get("Id"));
		assertEquals("ADMIN", xmlData.get("Name"));
		assertEquals("30", xmlData.get("Age"));

		List<Map<String, String>> addresses = (List<Map<String, String>>) xmlData
				.get("Address");
		assertEquals(2, addresses.size());
	}

}
