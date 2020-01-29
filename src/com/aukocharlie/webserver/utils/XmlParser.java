package com.aukocharlie.webserver.utils;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * XML file parser
 */
public class XmlParser {

    private Document document;

    public XmlParser(String filePath) throws DocumentException {
        SAXReader reader = new SAXReader();
        this.document = reader.read(new File(filePath));
        this.getIteratorByXPath("/src/com/aukocharlie/webserver");
    }

    public Element getRootElement() {
        return this.document.getRootElement();
    }

    public Iterator getIteratorByXPath(String XPathExpression) {
        List list = this.document.selectNodes(XPathExpression);
        return list.iterator();
    }

    public List<Node> getListByXPath(String XPathExpression) {
        return this.document.selectNodes(XPathExpression);
    }

}