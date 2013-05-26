package com.easeframe.tool.main;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easeframe.tool.comparator.XmlDataComparator;
import com.easeframe.tool.parser.FullXmlDataHanlderImpl;
import com.easeframe.tool.parser.SliceXmlDataHanlderImpl;
import com.easeframe.tool.util.PropertiesLoader;

public class CompareTool {
	private static Logger reportLogger = LoggerFactory.getLogger("report");

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args == null || args.length != 2) {
			throw new IllegalArgumentException("Missing required parameters!");
		}

		String sourceFile = args[0];
		String destFile = args[1];

		long start = System.currentTimeMillis();

		PropertiesLoader.load();

		reportLogger
				.info(">>>>>> Start to compare XML file - Source File[{}], Compared File[{}] <<<<<<",
						sourceFile, destFile);

		FullXmlDataHanlderImpl srcXmlHandler = new FullXmlDataHanlderImpl();
		srcXmlHandler.processFile(sourceFile);

		XmlDataComparator comparator = new XmlDataComparator(
				srcXmlHandler.getXmlData());

		SliceXmlDataHanlderImpl destXmlHandler = new SliceXmlDataHanlderImpl(
				comparator);
		destXmlHandler.processFile(destFile);

		long end = System.currentTimeMillis();
		reportLogger.info("Total record count in source file:    "
				+ srcXmlHandler.getRecordCount());
		reportLogger.info("Total record count in compared file:  "
				+ destXmlHandler.getRecordCount());
		reportLogger.info("Different record count:               "
				+ comparator.getDiffCount());
		reportLogger.info("Different fields:                     "
				+ StringUtils.join(comparator.getDiffFields(), ", "));
		reportLogger.info("Total processing time:                "
				+ (end - start));
		reportLogger.info(">>>>>> Finish to compare XML files. <<<<<<");
	}

}
