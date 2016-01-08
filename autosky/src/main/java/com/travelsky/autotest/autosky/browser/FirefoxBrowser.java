package com.travelsky.autotest.autosky.browser;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

public class FirefoxBrowser extends Browser
{
  public static final String FIREFOX_REGISTRY_KEY = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\firefox.exe";

  public FirefoxBrowser()
  {
    String path = getFirefoxInstallFolder();
    System.setProperty("webdriver.firefox.bin", path + "\\firefox.exe");
    ProfilesIni defaultProfiles = new ProfilesIni();
    FirefoxProfile profile = defaultProfiles.getProfile("default");
    this.driver = new FirefoxDriver(profile);
    this.driver.manage().timeouts().pageLoadTimeout(60L, TimeUnit.SECONDS);
    this.driver.manage().timeouts().implicitlyWait(2L, TimeUnit.SECONDS);
    currentDriver = this.driver;
    drivers.add(currentDriver);

    currentBrowserType = BrowserType.FIREFOX;
  }

  public static String getFirefoxInstallFolder()
  {
    String path = null;
    try {
      path = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\firefox.exe", "Path");
    } catch (Throwable e) {
    }
    return path;
  }

  public String version(String browserType)
  {
    return super.version("Firefox");
  }

  public static FirefoxBrowser start(String url) {
    FirefoxBrowser ff = new FirefoxBrowser();
    try {
      currentDriver = ff.driver;
      String title = currentDriver.getTitle();
      ff.methodInfo = ("FirefoxBrowser().start(\"" + url + "\")");
      if ((url.indexOf("http://") < 0) && (url.indexOf("https://") < 0))
      {
        url = "http://" + url;
      }

      ff.driver.get(url);
      ff.driver.manage().window().maximize();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, ff.methodInfo, RunResult.FAIL, e.getMessage(), ff.driver);
      throw new StepException(ff.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, ff.methodInfo, RunResult.PASS, ff.driver);
    return ff;
  }
}