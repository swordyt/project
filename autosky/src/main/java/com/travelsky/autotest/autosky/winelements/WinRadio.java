package com.travelsky.autotest.autosky.winelements;

import com.travelsky.autotest.autosky.autoit.AutoItX;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.window.WinContainer;
import java.util.HashMap;

public class WinRadio extends WinElement
{
  public WinRadio(WinContainer winContainer, HashMap<String, Object> options)
  {
    super(winContainer, options);
  }

  public Boolean isChecked()
  {
    Boolean result = Boolean.valueOf(false);
    this.methodInfo += ".isChecked()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = Boolean.valueOf(AutoItX.getInstance().controlCommandIsChecked(this.hWndProperty, "", ""));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public void check()
  {
    this.methodInfo += ".check()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlCommandCheck(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void uncheck()
  {
    this.methodInfo += ".uncheck()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlCommandUncheck(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void set(Boolean check)
  {
    this.methodInfo = (this.methodInfo + ".set(" + check + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      if (check.booleanValue())
        AutoItX.getInstance().controlCommandCheck(this.hWndProperty, "", "");
      else
        AutoItX.getInstance().controlCommandUncheck(this.hWndProperty, "", "");
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }
}