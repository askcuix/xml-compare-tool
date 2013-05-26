package com.easeframe.tool.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.easeframe.tool.Constant;
import com.easeframe.tool.util.PropertiesLoader;

public abstract class XmlDataHandler extends DefaultHandler {

	protected static Logger logger = LoggerFactory
			.getLogger(XmlDataHandler.class);

	private String rootElem;

	private String filePath;

	private StringBuffer xmlRecord = new StringBuffer();

	private StringBuffer elemValue = new StringBuffer();

	private String recordTag = null;

	private Map<String, String> recordKey = new LinkedHashMap<String, String>();

	public void processFile(String filepath) throws Exception {
		this.filePath = filepath;

		this.rootElem = PropertiesLoader.getProperty(Constant.ROOT_TAG);
		String idTag = PropertiesLoader.getProperty(Constant.ID_TAG);

		if (StringUtils.isBlank(rootElem)) {
			throw new IllegalArgumentException("Configure[" + Constant.ROOT_TAG
					+ "] is required!");
		}

		if (StringUtils.isBlank(idTag)) {
			throw new IllegalArgumentException("Configure[" + Constant.ID_TAG
					+ "]is required!");
		}

		for (String key : StringUtils.split(idTag, Constant.TAG_SEPARATOR)) {
			recordKey.put(key, null);
		}

		InputStreamReader inputReader = null;
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(this);
			xmlReader.setErrorHandler(this);
			inputReader = new InputStreamReader(new FileInputStream(filepath),
					"UTF-8");
			xmlReader.parse(new InputSource(inputReader));
		} catch (Exception e) {
			logger.error("Parse file [" + filepath + "] error.", e);
		} finally {
			if (inputReader != null) {
				try {
					inputReader.close();
				} catch (IOException e) {
					logger.error("Close InputStreamReader error.", e);
				}
			}
		}
	}

	protected abstract void processElement(String key, String xml);

	protected abstract void processDocument();

	public abstract int getRecordCount();

	@Override
	public void startDocument() throws SAXException {
		logger.info("Document parser start: {}", filePath);

		logger.debug("****** startDocument ******");
	}

	@Override
	public void endDocument() throws SAXException {
		logger.debug("****** endDocument ******");

		processDocument();

		logger.info("Document parser end: {}", filePath);
	}

	@Override
	public void processingInstruction(String target, String instruction)
			throws SAXException {
		logger.debug("processingInstruction - target:{}, instruction:{}",
				target, instruction);
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) {
		logger.debug("startPrefixMapping - prefix:{}, URI:{}", prefix, uri);
	}

	@Override
	public void endPrefixMapping(String prefix) {
		logger.debug("endPrefixMapping - prefix:{}", prefix);
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String fullName, Attributes attributes) throws SAXException {
		try {
			logger.debug(
					"startElement - Element:{}, namespaceURI:{}, localName:{}",
					fullName, namespaceURI, localName);

			if (StringUtils.equals(localName, rootElem)) {
				recordTag = localName;
			}

			if (recordTag != null) {
				xmlRecord.append("<").append(localName);
				for (int i = 0; i < attributes.getLength(); i++) {
					xmlRecord.append(" ").append(attributes.getQName(i))
							.append("=\"").append(attributes.getValue(i))
							.append("\"");
				}
				xmlRecord.append(">");
			}
		} catch (Exception e) {
			logger.error("startElement encounter error.", e);
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName,
			String fullName) throws SAXException {
		logger.debug("endElement - Element:{}, namespaceURI:{}, localName:{}",
				fullName, namespaceURI, localName);

		if (recordTag != null) {
			String value = StringUtils.strip(elemValue.toString());
			value = StringEscapeUtils.escapeXml(value);
			xmlRecord.append(value);

			if (recordKey.containsKey(localName)) {
				recordKey.put(localName, value);
			}

			elemValue = new StringBuffer();
			xmlRecord.append("</").append(localName).append(">");
		}

		if (StringUtils.equals(recordTag, localName)) {
			logger.debug("Xml Record: [{}]", xmlRecord);

			String xmlKey = StringUtils.join(recordKey.values(), "#");
			processElement(xmlKey, xmlRecord.toString());

			xmlRecord = new StringBuffer();
			recordTag = null;
		}
	}

	@Override
	public void characters(char[] chars, int start, int length)
			throws SAXException {
		logger.debug("Characters:[{}]", new String(chars, start, length));

		if (recordTag != null) {
			String value = new String(chars, start, length);
			elemValue.append(value);
		}
	}

	@Override
	public void ignorableWhitespace(char[] chars, int start, int length)
			throws SAXException {
		logger.debug("ignorableWhitespace calling");
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		logger.debug("skippedEntity calling");
	}

	@Override
	public void warning(SAXParseException exception) {
		logger.warn("WARNING: \tRow:\t{}\tColumn:\t{}\tError Message:\t{}",
				exception.getLineNumber(), exception.getColumnNumber(),
				exception.getMessage());
	}

	@Override
	public void error(SAXParseException exception) {
		logger.error("ERROR: \tRow:\t{}\tColumn:\t{}\tError Message:\t{}",
				exception.getLineNumber(), exception.getColumnNumber(),
				exception.getMessage());
	}

}
