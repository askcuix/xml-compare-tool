package com.easeframe.tool.parser;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.easeframe.tool.comparator.XmlDataComparator;

public class SliceXmlDataHanlderImplTest {

	@Test
	public void testParse() throws Exception {
		String path = SliceXmlDataHanlderImplTest.class.getResource("/")
				.getPath();
		String destFile = path + "data/compared.xml";

		XmlDataComparator comparator = new DummyXmlDataComparator(
				new HashMap<String, String>());

		SliceXmlDataHanlderImpl destXmlHandler = new SliceXmlDataHanlderImpl(
				comparator);
		destXmlHandler.processFile(destFile);

		assertEquals(1, destXmlHandler.getRecordCount());

	}

	class DummyXmlDataComparator extends XmlDataComparator {

		public DummyXmlDataComparator(Map<String, String> dataMap) {
			super(dataMap);
		}

		@Override
		public void compare(String key, String comparedXml) {
			System.out.println("Key: " + key + ", Value: " + comparedXml);
		}

		@Override
		public void aggregate() {
			// do nothing
		}

		@Override
		public Set<String> getDiffFields() {
			return new HashSet<String>();
		}

		@Override
		public int getDiffCount() {
			return 0;
		}

	}
}
