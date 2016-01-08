package com.travelsky.autotest.autosky.junit.rules;

import com.travelsky.autotest.autosky.browser.Browser;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.junit.enums.TestSuiteType;
import com.travelsky.autotest.autosky.window.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIn;
import org.junit.Assert;
import org.junit.rules.Verifier;
import org.junit.runners.model.MultipleFailureException;
import org.openqa.selenium.WebDriver;
import org.springframework.web.util.HtmlUtils;

public class AssertCollector extends Verifier
{
  private List<Throwable> errors = new ArrayList();
  private Boolean hasError = Boolean.valueOf(false);

  private Boolean isBreak = Boolean.valueOf(false);

  protected void verify() throws Throwable
  {
    MultipleFailureException.assertEmpty(this.errors);
  }

  private void addError(Throwable error)
  {
    this.errors.add(error);
  }

  private <T> void checkThat(final String reason, final T value, final Matcher<T> matcher)
  {
    if (getIsSkip().booleanValue())
      return;
    this.hasError = Boolean.valueOf(false);
    checkSucceeds(new Callable() {
      public Object call() throws Exception {
        Assert.assertThat(reason, value, matcher);
        return value;
      }
    });
  }

  private Object checkSucceeds(Callable<Object> callable)
  {
    try
    {
      return callable.call();
    } catch (Throwable e) {
      this.hasError = Boolean.valueOf(true);
      addError(e);
    }return null;
  }

  public void assertTrue(boolean actual)
  {
    assertTrue(actual, "");
  }

  public void assertTrue(boolean actual, String message)
  {
    assertTrue(actual, message, null);
  }

  public void assertTrue(boolean actual, String message, Browser browser)
  {
    String methodInfo = "assertTrue(" + String.valueOf(actual) + "," + message + ")";
    checkThat(message, Boolean.valueOf(actual), Matchers.is(Boolean.valueOf(true)));
    assertLog(methodInfo, message, browser);
  }

  public void assertFalse(boolean actual)
  {
    assertFalse(actual, "");
  }

  public void assertFalse(boolean actual, String message)
  {
    assertFalse(actual, message, null);
  }

  public void assertFalse(boolean actual, String message, Browser browser)
  {
    String methodInfo = "assertFalse(" + String.valueOf(actual) + "," + message + ")";
    checkThat(message, Boolean.valueOf(actual), Matchers.is(Boolean.valueOf(false)));
    assertLog(methodInfo, message, browser);
  }

  public <T> void assertEqual(T actual, T expect) {
    assertEqual(actual, expect, "");
  }

  public <T> void assertEqual(T actual, T expect, String message)
  {
    assertEqual(actual, expect, message, null);
  }

  public <T> void assertEqual(T actual, T expect, String message, Browser browser)
  {
    String methodInfo = "assertEqual(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
    checkThat(message, actual, Matchers.equalTo(expect));
    assertLog(methodInfo, message, browser);
  }

  public void assertMatch(String actual, String regxp)
  {
    assertMatch(actual, regxp, "");
  }

  public void assertMatch(String actual, String regxp, String message)
  {
    assertMatch(actual, regxp, message, null);
  }

  public void assertMatch(String actual, String regxp, String message, Browser browser)
  {
    checkThat(message, Boolean.valueOf(actual.matches(regxp)), Matchers.is(Boolean.valueOf(true)));
    String methodInfo = "assertMatch(" + String.valueOf(actual) + "," + String.valueOf(regxp) + "," + message + ")";
    assertLog(methodInfo, message, browser);
  }

  public void assertContains(String actual, String expect)
  {
    assertContains(actual, expect, "");
  }

  public void assertContains(String actual, String expect, String message)
  {
    assertContains(actual, expect, message, null);
  }

  public void assertContains(String actual, String expect, String message, Browser browser)
  {
    checkThat(message, actual, Matchers.is(Matchers.containsString(expect)));
    String methodInfo = "assertContains(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
    assertLog(methodInfo, message, browser);
  }

