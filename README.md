xml-compare-tool
================

The tools used for compare XML files, it's simple and fast, that maybe not suit to all scenario, the purpose of this tool is just for quicky find out difference data from 2 large XML files.

In our scenario, I need compare 2 XML files which generated from program, that maybe large than 100M, it's hard to open in desktop software. Meanwhile, the data position is randomly in XML, the plain text compare software(e.g. Beyond Compare) can not well display the difference. This tool can help me solve those problems.

##Usage

1. Configure your XML element info in config.properties.   
   **root.tag**   
   *Mandatory*. The root element of XML record.
   E.g. if your XML content like below, then root element is *Product*.   
   \<ProductList>   
      \<Product>   
          \<Id>1\</Id>  
          \<Price>   
             \<PriceData>2013-05-30\</PriceData>  
             \<PriceAmount>15\</PriceAmount>  
          \</Price>  
          \<Price>  
             \<PriceData>2013-05-31\</PriceData>   
             \<PriceAmount>20\</PriceAmount>   
          \</Price>   
          ……   
      \</Product>    
      \<Product>   
          \<Id>2\</Id>   
      \</Product>     
      ……   
   \</ProductList>   
   
   **id.tag**  
   *Mandatory*. The id element of XML record which can be used for identify record. That can be composite key which composed by multiple element, use comma(,) to separate. From above example, that's *Id*.
   
   **repeat.tag**   
   *Optional*. The repeatable element of XML record. Each list of values will treat as one field, such as *Price* from above.
   
2. Build your project, the xml-compare-tool-\<version>-jar-with-dependencies.jar will generated.   
   *mvn clean install*

3. Copy xml-compare-tool-\<version>-jar-with-dependencies.jar to your folder, e.g. */User/Chris/xmlCompare*.

4. Go into your folder, and run command like below. The file path should be absolute path, you can adjust maximum memory size according to your file size.  
   *java -Xmx1024m -jar xml-compare-tool-\<version>-jar-with-dependencies.jar <source file> <compared file>*

5. Check report log *logs/report.log* to find out data difference, also include below statistics info.  
   \- Total record count in source file  
   \- Total record count in compared file  
   \- Different record count  
   \- Different field list  
   \- Processing time  

