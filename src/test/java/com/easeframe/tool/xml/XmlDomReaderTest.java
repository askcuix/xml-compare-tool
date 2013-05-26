package com.easeframe.tool.xml;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.easeframe.tool.parser.XmlDomReader;

public class XmlDomReaderTest {

	@Test
	public void testRead(){
		StringBuffer xml = new StringBuffer();
		xml.append("<product>");
		xml.append("<prodInfo>");
		xml.append("<name>unit trust</name>");
		xml.append("<country>CN</country>");
		xml.append("<price>");
		xml.append("<prcEffDt>2013-05-17</prcEffDt>");
		xml.append("<prcAmt>1.2</prcAmt>");
		xml.append("</price>");
		xml.append("<price>");
		xml.append("<prcEffDt>2013-05-18</prcEffDt>");
		xml.append("<prcAmt>1.3</prcAmt>");
		xml.append("</price>");
		xml.append("</prodInfo>");
		xml.append("<unitTrust>");
		xml.append("<fundType>FI</fundType>");
		xml.append("<fundSw>");
		xml.append("<fundSwCde>QDII</fundSwCde>");
		xml.append("</fundSw>");
		xml.append("</unitTrust>");
		xml.append("</product>");
//		xml.append("<prodInfo>");
//		xml.append("<name>unit trust</name>");
//		xml.append("<country>CN</country>");
//		xml.append("<price>");
//		xml.append("<prcEffDt>2013-05-17</prcEffDt>");
//		xml.append("<prcAmt>1.2</prcAmt>");
//		xml.append("</price>");
//		xml.append("<price>");
//		xml.append("<prcEffDt>2013-05-18</prcEffDt>");
//		xml.append("<prcAmt>1.3</prcAmt>");
//		xml.append("</price>");
//		xml.append("</prodInfo>");
		
		XmlDomReader reader = new XmlDomReader();
		Map<String, Object> xmlDataMap = reader.read(xml.toString());
		
		for(String key : xmlDataMap.keySet()){
			Object value = xmlDataMap.get(key);
			
			if(value instanceof String){
				System.out.println("Field: " + key + ", Value: " + value);
			}else if (value instanceof List){
				List<Map<String, String>> complextData = (List<Map<String, String>>)value;
				System.out.println("Field: " + key);
				System.out.println("Values: ");
				for(Map<String, String> complexValue : complextData){
					for(String complextField: complexValue.keySet()){
						System.out.println("Field: " + complextField + ", Value: " + complexValue.get(complextField));
					}
					System.out.println("-----------------------------");
				}
			}else{
				// not supported value
			}
		}
	}

}
