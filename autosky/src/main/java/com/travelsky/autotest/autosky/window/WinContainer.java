package com.travelsky.autotest.autosky.window;

import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.winelements.WinButton;
import com.travelsky.autotest.autosky.winelements.WinCheckbox;
import com.travelsky.autotest.autosky.winelements.WinComboBox;
import com.travelsky.autotest.autosky.winelements.WinEdit;
import com.travelsky.autotest.autosky.winelements.WinElement;
import com.travelsky.autotest.autosky.winelements.WinListView;
import com.travelsky.autotest.autosky.winelements.WinRadio;
import com.travelsky.autotest.autosky.winelements.WinTabControl;
import com.travelsky.autotest.autosky.winelements.WinToolBar;
import com.travelsky.autotest.autosky.winelements.WinTreeView;
import java.util.HashMap;
import java.util.regex.Pattern;

public class WinContainer
{
  public WinContainer winContainer;
  public String methodInfo;
  public String windowInfo;
  public String errorInfo;
  public String currentTime;
  public String hWnd = null;
  public String hWndProperty = null;

  public String locate() {
    return null;
  }

  public WinElement winElement()
  {
    WinElement winElement = new WinElement(this, toHash("index=>1"));
    return winElement;
  }

  public WinElement winElement(String options)
  {
    WinElement winElement = new WinElement(this, toHash(options));
    return winElement;
  }

  public WinButton winButton()
  {
    HashMap selectors = new HashMap();
    selectors.put("class", "Button");
    WinButton winButton = new WinButton(this, selectors);
    return winButton;
  }

  public WinButton winButton(String options)
  {
    HashMap selectors = toHash(options);
    if (selectors.get("class") == null) {
      selectors.put("class", "Button");
    }
    WinButton winButton = new WinButton(this, selectors);
    return winButton;
  }

  public WinEdit winEdit()
  {
    HashMap selectors = new HashMap();
    selectors.put("class", "Edit");
    WinEdit winEdit = new WinEdit(this, selectors);
    return winEdit;
  }

  public WinEdit winEdit(String options)
  {
    HashMap selectors = toHash(options);
    if (selectors.get("class") == null) {
      selectors.put("class", "Edit");
    }
    WinEdit winEdit = new WinEdit(this, selectors);
    return winEdit;
  }

  public WinCheckbox winCheckbox()
  {
    HashMap selectors = new HashMap();
    selectors.put("class", "Button");
    WinCheckbox winCheckbox = new WinCheckbox(this, selectors);
    return winCheckbox;
  }

  public WinCheckbox winCheckbox(String options)
  {
    HashMap selectors = toHash(options);
    if (selectors.get("class") == null) {
      selectors.put("class", "Button");
    }
    WinCheckbox winCheckbox = new WinCheckbox(this, selectors);
    return winCheckbox;
  }

  public WinRadio winRadio()
  {
    HashMap selectors = new HashMap();
    selectors.put("class", "Button");
    WinRadio winRadio = new WinRadio(this, selectors);
    return winRadio;
  }

  public WinRadio winRadio(String options)
  {
    HashMap selectors = toHash(options);
    if (selectors.get("class") == null) {
      selectors.put("class", "Button");
    }
    WinRadio winRadio = new WinRadio(this, selectors);
    return winRadio;
  }

  public WinComboBox winComboBox()
  {
    HashMap selectors = new HashMap();
    selectors.put("class", "ComboBox");
    WinComboBox winComboBox = new WinComboBox(this, selectors);
    return winComboBox;
  }

  public WinComboBox winComboBox(String options)
  {
    HashMap selectors = toHash(options);
    if (selectors.get("class") == null) {
      selectors.put("class", "ComboBox");
    }
    WinComboBox winComboBox = new WinComboBox(this, selectors);
    return winComboBox;
  }

  public WinListView winListView()
  {
    HashMap selectors = new HashMap();
    selectors.put("class", "SysListView32");
    WinListView winListView = new WinListView(this, selectors);
    return winListView;
  }

  public WinListView winListView(String options)
  {
    HashMap selectors = toHash(options);
    if (selectors.get("class") == null) {
      selectors.put("class", "SysListView32");
    }
    WinListView winListView = new WinListView(this, selectors);
    return winListView;
  }

  public WinTreeView winTreeView()
  {
    HashMap selectors = new HashMap();
    selectors.put("class", "SysTreeView32");
    WinTreeView winTreeView = new WinTreeView(this, selectors);
    return winTreeView;
  }

  public WinTreeView winTreeView(String options)
  {
    HashMap selectors = toHash(options);
    if (selectors.get("class") == null) {
      selectors.put("class", "SysTreeView32");
    }
    WinTreeView winTreeView = new WinTreeView(this, selectors);
    return winTreeView;
  }

  public WinToolBar winToolBar()
  {
    HashMap selectors = new HashMap();
    selectors.put("class", "ToolbarWindow32");
    WinToolBar winToolBar = new WinToolBar(this, selectors);
    return winToolBar;
  }

  public WinToolBar winToolBar(String options)
  {
    HashMap selectors = toHash(options);
    if (selectors.get("class") == null) {
      selectors.put("class", "ToolbarWindow32");
    }
    WinToolBar winToolBar = new WinToolBar(this, selectors);
    return winToolBar;
  }

  public WinTabControl winTabControl()
  {
    HashMap selectors = new HashMap();
    selectors.put("class", "SysTabControl32");
    WinTabControl winTabControl = new WinTabControl(this, selectors);
    return winTabControl;
  }

  public WinTabControl winTabControl(String options)
  {
    HashMap selectors = toHash(options);
    if (selectors.get("class") == null) {
      selectors.put("class", "SysTabControl32");
    }
    WinTabControl winTabControl = new WinTabControl(this, selectors);
    return winTabControl;
  }

  public HashMap<String, Object> toHash(String options)
  {
    HashMap map = new HashMap();
    String methodInfo = java.lang.Thread.currentThread().getStackTrace()[2].getMethodName() + "(\"" + options + "\")";
    map.put("methodInfo", methodInfo);
    String[] optionInfos = options.split(",");
    for (String optionInfo : optionInfos) {
      if (optionInfo.indexOf("=>") < 0) {
        this.errorInfo = (options + "格式错误,请参照（属性=>属性值）,示例：ie.button(\"id=>kw\").click();");
        LogModule.logStepFail(StepType.ACTION, methodInfo, RunResult.FAIL, this.errorInfo);
        throw new RuntimeException(this.errorInfo);
      }

      String[] option = optionInfo.split("=>");
      if (option.length == 2) {
        if ((option[1].startsWith("/")) && (option[1].endsWith("/")) && (option[1].lastIndexOf("/") != option[1].indexOf("/"))) {
          Pattern pattern = Pattern.compile(option[1].substring(1, option[1].length() - 1), 32);
          map.put(option[0], pattern);
        } else {
          if (option[0].toLowerCase().equals("index")) {
            option[0] = "instance";
          }
          map.put(option[0], option[1]);
        }
      } else {
        this.errorInfo = (options + "格式错误,请参照（属性=>属性值）,多个属性键值匹配用逗号分隔,示例：ie.button(\"name=>aa,type=>submit,index=>1\").click();");
        LogModule.logStepFail(StepType.ACTION, methodInfo, RunResult.FAIL, this.errorInfo);
        throw new RuntimeException(this.errorInfo);
      }
    }
    return map;
  }
}