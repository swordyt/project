package com.travelsky.autotest.autosky.window;

import com.travelsky.autotest.autosky.autoit.AutoItX;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Window extends WinContainer
{
  private HashMap<String, Object> options;
  public static String HWND;

  public Window(HashMap<String, Object> options)
  {
    this.options = options;
  }

  public Window(String options)
  {
    this.methodInfo = ("window(" + options + ")");
    this.windowInfo = ("window(" + options + ")");
    this.options = toHash(options);
  }

  public Window attach(String options)
  {
    this.methodInfo = ("attach(" + options + ")");
    this.windowInfo = ("window(" + options + ")");
    this.options = toHash(options);
    return this;
  }

  public String locate()
  {
    this.options.remove("methodInfo");
    WinLocator winLocator = new WinLocator(Boolean.valueOf(true), this.options);
    this.hWnd = winLocator.locate();
    if (this.hWnd == null) {
      this.errorInfo = (this.windowInfo + "定位失败！");
    } else {
      HWND = this.hWnd;
      this.hWndProperty = ("[Handle:" + this.hWnd + "]");
    }
    return this.hWnd;
  }

  protected Boolean assertExists()
  {
    locate();
    return Boolean.valueOf(this.hWnd != null);
  }

  public String getHandle()
  {
    this.methodInfo = (this.windowInfo + ".getHandle()");
    String result = "";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetHandle(this.hWndProperty);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public void focus()
  {
    this.methodInfo = (this.windowInfo + ".focus()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().winActivate(this.hWndProperty);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void winMenuSelectItem(String[] items)
  {
    this.methodInfo = (this.windowInfo + ".winMenuSelectItem()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      Boolean success = Boolean.valueOf(AutoItX.getInstance().winMenuSelectItem(this.hWndProperty, "", items));
      if (!success.booleanValue())
        throw new Exception("");
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public int getClientSizeWidth()
  {
    int result = 0;
    this.methodInfo = (this.windowInfo + ".getClientSizeWidth()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetClientSizeWidth(this.hWndProperty);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public int getClientSizeHeight()
  {
    int result = 0;
    this.methodInfo = (this.windowInfo + ".getClientSizeHeight()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetClientSizeHeight(this.hWndProperty);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public int getPosX()
  {
    int result = 0;
    this.methodInfo = (this.windowInfo + ".getPosX()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetPosX(this.hWndProperty);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public int getPosY()
  {
    int result = 0;
    this.methodInfo = (this.windowInfo + ".getPosY()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetPosY(this.hWndProperty);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public int getPosWidth()
  {
    int result = 0;
    this.methodInfo = (this.windowInfo + ".getPosWidth()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetPosWidth(this.hWndProperty);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public int getPosHeight()
  {
    int result = 0;
    this.methodInfo = (this.windowInfo + ".getPosHeight()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetPosHeight(this.hWndProperty);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public String getProcess()
  {
    String result = "-1";
    this.methodInfo = (this.windowInfo + ".getProcess()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetProcess(this.hWndProperty);
      if (result.equals("-1"))
        throw new Exception("");
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public int getState()
  {
    int result = 0;
    this.methodInfo = (this.windowInfo + ".getState()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetState(this.hWndProperty);
      if (result == 0)
        throw new Exception("");
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public String getText()
  {
    String result = "";
    this.methodInfo = (this.windowInfo + ".getText()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetText(this.hWndProperty);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public String getTitle()
  {
    String result = "";
    this.methodInfo = (this.windowInfo + ".getTitle()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().winGetTitle(this.hWndProperty);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  private String toProperties(HashMap<String, Object> options) {
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

  public String[][] winList()
  {
    this.methodInfo = (this.windowInfo + ".winList()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }String[][] result;
    try {
      result = AutoItX.getInstance().winList(toProperties(this.options));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public void close()
  {
    this.methodInfo = (this.windowInfo + ".close()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().winClose(this.hWndProperty);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void kill()
  {
    this.methodInfo = (this.windowInfo + ".kill()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().winKill(this.hWndProperty);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void minimize()
  {
    this.methodInfo = (this.windowInfo + ".minimize()");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().winMinimizeAll();
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void sendKeys(String keys)
  {
    this.methodInfo = (this.windowInfo + ".sendKeys(" + keys + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().winActivate(this.hWndProperty);
      AutoItX.getInstance().send(keys);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void sendSpecialKeys(String keys)
  {
    this.methodInfo = (this.windowInfo + ".sendKeys(" + keys + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().winActivate(this.hWndProperty);
      AutoItX.getInstance().send(keys, false);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void sleep(int s)
  {
    this.methodInfo = (this.windowInfo + ".sleep(" + s + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().sleep(1000 * s);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void mouseClick(String button, int[] args)
  {
    this.methodInfo = (this.windowInfo + ".mouseClick(" + button + args + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().mouseClick(button, args);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void mouseClick(String button, int posX, int posY)
  {
    this.methodInfo = (this.windowInfo + ".mouseClick(" + button + "," + posX + "," + posY + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().mouseClick(button, posX, posY, 1, 0);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }
}