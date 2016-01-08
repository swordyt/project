package com.travelsky.autotest.autosky.browser;

import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.BrowserType;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeBrowser extends Browser
{
  public ChromeBrowser()
  {
    System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe".toString());
    ChromeOptions options = new ChromeOptions();
    options.addArguments(new String[] { "chrome.switches" });

    this.driver = new ChromeDriver(options);

    this.driver.manage().timeouts().implicitlyWait(2L, TimeUnit.SECONDS);
    currentDriver = this.driver;
    drivers.add(this.driver);

    currentBrowserType = BrowserType.CHROME;
  }

  public String version(String browserType)
  {
    return super.version("Chrome");
  }

  public static ChromeBrowser start(String url)
  {
    ChromeBrowser cb = new ChromeBrowser();
    try {
      currentDriver = cb.driver;
      cb.methodInfo = ("ChromeBrowser().start(\"" + url + "\")");
      if ((url.indexOf("http://") < 0) && (url.indexOf("https://") < 0))
      {
        url = "http://" + url;
      }

      cb.driver.get(url);
      cb.driver.manage().window().maximize();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, cb.methodInfo, RunResult.FAIL, e.getMessage(), cb.driver);
      throw new StepException(cb.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, cb.methodInfo, RunResult.PASS, cb.driver);
    return cb;
  }
}