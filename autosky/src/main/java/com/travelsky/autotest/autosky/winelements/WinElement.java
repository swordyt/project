package com.travelsky.autotest.autosky.winelements;

import com.travelsky.autotest.autosky.autoit.AutoItX;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.window.WinContainer;
import com.travelsky.autotest.autosky.window.WinLocator;
import java.util.HashMap;
import org.openqa.selenium.WebElement;

public class WinElement
{
  protected HashMap<String, Object> options;
  public WinContainer winContainer;
  public String methodInfo;
  public String errorInfo;
  public String hWnd;
  public WinElement current;
  public String hWndProperty;

  public WinElement(WinContainer winContainer, HashMap<String, Object> options)
  {
    this.winContainer = winContainer;
    this.methodInfo = ((String)options.remove("methodInfo"));
    this.options = options;
  }

  public String locate()
  {
    if ((this.options.get("element") != null) && ((this.options.get("element") instanceof WebElement))) {
      this.current = ((WinElement)this.options.get("winElement"));
      if (this.methodInfo == null) {
        this.methodInfo = (this.winContainer.windowInfo + ".(\"winElement=>" + this.current + "\")");
      }
      this.hWnd = this.current.hWnd;
      this.hWndProperty = ("[Handle:" + this.hWnd + "]");
    } else {
      try {
        this.winContainer.locate();
        WinLocator winLocator = new WinLocator(this.winContainer, this.options);
        this.hWnd = winLocator.locate();
        this.methodInfo = (this.winContainer.windowInfo + "." + this.methodInfo);
        if (this.hWnd == null)
        {
          if (this.winContainer.errorInfo == null)
            this.errorInfo = (this.methodInfo + "定位失败！");
          else
            this.errorInfo = this.winContainer.errorInfo;
        }
        else
          this.hWndProperty = ("[Handle:" + this.hWnd + "]");
      }
      catch (Exception e) {
        this.methodInfo = (this.winContainer.methodInfo + "." + this.methodInfo);

        if (this.winContainer.errorInfo == null)
          this.errorInfo = (this.methodInfo + "定位失败！" + e.getMessage());
        else {
          this.errorInfo = this.winContainer.errorInfo;
        }
      }
    }

    return this.hWnd;
  }

  protected Boolean assertExists()
  {
    locate();
    return Boolean.valueOf(this.hWnd != null);
  }

  public void focus()
  {
    this.methodInfo += ".focus()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlFocus(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void hide()
  {
    this.methodInfo += ".hide()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlHide(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void show()
  {
    this.methodInfo += ".show()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlShow(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void enable()
  {
    this.methodInfo += ".enable()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlEnable(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void disable()
  {
    this.methodInfo += ".disable()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlDisable(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void click()
  {
    this.methodInfo += ".click()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      controlClick("left", 1);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void mouseClick(String button, int xPosition, int yPosition)
  {
    this.methodInfo = (this.methodInfo + ".mouseClick(" + xPosition + "," + yPosition + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      controlClick(button, 1, xPosition, yPosition);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void mouseClick(int xPosition, int yPosition)
  {
    this.methodInfo = (this.methodInfo + ".mouseClick(" + xPosition + "," + yPosition + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      controlClick("left", 1, xPosition, yPosition);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void rightClick()
  {
    this.methodInfo += ".rightClick()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      controlClick("right", 1);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void doubleClick()
  {
    this.methodInfo += ".doubleClick()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      controlClick("left", 2);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public String getText()
  {
    String result = "";
    this.methodInfo += ".getText()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlGetText(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public int getPosX()
  {
    int result = 0;
    this.methodInfo += ".getPosX()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlGetPosX(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public int getPosY()
  {
    int result = 0;
    this.methodInfo += ".getPosY()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlGetPosY(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public int getPosWidth()
  {
    int result = 0;
    this.methodInfo += ".getPosWidth()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlGetPosWidth(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return result;
  }

  public int getPosHeight()
  {
    int result = 0;
    this.methodInfo += ".getPosHeight()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlGetPosHeight(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  private void controlClick(String button, int clicks, int x, int y)
  {
    AutoItX.getInstance().controlClick(this.hWndProperty, "", "", button, clicks, x, y);
  }

  private void controlClick(String button, int clicks)
  {
    AutoItX.getInstance().controlClick(this.hWndProperty, "", "", button, clicks);
  }
}