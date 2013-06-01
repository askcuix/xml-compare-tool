package com.easeframe.tool.comparator;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class XmlDataComparatorTest {

	@Test
	public void testRecOnlyExistinComparedFile() {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("srcKey", "<test></test>");

		XmlDataComparator comparator = new XmlDataComparator(dataMap);
		comparator.compare("destKey", "<test></test>");

		assertEquals(1, comparator.getDiffCount());
		assertTrue(comparator.getDiffFields().isEmpty());
	}

	@Test
	public void testAggregate() {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("srcKey1", "<test><value>1</value></test>");
		dataMap.put("srcKey2", "<test><value>2</value></test>");

		XmlDataComparator comparator = new XmlDataComparator(dataMap);
		comparator.aggregate();

		assertEquals(2, comparator.getDiffCount());
		assertTrue(dataMap.isEmpty());
		assertTrue(comparator.getDiffFields().isEmpty());
	}

	@Test
	public void testFieldOnlyExistinComparedFile() {
		String srcXml = "<User><Id>1</Id><Name>ADMIN</Name></User>";
		String destXml = "<User><Id>1</Id><Name>ADMIN</Name><Age>30</Age><Address><Country>CN</Country><City>GZ</City></Address></User>";

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("1", srcXml);

		XmlDataComparator comparator = new XmlDataComparator(dataMap);
		comparator.compare("1", destXml);

		assertEquals(1, comparator.getDiffCount());

		Set<String> diffFields = comparator.getDiffFields();
		assertEquals(2, diffFields.size());
		assertTrue(diffFields.contains("Age"));
		assertTrue(diffFields.contains("Address"));
	}

	@Test
	public void testFieldOnlyExistInSourceFile() {
		String srcXml = "<User><Id>1</Id><Name>ADMIN</Name><Age>30</Age><Address><Country>CN</Country><City>GZ</City></Address></User>";
		String destXml = "<User><Id>1</Id><Name>ADMIN</Name></User>";

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("1", srcXml);

		XmlDataComparator comparator = new XmlDataComparator(dataMap);
		comparator.compare("1", destXml);

		assertEquals(1, comparator.getDiffCount());

		Set<String> diffFields = comparator.getDiffFields();
		assertEquals(2, diffFields.size());
		assertTrue(diffFields.contains("Age"));
		assertTrue(diffFields.contains("Address"));
	}

	@Test
	public void testCompare() {
		String srcXml = "<User><Id>1</Id><Name>ADMIN</Name><Age>30</Age><Address><Country>CN</Country><City>GZ</City></Address><Address><Country>TW</Country><City>TW</City></Address></User>";
		String destXml = "<User><Id>1</Id><Name>ADMIN</Name><Age>31</Age><Address><Country>CN</Country><City>GZ</City></Address><Address><Country>SG</Country><City>SG</City></Address></User>";

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("1", srcXml);

		XmlDataComparator comparator = new XmlDataComparator(dataMap);
		comparator.compare("1", destXml);

		assertEquals(1, comparator.getDiffCount());

		Set<String> diffFields = comparator.getDiffFields();
		assertEquals(2, diffFields.size());
		assertTrue(diffFields.contains("Age"));
		assertTrue(diffFields.contains("Address"));
	}

}
