package com.easeframe.tool.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class XmlDataComparatorTest {

	@Test
	public void testCompare() {
		Map<String, Object> srcData = new HashMap<String, Object>();
		srcData.put("name", "unit trust");
		srcData.put("country", "CN");

		List<Map<String, String>> prcSrcData = new ArrayList<Map<String, String>>();
		Map<String, String> prcSrcMap1 = new HashMap<String, String>();
		prcSrcMap1.put("prcEffDt", "2013-05-17");
		prcSrcMap1.put("prcAmt", "1.2");
		prcSrcData.add(prcSrcMap1);
		Map<String, String> prcSrcMap2 = new HashMap<String, String>();
		prcSrcMap2.put("prcEffDt", "2013-05-18");
		prcSrcMap2.put("prcAmt", "1.3");
		prcSrcData.add(prcSrcMap2);
		srcData.put("price", prcSrcData);

		List<Map<String, String>> utSwSrcData = new ArrayList<Map<String, String>>();
		Map<String, String> utSwSrcMap = new HashMap<String, String>();
		utSwSrcMap.put("fundType", "FI");
		utSwSrcData.add(utSwSrcMap);
		srcData.put("fundSw", utSwSrcData);

		Map<String, Object> destData = new HashMap<String, Object>();
		destData.put("name", "unit trust");
		destData.put("country", "HK");

		List<Map<String, String>> prcDestData = new ArrayList<Map<String, String>>();
		Map<String, String> prcDestMap1 = new HashMap<String, String>();
		prcDestMap1.put("prcEffDt", "2013-05-17");
		prcDestMap1.put("prcAmt", "1.2");
		prcDestData.add(prcDestMap1);
		Map<String, String> prcDestMap2 = new HashMap<String, String>();
		prcDestMap2.put("prcEffDt", "2013-05-19");
		prcDestMap2.put("prcAmt", "1.3");
		prcDestData.add(prcDestMap2);
		destData.put("price", prcDestData);

		List<Map<String, String>> utSwDestData = new ArrayList<Map<String, String>>();
		Map<String, String> utSwDestMap = new HashMap<String, String>();
		utSwDestMap.put("fundType", "FI");
		utSwDestData.add(utSwDestMap);
		destData.put("fundSw", utSwDestData);

		XmlDataComparator comparator = new XmlDataComparator();
		comparator.compare(srcData, destData);
	}

}
