package com.easeframe.tool.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDataComparator {
	
	private static Logger reportLogger = LoggerFactory.getLogger("report");

	@SuppressWarnings("unchecked")
	public void compare(Map<String, Object> srcData,
			Map<String, Object> destData) {
		if (srcData == null || srcData.isEmpty() || destData == null
				|| destData.isEmpty()) {
			return;
		}

		for (String key : srcData.keySet()) {
			Object srcValue = srcData.get(key);
			Object destValue = destData.get(key);

			if (srcValue.getClass() != destValue.getClass()) {
				return;
			}

			if (srcValue instanceof String) {
				if (!StringUtils.equals((String) srcValue, (String) destValue)) {
					reportLogger.info("Field[" + key + "] not matched: ["
							+ srcValue + "] [" + destValue + "]");
				}
			} else if (srcValue instanceof List) {
				List<Map<String, String>> srcValList = (List<Map<String, String>>) srcValue;
				List<Map<String, String>> destValList = (List<Map<String, String>>) destValue;
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

				if (valuePair.size() != srcValList.size()) {
					reportLogger.info("Field[" + key + "] not matched: ");
					StringBuffer srcLog = new StringBuffer(
							"\t\tOnly exist in source file: ");
					for (int i = 0; i < srcValList.size(); i++) {
						if (!valuePair.containsKey(i)) {
							Map<String, String> conflictSrcMap = srcValList
									.get(i);
							srcLog.append("[");
							for (String srcKey : conflictSrcMap.keySet()) {
								srcLog.append("Field: " + srcKey + " Value: "
										+ conflictSrcMap.get(srcKey) + " ");
							}
							srcLog.append("]");
						}
					}
					reportLogger.info(srcLog.toString());
					StringBuffer destLog = new StringBuffer(
							"\t\tOnly exist in destination file: ");
					for (int i = 0; i < destValList.size(); i++) {
						if (!valuePair.containsValue(i)) {
							Map<String, String> conflictDestMap = destValList
									.get(i);
							destLog.append("[");
							for (String destKey : conflictDestMap.keySet()) {
								destLog.append("Field: " + destKey + " Value: "
										+ conflictDestMap.get(destKey) + " ");
							}
							destLog.append("]");
						}
					}
					reportLogger.info(destLog.toString());
				}
			} else {
				// not supported
			}
		}
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
