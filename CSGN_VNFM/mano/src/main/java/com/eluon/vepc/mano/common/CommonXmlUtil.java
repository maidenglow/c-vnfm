package com.eluon.vepc.mano.common;

import java.util.LinkedHashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.JSONValue;

public class CommonXmlUtil {

	public static String getElementToString(Element rootElement){
		
		Document jdomDoc = new Document();
		jdomDoc.setRootElement(rootElement);
		
		XMLOutputter xml = new XMLOutputter();
        xml.setFormat(Format.getPrettyFormat());
        return xml.outputString(jdomDoc);
        
	}
	
	public static String getXmlToJson(String s){
		
		JSONObject soapDatainJsonObject = XML.toJSONObject(s);
		
        return soapDatainJsonObject.toString();
        
	}
	
	public static String getListMapToJson(List<LinkedHashMap<String, String>> resultList){

        return JSONValue.toJSONString(resultList);
        
	}
}
