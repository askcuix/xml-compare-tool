package com.easeframe.tool.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easeframe.tool.Constant;
import com.easeframe.tool.util.PropertiesLoader;

public class XmlDomReader {
	private static Logger logger = LoggerFactory.getLogger(XmlDomReader.class);

	public Map<String, Object> read(String xml) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			logger.error("Parse XML content error.", e);
			throw new IllegalStateException("Parse XML content error.", e);
		}

		Map<String, Object> xmlDataMap = new HashMap<String, Object>();

		List<String> repeatTagList = null;

		String repeatTag = PropertiesLoader.getProperty(Constant.REPEAT_TAG);
		if (StringUtils.isBlank(repeatTag)) {
			repeatTagList = new ArrayList<String>();
		} else {
			repeatTagList = Arrays.asList(StringUtils.split(repeatTag,
					Constant.TAG_SEPARATOR));
		}

		Element root = doc.getRootElement();

		treeWalk(root, repeatTagList, xmlDataMap);

		return xmlDataMap;
	}

	@SuppressWarnings("rawtypes")
	private void treeWalk(Element root, List<String> repeatTagList,
			Map<String, Object> xmlDataMap) {
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();

			if (element.elements().size() > 0) {
				if (repeatTagList.contains(element.getName())) {
					childWalk(element, xmlDataMap);
				} else {
					treeWalk(element, repeatTagList, xmlDataMap);
				}

			} else {
				xmlDataMap.put(element.getName(), element.getText());
			}

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void childWalk(Element subElem, Map<String, Object> xmlDataMap) {
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
