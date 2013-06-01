package com.easeframe.tool.main;

import org.junit.Test;

public class CompareToolTest {

	@Test
	public void testCompare() throws Exception {
		String path = CompareToolTest.class.getResource("/").getPath();
		String sourceFile = path + "data/source.xml";
		String destFile = path + "data/compared.xml";

		CompareTool.main(new String[] { sourceFile, destFile });
	}

}