  public <T> void assertContains(Collection<T> actual, T expect)
  {
    assertContains(actual, expect, "", null);
  }

  public <T> void assertContains(Collection<T> actual, T expect, String message)
  {
    assertContains(actual, expect, message, null);
  }

  public <T> void assertContains(Collection<T> actual, T expect, String message, Browser browser)
  {
    checkThat(message, actual, Matchers.hasItem(expect));
    String methodInfo = "assertContains(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
    assertLog(methodInfo, message, browser);
  }

  public void assertIn(String actual, String expect)
  {
    assertIn(actual, expect, "");
  }

  public void assertIn(String actual, String expect, String message)
  {
    assertIn(actual, expect, message, null);
  }

  public void assertIn(String actual, String expect, String message, Browser browser)
  {
    checkThat(message, expect, Matchers.is(Matchers.containsString(actual)));
    String methodInfo = "assertIn(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
    assertLog(methodInfo, message, browser);
  }

  public <T> void assertIn(T actual, Collection<T> expect)
  {
    assertIn(actual, expect, "", null);
  }

  public <T> void assertIn(T actual, Collection<T> expect, String message)
  {
    assertIn(actual, expect, message, null);
  }

  public <T> void assertIn(T actual, Collection<T> expect, String message, Browser browser)
  {
    checkThat(message, actual, IsIn.isIn(expect));
    String methodInfo = "assertIn(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
    assertLog(methodInfo, message, browser);
  }

  public <T> void assertOut(T actual, Collection<T> expect)
  {
    assertOut(actual, expect, "", null);
  }

  public <T> void assertOut(T actual, Collection<T> expect, String message)
  {
    assertOut(actual, expect, message, null);
  }

  public <T> void assertOut(T actual, Collection<T> expect, String message, Browser browser)
  {
    checkThat(message, actual, Matchers.not(IsIn.isIn(expect)));
    String methodInfo = "assertOut(" + String.valueOf(actual) + "," + String.valueOf(expect) + "," + message + ")";
    assertLog(methodInfo, message, browser);
  }

  private void assertLog(String methodInfo, String message, Browser browser)
  {
    if (getIsSkip().booleanValue())
      return;
    WebDriver driver = browser == null ? Browser.currentDriver : browser.driver;

    if (this.hasError.booleanValue()) {
      String errorMessage = ((Throwable)this.errors.get(this.errors.size() - 1)).getMessage();
      errorMessage = HtmlUtils.htmlEscape(errorMessage);
      LogModule.logStepFail(StepType.ASSERT, methodInfo, RunResult.FAIL, errorMessage, Browser.currentDriver == null ? TestSuiteType.WINDOWS_UI : Window.HWND == null ? TestSuiteType.OTHER : TestSuiteType.WEB_UI);

      if (this.isBreak.booleanValue())
        throw new RuntimeException(errorMessage);
    }
    else
    {
      LogModule.logStepPass(StepType.ASSERT, methodInfo, RunResult.PASS);
    }
  }

  public void setIsBreak(Boolean isBreak)
  {
    this.isBreak = isBreak;
  }

  public Boolean getIsBreak() {
    return this.isBreak;
  }

  public void logStepFail(String stepDesc, String failReason)
  {
    LogModule.logStepFail(StepType.CUSTOM, stepDesc, RunResult.FAIL, failReason, Browser.currentDriver == null ? TestSuiteType.WINDOWS_UI : Window.HWND == null ? TestSuiteType.OTHER : TestSuiteType.WEB_UI);
  }

  public void logStepPass(String stepDesc)
  {
    LogModule.logStepPass(StepType.CUSTOM, stepDesc, RunResult.PASS);
  }

  public Boolean getIsSkip()
  {
    Boolean isSkip = Boolean.valueOf(!Boolean.valueOf(DriverService.PROPERTIES.getProperty("caseAssert")).booleanValue());
    return isSkip;
  }
}