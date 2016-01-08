package com.travelsky.autotest.autosky.elements;

import com.travelsky.autotest.autosky.browser.Container;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import java.util.HashMap;
import org.openqa.selenium.WebElement;

public class Checkbox extends Element
{
  public Checkbox(Container container, HashMap<String, Object> options)
  {
    super(container, options);
  }

  public void check()
  {
    this.methodInfo += ".check()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      int retry = 3;
      while ((!this.current.isSelected()) && (retry > 0)) {
        this.current.click();
        retry--;
      }
      highlight(Boolean.valueOf(false));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void uncheck()
  {
    this.methodInfo += ".uncheck()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      int retry = 3;
      while ((this.current.isSelected()) && (retry > 0)) {
        this.current.click();
        retry--;
      }
      highlight(Boolean.valueOf(false));
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void set(Boolean check)
  {
    this.methodInfo = (this.methodInfo + ".set(" + check + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      int retry = 3;
      while ((this.current.isSelected() != check.booleanValue()) && (retry > 0)) {
        this.current.click();
        retry--;
      }
      highlight(Boolean.valueOf(false));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }
}