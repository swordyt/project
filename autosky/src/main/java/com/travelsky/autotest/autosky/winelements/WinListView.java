package com.travelsky.autotest.autosky.winelements;

import com.travelsky.autotest.autosky.autoit.AutoItX;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.window.WinContainer;
import java.util.HashMap;

public class WinListView extends WinElement
{
  public WinListView(WinContainer winContainer, HashMap<String, Object> options)
  {
    super(winContainer, options);
  }

  public void selectAll()
  {
    this.methodInfo += ".selectAll()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlListViewSelectAll(this.hWndProperty, "", "", "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void selectClear()
  {
    this.methodInfo += ".selectAll()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlListViewSelectClear(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void selectInvert()
  {
    this.methodInfo += ".selectInvert()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlListViewSelectInvert(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public int getRowCount()
  {
    int result = 0;
    this.methodInfo += ".getRowCount()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlListViewGetItemCount(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public int getColumnCount()
  {
    int result = 0;
    this.methodInfo += ".getColumnCount()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlListViewGetSubItemCount(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public void select(int index)
  {
    this.methodInfo = (this.methodInfo + ".select(" + index + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlListViewSelect(this.hWndProperty, "", "", String.valueOf(index - 1), String.valueOf(index - 1));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void select(int from, int to)
  {
    this.methodInfo = (this.methodInfo + ".select(" + from + "," + to + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlListViewSelect(this.hWndProperty, "", "", String.valueOf(from - 1), String.valueOf(to - 1));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void unSelect(int index)
  {
    this.methodInfo = (this.methodInfo + ".deSelect(" + index + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlListViewDeSelect(this.hWndProperty, "", "", String.valueOf(index - 1), String.valueOf(index - 1));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public void unSelect(int from, int to)
  {
    this.methodInfo = (this.methodInfo + ".deSelect(" + from + "," + to + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      AutoItX.getInstance().controlListViewDeSelect(this.hWndProperty, "", "", String.valueOf(from - 1), String.valueOf(to - 1));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.hWnd);
  }

  public int getItemIndex(String item)
  {
    int result = 0;
    this.methodInfo = (this.methodInfo + ".getItemIndex(" + item + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlListViewFindItem(this.hWndProperty, "", "", item, "1");
      if (result == -1)
        throw new Exception("");
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public int getItemIndex(String item, int column)
  {
    int result = 0;
    this.methodInfo = (this.methodInfo + ".getItemIndex(" + item + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlListViewFindItem(this.hWndProperty, "", "", item, String.valueOf(column - 1));
      if (result == -1)
        throw new Exception("");
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public String getCellText(int row, int column)
  {
    String result = "";
    this.methodInfo = (this.methodInfo + ".getCellText(" + row + "," + column + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlListViewGetText(this.hWndProperty, "", "", String.valueOf(row - 1), String.valueOf(column - 1));
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public Boolean isSelected(int index)
  {
    Boolean result = Boolean.valueOf(false);
    this.methodInfo = (this.methodInfo + ".isSelected(" + index + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = Boolean.valueOf(AutoItX.getInstance().controlListViewIsSelected(this.hWndProperty, "", "", String.valueOf(index - 1)));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public String[] getSelectedItems()
  {
    this.methodInfo += ".getSelectedItems()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }String[] result;
    try {
      result = AutoItX.getInstance().controlListViewGetSelected(this.hWndProperty, "", "", "1").split("\\|");
      if (!result[0].equals("")) {
        for (int i = 0; i < result.length; i++)
          result[i] = String.valueOf(Integer.parseInt(result[i]) + 1);
      }
    }
    catch (Exception e)
    {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }

  public int getSelectedCount()
  {
    int result = 0;
    this.methodInfo += ".getSelectedItems()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.hWnd);
      throw new StepException(this.errorInfo);
    }
    try {
      result = AutoItX.getInstance().controlListViewGetSelectedCount(this.hWndProperty, "", "");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.hWnd);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    return result;
  }
}