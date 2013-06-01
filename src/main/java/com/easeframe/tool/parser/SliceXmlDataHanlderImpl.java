package com.easeframe.tool.parser;

import com.easeframe.tool.comparator.XmlDataComparator;

/**
 * Slice XML file parser.
 * 
 * @author Chris
 * 
 */
public class SliceXmlDataHanlderImpl extends XmlDataHandler {

	private int recordCount = 0;

	private XmlDataComparator dataComparator = null;

	/**
	 * Constructor.
	 * 
	 * @param dataComparator
	 *            XML content comparator
	 */
	public SliceXmlDataHanlderImpl(XmlDataComparator dataComparator) {
		this.dataComparator = dataComparator;
	}

	@Override
	protected void processElement(String key, String xml) {
		recordCount++;

		dataComparator.compare(key, xml);
	}

	@Override
	protected void processDocument() {
		dataComparator.aggregate();
	}

	@Override
	public int getRecordCount() {
		return recordCount;
	}

}
