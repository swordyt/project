package com.travelsky.autotest.autosky.elements;

import com.travelsky.autotest.autosky.browser.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row extends Element
{
  public Row(Container container, HashMap<String, Object> options)
  {
    super(container, options);
  }

  public List<Element> getCellList()
  {
    HashMap temp = new HashMap();
    temp.put("tagName", ".//th|.//td");
    temp.put("methodInfo", this.methodInfo + "getCellList()");
    return getElementList(temp);
  }

  public Map<String, Integer> getCellTextIndexMap()
  {
    Map cellTextIndexMap = new HashMap();
    List cellTextList = new ArrayList();
    HashMap temp = new HashMap();
    temp.put("tagName", ".//th|.//td");
    temp.put("methodInfo", this.methodInfo + ".getCellTextIndexMap()");
    cellTextList = getElementTextList(temp);
    for (int i = 0; i < cellTextList.size(); i++) {
      String text = (String)cellTextList.get(i);
      cellTextIndexMap.put(text, Integer.valueOf(i + 1));
    }
    return cellTextIndexMap;
  }

  public List<String> getCellTextList()
  {
    List cellTextList = new ArrayList();
    HashMap temp = new HashMap();
    temp.put("tagName", ".//th|.//td");
    temp.put("methodInfo", this.methodInfo + ".getCellTextList()");
    cellTextList = getElementTextList(temp);
    return cellTextList;
  }
}