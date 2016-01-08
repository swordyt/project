package com.travelsky.autotest.autosky.utils;

import com.travelsky.autotest.autosky.junit.modules.CaseInfo;
import com.travelsky.autotest.autosky.junit.modules.ModuleInfo;
import com.travelsky.autotest.autosky.junit.modules.SuiteInfo;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtil
{
  public static String GetResAttributeXML(String Attribute, String resultList)
    throws UnsupportedEncodingException, DocumentException
  {
    System.out.println(resultList);
    String res = "";
    try {
      Document doc = DocumentHelper.parseText(resultList);
      Element root = doc.getRootElement();
      res = getElementAttribute(root, Attribute);
    } catch (DocumentException e) {
      e.printStackTrace();
    }
    return res;
  }

  public static String getElementAttribute(Element elem, String Attribute)
  {
    String res = "";
    res = elem.attributeValue(Attribute);
    System.out.println(elem.getName());
    if ((res == null) || (res.equals(""))) {
      Iterator it = elem.elementIterator();
      while (it.hasNext()) {
        Element e = (Element)it.next();
        System.out.println(e.getName());
        res = getElementAttribute(e, Attribute);
        if ((res != null) && (!res.equals(""))) {
          break;
        }
      }
    }
    return res;
  }

  public static List<String> getAttributeValue(String infoXML, String attribute)
  {
    List listAttributeValue = new ArrayList();
    try {
      Document document = DocumentHelper.parseText(infoXML);
      Element root = document.getRootElement();
      listAttributeValue = getAttributeValue2(root, attribute);
    }
    catch (DocumentException e1) {
      e1.printStackTrace();
    }
    return listAttributeValue;
  }

  private static List<String> getAttributeValue2(Element element, String attribute)
  {
    List value = new ArrayList();
    value.add(element.attributeValue(attribute));
    System.out.println(element.getName());
    Iterator it;
    if ((value.get(value.size() - 1) == null) || (value == null) || ("null".equals(value.get(value.size() - 1)))) {
      for (it = element.elementIterator(); it.hasNext(); ) {
        Element ele = (Element)it.next();
        System.out.println(ele.getName());
        value = getAttributeValue2(ele, attribute);
        if ((value.get(value.size() - 1) != null) && (value != null) && (!"null".equals(value.get(value.size() - 1))));
      }
    }
    return value;
  }

  public static List<Map<String, String>> xmlDatas(String filePath) {
    List datas = new ArrayList();

    SAXReader reader = new SAXReader();
    try
    {
      Document doc = reader.read(filePath);
      Element root = doc.getRootElement();

      for (Iterator it = root.elementIterator(); it.hasNext(); ) {	
        Element element = (Element)it.next();

        Map data = new HashMap();
        for (Iterator itt = element.elementIterator(); itt.hasNext(); )
        {
          Element el = (Element)itt.next();
          data.put(el.getName(), el.getText());
        }
        datas.add(data);
      }
    }
    catch (DocumentException e)
    {
      Iterator it;
      e.printStackTrace();
    }
    return datas;
  }

  public static SuiteInfo parsePlanXml(String filePath)
    throws DocumentException
  {
    SuiteInfo suiteInfo = new SuiteInfo();

    SAXReader reader = new SAXReader();
    try
    {
      Document doc = reader.read(filePath);
      Element root = doc.getRootElement();
      suiteInfo.setSuiteName(root.attributeValue("name"));
      List moduleInfoList = new ArrayList();
      for (Iterator it = root.elementIterator(); it.hasNext(); ) {
        Element element = (Element)it.next();
        ModuleInfo moduleInfo = new ModuleInfo();
        moduleInfo.setModuleName(element.attributeValue("name"));
        moduleInfo.setModuleRun(Boolean.valueOf(element.attributeValue("run")));

        List caseInfoList = new ArrayList();
        for (Iterator itt = element.elementIterator(); itt.hasNext(); )
        {
          Element el = (Element)itt.next();
          CaseInfo caseInfo = new CaseInfo();
          caseInfo.setCaseName(el.attributeValue("name"));
          caseInfo.setCaseRun(Boolean.valueOf(el.attributeValue("run")));
          caseInfoList.add(caseInfo);
        }
        moduleInfo.setCaseInfoList(caseInfoList);
        moduleInfoList.add(moduleInfo);
      }
      suiteInfo.setModuleInfoList(moduleInfoList);
    } catch (DocumentException e) {
      throw new DocumentException(e);
    }
    return suiteInfo;
  }

  public static void main(String[] args) {
    List a = xmlDatas("D:/workspace/autosky/src/test/resources/plan.xml");
    System.out.println(a);
  }
}