
package com.travelsky.autotest.autosky.window;

import com.travelsky.autotest.autosky.autoit.AutoItX;
import com.travelsky.autotest.autosky.winelements.WinElement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WinLocator
{
  public WinContainer winContainer = null;
  public HashMap<String, Object> options;
  public WinElement element;
  private Boolean isWindow = Boolean.valueOf(false);

  public WinLocator(WinContainer winContainer, HashMap<String, Object> options)
  {
    this.winContainer = winContainer;
    this.options = ((HashMap)options.clone());
  }

  public WinLocator(HashMap<String, Object> options)
  {
    this.options = ((HashMap)options.clone());
  }

  public WinLocator(Boolean isWindow, HashMap<String, Object> options)
  {
    this.isWindow = isWindow;
    this.options = ((HashMap)options.clone());
  }

  public String locate()
  {
    String properties = toProperties(this.options);
    if (this.isWindow.booleanValue()) {
      this.isWindow = Boolean.valueOf(false);
      if (AutoItX.getInstance().winWait(properties, "", 10)) {
        AutoItX.getInstance().winActivate(properties);

        String hWnd = AutoItX.getInstance().winGetHandle(properties, "");
        if ((hWnd != null) && (hWnd.equals("0x00000000"))) {
          return null;
        }
        return hWnd;
      }

      return null;
    }
    if (this.winContainer != null) {
      String hWnd = AutoItX.getInstance().controlGetHandle(this.winContainer.hWndProperty, "", properties);
      if ((hWnd != null) && (hWnd.equals("0x00000000"))) {
        return null;
      }
      return hWnd;
    }

    return null;
  }

  private String toProperties(HashMap<String, Object> options)
  {
    String properties = "";
    Iterator iterator = options.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)iterator.next();
      String name = (String)entry.getKey();
      String value = entry.getValue().toString();
      properties = properties + name.toUpperCase() + ":" + value + ";";
    }
    if (!properties.equals("")) {
      if (properties.endsWith(";")) {
        properties = properties.substring(0, properties.length() - 1);
      }
      properties = "[" + properties + "]";
    }
    return properties;
  }
}