package com.travelsky.autotest.autosky.elements;

import com.travelsky.autotest.autosky.browser.Container;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import java.util.HashMap;
import org.openqa.selenium.WebElement;

public class Input extends Element
{
  public Input(Container container, HashMap<String, Object> options)
  {
    super(container, options);
  }

  public void set(String value)
  {
    this.methodInfo = (this.methodInfo + ".set(\"" + value + "\")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      this.current.clear();
      this.current.sendKeys(new CharSequence[] { value });
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }
}