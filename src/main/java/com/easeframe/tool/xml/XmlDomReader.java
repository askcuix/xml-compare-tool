package com.easeframe.tool.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDomReader {
	private static Logger logger = LoggerFactory
			.getLogger(XmlDomReader.class);
	
	private Map<String, Object> xmlDataMap = new HashMap<String, Object>();

	public Map<String, Object> read(String xml) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			logger.error("Parse XML content error.", e);
			throw new IllegalStateException("Parse XML content error.", e);
		}
		Element root = doc.getRootElement();

		treeWalk(root);

		return xmlDataMap;
	}

	@SuppressWarnings("rawtypes")
	private void treeWalk(Element root) {
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();

			if (element.elements().size() > 0) {
				for (Iterator sub = element.elementIterator(); sub.hasNext();) {
					Element subElem = (Element) sub.next();

					if (subElem.elements().size() > 0) {
						childWalk(subElem);
					} else {
						xmlDataMap.put(subElem.getName(), subElem.getText());
					}

				}

			} else {
				xmlDataMap.put(element.getName(), element.getText());
			}

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void childWalk(Element subElem) {
		String subElemName = subElem.getName();

		List<Map<String, String>> complextData = null;
		if (xmlDataMap.containsKey(subElemName)) {
			complextData = (List<Map<String, String>>) xmlDataMap
					.get(subElemName);
		} else {
			complextData = new ArrayList<Map<String, String>>();
		}

		Map<String, String> complextDataMap = new HashMap<String, String>();
		for (Iterator child = subElem.elementIterator(); child.hasNext();) {
			Element childElem = (Element) child.next();
			complextDataMap.put(childElem.getName(), childElem.getText());
		}

		complextData.add(complextDataMap);
		xmlDataMap.put(subElemName, complextData);
	}

}
