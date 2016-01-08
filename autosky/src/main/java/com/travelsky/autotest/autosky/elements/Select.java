package com.travelsky.autotest.autosky.elements;

import com.travelsky.autotest.autosky.browser.Container;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Select extends Element
{
  protected static final Log logger = LogFactory.getLog(Select.class);

  public Select(Container container, HashMap<String, Object> options) {
    super(container, options);
  }

  public void setOptionByValue(String value)
  {
    this.methodInfo = (this.methodInfo + ".setOptionByValue(\"" + value + "\")");
    Boolean exists = Boolean.valueOf(false);
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      List<WebElement> optionList = this.current.findElements(By.tagName("option"));
      for (WebElement op : optionList) {
        if (value.equalsIgnoreCase(op.getAttribute("value"))) {
          op.click();
          exists = Boolean.valueOf(true);
          break;
        }
      }
      if (!exists.booleanValue())
        throw new Exception("找不到选项：" + value);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void setOptionByText(String text)
  {
    this.methodInfo = (this.methodInfo + ".setOptionByText(\"" + text + "\")");
    Boolean exists = Boolean.valueOf(false);
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      List<WebElement> optionList = this.current.findElements(By.tagName("option"));
      Pattern pattern = null;
      if ((text.startsWith("/")) && (text.endsWith("/"))) {
        pattern = Pattern.compile(text.substring(1, text.length() - 1));
      }
      for (WebElement op : optionList)
      {
        String actualText = op.getText();
        if (actualText != null)
        {
          if ((pattern == null) && (text.equalsIgnoreCase(actualText.trim()))) {
            op.click();
            exists = Boolean.valueOf(true);
            break;
          }if (pattern != null) {
            Matcher matcher = pattern.matcher(actualText.trim());
            if ((matcher.matches()) || (matcher.find())) {
              op.click();
              exists = Boolean.valueOf(true);
              break;
            }
          }
        }
      }
      if (!exists.booleanValue())
        throw new Exception("找不到选项：" + text);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void setOptionByIndex(int index)
  {
    this.methodInfo = (this.methodInfo + ".setOptionByIndex(" + index + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      List optionList = this.current.findElements(By.tagName("option"));
      ((WebElement)optionList.get(index - 1)).click();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void setOptionByRand(int start, int end)
  {
    Random random = new Random(start);
    int index = start + random.nextInt(end + 1 - start);
    this.methodInfo = (this.methodInfo + ".setOptionByRand(" + start + "," + end + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      List optionList = this.current.findElements(By.tagName("option"));
      ((WebElement)optionList.get(index - 1)).click();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void setOptionByRand()
  {
    int start = 1;
    this.methodInfo += ".setOptionByRand()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      List optionList = this.current.findElements(By.tagName("option"));
      Random random = new Random();
      int index = start + random.nextInt(optionList.size() + 1 - start);
      this.methodInfo = (this.methodInfo + "在" + optionList.size() + "选项中随机到第" + index + "个选项");
      ((WebElement)optionList.get(index - 1)).click();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public List<String> getSelectedTextList()
  {
    this.methodInfo += ".getSelectedTextList()";
    List selectedTextList = new ArrayList();
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      List<WebElement> optionList = this.current.findElements(By.tagName("option"));
      for (WebElement element : optionList)
        if (element.isSelected())
          selectedTextList.add(element.getText().toString());
    }
    catch (Exception e)
    {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
    return selectedTextList;
  }

  public List<String> getSelectedValueList()
  {
    List selectedValueList = new ArrayList();
    this.methodInfo += ".getSelectedValueList()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      List<WebElement> optionList = this.current.findElements(By.tagName("option"));
      for (WebElement element : optionList)
        if (element.isSelected())
          selectedValueList.add(element.getAttribute("value"));
    }
    catch (Exception e)
    {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
    return selectedValueList;
  }

  public List<Element> getOptionList()
  {
    HashMap temp = new HashMap();
    temp.put("tagName", ".//option");
    temp.put("methodInfo", this.methodInfo + "getOptionList()");
    return getElementList(temp);
  }

  public List<String> getOptionTextList()
  {
    List optionTextList = new ArrayList();
    HashMap temp = new HashMap();
    temp.put("tagName", ".//option");
    temp.put("methodInfo", this.methodInfo + "getOptionTextList()");
    optionTextList = getElementTextList(temp);
    return optionTextList;
  }
}