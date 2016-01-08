package com.travelsky.autotest.autosky.elements;

import com.travelsky.autotest.autosky.browser.Container;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Table extends Element
{
  public Table(Container container, HashMap<String, Object> options)
  {
    super(container, options);
  }

  public List<Element> getRowList()
  {
    HashMap temp = new HashMap();
    temp.put("tagName", "tr");
    temp.put("methodInfo", this.methodInfo + ".getRowList()");
    return getElementList(temp);
  }

  public List<List<String>> getRowTextList()
  {
    this.methodInfo += ".getRowTextList()";
    List rowTextList = new ArrayList();
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      List<WebElement> rowElementList = this.current.findElements(By.xpath(".//tr"));
      for (WebElement rowElement : rowElementList) {
        List cellTextList = new ArrayList();
        List<WebElement> cellElementList = rowElement.findElements(By.xpath(".//th|.//td"));
        for (WebElement cellElement : cellElementList) {
          cellTextList.add(cellElement.getText());
        }
        rowTextList.add(cellTextList);
      }
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
    return rowTextList;
  }

  public Cell getCell(int row, int cell)
  {
    Cell cellElement = row("index=>" + Integer.valueOf(row)).cell("index=>" + Integer.valueOf(cell));
    return cellElement;
  }
}