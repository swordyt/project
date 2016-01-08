package com.travelsky.autotest.autosky.elements;

import com.travelsky.autotest.autosky.browser.Container;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Cell extends Element
{
  public Cell(Container container, HashMap<String, Object> options)
  {
    super(container, options);
  }

  public int index()
  {
    int index = 0;
    this.methodInfo += ".index()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      WebElement parent = (WebElement)executeJs("return arguments[0].parentNode;", new Object[] { this.current });
      List<WebElement> webElementList = parent.findElements(By.xpath("./*"));

      for (WebElement webElement : webElementList) {
        index++;
        if (webElement.getText().equals(this.current.getText()))
          break;
      }
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return index;
  }
}