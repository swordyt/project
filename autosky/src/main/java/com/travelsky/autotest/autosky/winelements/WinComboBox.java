package com.travelsky.autotest.autosky.winelements;

import com.travelsky.autotest.autosky.autoit.AutoItX;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.window.WinContainer;
import java.util.HashMap;

public class WinComboBox extends WinElement
{
  public WinComboBox(WinContainer winContainer, HashMap<String, Object> options)
  {
    super(winContainer, options);
  }

  public void select(String value)
  {
    this.methodInfo = (this.methodInfo + ".select(" + value + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlCommandSelectString(this.hWndProperty, "", "", value);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void showDropdown()
  {
    this.methodInfo += ".showDropdown()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlCommandShowDropdown(this.hWndProperty, "", "");
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }
}