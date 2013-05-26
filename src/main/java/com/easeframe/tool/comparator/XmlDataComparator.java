package com.easeframe.tool.comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easeframe.tool.parser.XmlDomReader;

public class XmlDataComparator {

	private static Logger reportLogger = LoggerFactory.getLogger("report");

	private Map<String, String> dataMap = null;

	private int diffCnt = 0;

	private Set<String> diffFields = new HashSet<String>();

	private XmlDomReader xmlReader = new XmlDomReader();

	public XmlDataComparator(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}

	public void compare(String key, String comparedXml) {
		if (!dataMap.containsKey(key)) {
			reportLogger.info("Record [{}] only exist in compared file.", key);
			diffCnt++;
			return;
		}

		String srcXml = dataMap.get(key);
		Map<String, Object> srcFieldData = xmlReader.read(srcXml);
		Map<String, Object> destFieldData = xmlReader.read(comparedXml);

		List<String> result = compareFields(srcFieldData, destFieldData);

		if (result != null && !result.isEmpty()) {
			reportLogger.info("Record [{}]", key);
			reportLogger
					.info("------------------------------------------------------------------------------");
			for (String report : result) {
				reportLogger.info(report);
			}
			reportLogger
					.info("------------------------------------------------------------------------------");
			diffCnt++;
		}

		dataMap.remove(key);
	}

	public void aggregate() {
		if (dataMap.isEmpty()) {
			return;
		}

		for (Iterator<Map.Entry<String, String>> it = dataMap.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			reportLogger.info("Record [{}] only exist in source file.",
					entry.getKey());
			diffCnt++;
			it.remove();
		}
	}

	public Set<String> getDiffFields() {
		return diffFields;
	}

	public int getDiffCount() {
		return diffCnt;
	}

	@SuppressWarnings("unchecked")
	public List<String> compareFields(Map<String, Object> srcData,
			Map<String, Object> destData) {
		if (srcData == null || srcData.isEmpty()) {
			throw new IllegalArgumentException(
					"Source XML Field Data is required!");
		}

		if (destData == null || destData.isEmpty()) {
			throw new IllegalArgumentException(
					"Compared XML Field Data is required!");
		}

		List<String> report = new ArrayList<String>();

		Set<String> fieldSet = new HashSet<String>();
		fieldSet.addAll(srcData.keySet());
		fieldSet.addAll(destData.keySet());

		for (String key : fieldSet) {
			Object srcValue = srcData.get(key);
			Object destValue = destData.get(key);

			if (srcValue == null && destValue != null) {
				if (destValue instanceof String) {
					diffFields.add(key);

					report.add("Field[" + key + "] not matched: [" + srcValue
							+ "] [" + destValue + "]");
				} else if (destValue instanceof List) {
					diffFields.add(key);

					StringBuffer listLog = new StringBuffer("Field[" + key
							+ "] only exist in compared file: ");
					listLog.append(buildListDiffLog((List<Map<String, String>>) destValue));
					report.add(listLog.toString());
				}

				continue;
			}

			if (srcValue != null && destValue == null) {
				if (srcValue instanceof String) {
					diffFields.add(key);

					report.add("Field[" + key + "] not matched: [" + srcValue
							+ "] [" + destValue + "]");
				} else if (srcValue instanceof List) {
					diffFields.add(key);

					StringBuffer listLog = new StringBuffer("Field[" + key
							+ "] only exist in source file: ");
					listLog.append(buildListDiffLog((List<Map<String, String>>) srcValue));
					report.add(listLog.toString());
				}

				continue;
			}

			if (srcValue.getClass() != destValue.getClass()) {
				throw new IllegalStateException("Field[" + key
						+ "] value type not matched: [" + srcValue.getClass()
						+ "] [" + destValue.getClass() + "]");
			}

			if (srcValue instanceof String) {
				if (!StringUtils.equals((String) srcValue, (String) destValue)) {
					diffFields.add(key);

					report.add("Field[" + key + "] not matched: [" + srcValue
							+ "] [" + destValue + "]");
				}
			} else if (srcValue instanceof List) {
				List<Map<String, String>> srcValList = (List<Map<String, String>>) srcValue;
				List<Map<String, String>> destValList = (List<Map<String, String>>) destValue;

				String diffLog = compareList(key, srcValList, destValList);

				if (StringUtils.isNotBlank(diffLog)) {
					diffFields.add(key);

					report.add(diffLog);
				}
			} else {
				// not supported
			}
		}

		fieldSet.clear();

		return report;
	}

	private String compareList(String key,
			List<Map<String, String>> srcValList,
			List<Map<String, String>> destValList) {
		Map<Integer, Integer> valuePair = new HashMap<Integer, Integer>();

		for (int i = 0; i < srcValList.size(); i++) {
			Map<String, String> srcMap = srcValList.get(i);
			for (int j = 0; j < destValList.size(); j++) {
				Map<String, String> destMap = destValList.get(j);
				if (compareMap(srcMap, destMap)) {
					valuePair.put(i, j);
					break;
				} else {
					continue;
				}
			}
		}

		if (valuePair.size() != srcValList.size()
				|| valuePair.size() != destValList.size()) {
			StringBuffer logBuffer = new StringBuffer("Field[" + key
					+ "] not matched: ");
			logBuffer.append("\t\tOnly exist in source file: ");
			for (int i = 0; i < srcValList.size(); i++) {
				if (!valuePair.containsKey(i)) {
					Map<String, String> conflictSrcMap = srcValList.get(i);
					logBuffer.append("[");
					for (String srcKey : conflictSrcMap.keySet()) {
						logBuffer.append("Field: " + srcKey + " Value: "
								+ conflictSrcMap.get(srcKey) + " ");
					}
					logBuffer.append("]");
				}
			}

			logBuffer.append("\t\tOnly exist in compared file: ");
			for (int i = 0; i < destValList.size(); i++) {
				if (!valuePair.containsValue(i)) {
					Map<String, String> conflictDestMap = destValList.get(i);
					logBuffer.append("[");
					for (String destKey : conflictDestMap.keySet()) {
						logBuffer.append("Field: " + destKey + " Value: "
								+ conflictDestMap.get(destKey) + " ");
					}
					logBuffer.append("]");
				}
			}
			return logBuffer.toString();
		}
		return null;
	}

	private String buildListDiffLog(List<Map<String, String>> valueList) {
		StringBuffer log = new StringBuffer();

		for (Map<String, String> value : valueList) {
			log.append("[");
			for (String key : value.keySet()) {
				log.append("Field: " + key + " Value: " + value.get(key) + " ");
			}
			log.append("]");
		}

		return log.toString();
	}

	private boolean compareMap(Map<String, String> srcMap,
			Map<String, String> destMap) {
		for (String key : srcMap.keySet()) {
			if (StringUtils.equals(srcMap.get(key), destMap.get(key))) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

}
