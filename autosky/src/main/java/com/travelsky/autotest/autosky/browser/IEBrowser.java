package com.travelsky.autotest.autosky.browser;

import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.BrowserType;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class IEBrowser extends Browser
{
  public IEBrowser()
  {
    System.setProperty("webdriver.ie.driver", "src/main/resources/IEDriverServer.exe".toString());
    DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
    capabilities.setCapability("ignoreProtectedModeSettings", true);
    this.driver = new InternetExplorerDriver(capabilities);
    this.driver.manage().window().maximize();
    this.driver.manage().timeouts().pageLoadTimeout(60L, TimeUnit.SECONDS);
    this.driver.manage().timeouts().implicitlyWait(2L, TimeUnit.SECONDS);
    currentDriver = this.driver;
    drivers.add(this.driver);

    currentBrowserType = BrowserType.IE;
  }

  public static IEBrowser start(String url) {
    IEBrowser ie = new IEBrowser();
    try {
      currentDriver = ie.driver;
      String title = currentDriver.getTitle();
      ie.methodInfo = ("IEBrowser().start(\"" + url + "\")");
      ie.driver.get(url);
      ie.driver.manage().window().maximize();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, ie.methodInfo, RunResult.FAIL, e.getMessage(), ie.driver);
      throw new StepException(ie.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, ie.methodInfo, RunResult.PASS, ie.driver);
    return ie;
  }
}