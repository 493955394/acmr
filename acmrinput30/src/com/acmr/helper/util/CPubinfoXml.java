package com.acmr.helper.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 * XML
 *
 */
public class CPubinfoXml {

	public static Document getDocumentFromString(String xml1) {
		Document doc1 = null;
		try {
			doc1 = org.dom4j.DocumentHelper.parseText(xml1.trim());
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return doc1;
	}

	public static String getSubNodeValue(Element e1, String tag) {
		Element e2 = e1.element(tag);
		if (e2 != null) {
			return e2.getStringValue();
		}
		return "";
	}

	public static void setSubNodeValue(Element e1, String tag, String v) {
		Element e2 = e1.element(tag);
		if (e2 == null) {
			e2 = e1.addElement(tag);
		}
		e2.setText(v);
	}

	public static void AppendSubNodeValue(Element e1, String tag, String v) {
		Element e2 = e1.addElement(tag);
		e2.setText(v);
	}

	public static String getAttrValue(Element e1, String tag) {
		Attribute e2 = e1.attribute(tag);
		if (e2 != null) {
			return e2.getStringValue();
		}
		return "";
	}

	public static void setAttrValue(Element e1, String tag, String v) {
		Attribute e2 = e1.attribute(tag);
		if (e2 == null) {
			e2 = (Attribute) e1.addAttribute(tag, v);
		}
		e2.setValue(v);
	}
}
