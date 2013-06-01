package com.easeframe.tool.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Full XML file parser.
 * 
 * @author Chris
 * 
 */
public class FullXmlDataHanlderImpl extends XmlDataHandler {

	private int recordCount = 0;

	private Map<String, String> xmlDataMap = new HashMap<String, String>();

	@Override
	protected void processElement(String key, String xml) {
		recordCount++;

		xmlDataMap.put(key, xml);

	}

	@Override
	protected void processDocument() {
		logger.info("Full XML parser no special action need for document end!");
	}

	@Override
	public int getRecordCount() {
		return recordCount;
	}

	/**
	 * Get XML data mapping.
	 * 
	 * Key is record key, Value is XML content.
	 * 
	 * @return data mapping
	 */
	public Map<String, String> getXmlData() {
		return xmlDataMap;
	}

}
