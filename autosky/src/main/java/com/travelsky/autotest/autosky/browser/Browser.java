package com.travelsky.autotest.autosky.browser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.BrowserProcessType;
import com.travelsky.autotest.autosky.junit.enums.BrowserType;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.utils.FileUtil;
import com.travelsky.autotest.autosky.utils.SystemUtil;

public class Browser extends Container
{
  public static WebDriver currentDriver;
  public static List<WebDriver> drivers = new ArrayList();
  public static BrowserType currentBrowserType = null;
  public static BrowserProcessType currentBrowserProcess;
  public static int TIMEOUT = 60;

  public static void killBrowserProcess() {
    killBrowserProcess(currentBrowserType);
  }

  public void openIEBrowser()
  {
  }

  public Browser() {
    StackTraceElement[] stack = new Throwable().getStackTrace();
    try {
      Class calledClass = Class.forName(stack[1].getClassName());
      if (calledClass.isAssignableFrom(getClass()))
        return;
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }

    if (currentBrowserType == null) {
      currentBrowserType = BrowserType.CHROME;
    }
    switch (currentBrowserType.ordinal()) {
    case 0:
      System.setProperty("webdriver.ie.driver", "resource/IEDriverServer.exe".toString());
      DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
      capabilities.setCapability("ignoreProtectedModeSettings", true);
      this.driver = new InternetExplorerDriver(capabilities);
      this.driver.manage().window().maximize();
      this.driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
      currentDriver = this.driver;
      drivers.add(this.driver);
      currentBrowserType = BrowserType.IE;
      break;
    case 1:
      System.setProperty("webdriver.chrome.driver", "resource/chromedriver.exe".toString());
      ChromeOptions options = new ChromeOptions();
      options.addArguments(new String[] { "start-maximized" });
      this.driver = new ChromeDriver(options);
      this.driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
      currentDriver = this.driver;
      drivers.add(this.driver);
      currentBrowserType = BrowserType.CHROME;
      break;
    case 2:
      String path = FirefoxBrowser.getFirefoxInstallFolder();
      System.setProperty("webdriver.firefox.bin", path + "\\firefox.exe");
      ProfilesIni defaultProfiles = new ProfilesIni();
      FirefoxProfile profile = defaultProfiles.getProfile("default");
      this.driver = new FirefoxDriver(profile);
      this.driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
      currentDriver = this.driver;
      drivers.add(currentDriver);
      currentBrowserType = BrowserType.FIREFOX;
      break;
    }
  }

  public static Browser start(String url)
  {
    Browser browser = null;

    if (currentBrowserType == null) {
      currentBrowserType = BrowserType.CHROME;
    }
    switch (currentBrowserType.ordinal()) {
    case 0:
      browser = IEBrowser.start(url);
      break;
    case 1:
      browser = ChromeBrowser.start(url);
      break;
    case 2:
      browser = FirefoxBrowser.start(url);
      break;
    }

    return browser;
  }

  public static void killBrowserProcess(BrowserType browserType)
  {
    if (browserType == null) {
      browserType = BrowserType.IE;
    }

    if (System.getProperties().getProperty("os.name").contains("Windows"))
      return;
    switch (browserType.ordinal()) {
    case 1:
      SystemUtil.killProcess("iexplore.exe");
      SystemUtil.killProcess("IEDriverServer.exe");

      break;
    case 2:
      SystemUtil.killProcess("chrome.exe");
      SystemUtil.killProcess("chromedriver.exe");
      break;
    case 3:
      SystemUtil.killProcess("firefox.exe");
    }
    try
    {
      Thread.sleep(1000L);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void killAllBrowserProcess()
  {
    SystemUtil.killProcess("IEDriverServer.exe");
    SystemUtil.killProcess("iexplore.exe");
    SystemUtil.killProcess("chromedriver.exe");
    SystemUtil.killProcess("chrome.exe");
    try
    {
      Thread.sleep(2000L);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static String currentUrl()
  {
    String currentUrl = "";
    String methodInfo = "BaseBrowser.currentUrl()";
    try {
      currentUrl = currentDriver.getCurrentUrl();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, methodInfo, RunResult.FAIL, e.getMessage(), currentDriver);
      throw new StepException(methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, methodInfo, RunResult.PASS, currentDriver);
    return currentUrl;
  }

  public static void closeAll()
  {
    for (WebDriver driver : drivers)
      try {
        driver.quit();
      }
      catch (Exception e)
      {
      }
    drivers.clear();
  }

  public Browser locate()
  {
    String title = "";
    try {
      if (this.hasFrame) {
        this.driver.switchTo().defaultContent();
        this.hasFrame = false;
      }
      currentDriver = this.driver;
      title = this.driver.getTitle();
    }
    catch (Exception e) {
    }
    this.methodInfo = (getClass().getSimpleName() + "(\"" + title + "\")");
    return this;
  }

  public Browser wd()
  {
    locate();
    return this;
  }

  public void maximizeWindow()
  {
    locate();
    this.methodInfo += ".maximizeWindow()";
    try {
      this.driver.manage().window().maximize();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void goTo(String url)
  {
    locate();
    this.methodInfo = (this.methodInfo + ".goTo(\"" + url + "\")");
    if ((url.indexOf("http://") < 0) && (url.indexOf("https://") < 0))
    {
      url = "http://" + url;
    }
    try
    {
      this.driver.get(url);
      this.driver.manage().window().maximize();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public String url()
  {
    locate();
    String url = "";
    this.methodInfo += ".url()";
    try {
      url = this.driver.getCurrentUrl();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
    return url;
  }

  public String text()
  {
    locate();
    String text = "";
    this.methodInfo += ".text()";
    try {
      text = this.driver.findElement(By.tagName("body")).getText();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
    return text;
  }

  public String html()
  {
    locate();
    String html = "";
    this.methodInfo += ".html()";
    try {
      html = this.driver.getPageSource();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
    return html;
  }

  private void switchToWindowByID(String id)
  {
    this.driver.switchTo().window(id);
  }

  public String getWindowHandle()
  {
    locate();
    String windowHandle = "";
    this.methodInfo += ".getWindowHandle()";
    try {
      windowHandle = this.driver.getWindowHandle();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
    return windowHandle;
  }

  public void refresh()
  {
    locate();
    this.methodInfo += ".refresh()";
    try {
      this.driver.navigate().refresh();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), currentDriver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, currentDriver);
  }

  public void forward()
  {
    locate();
    this.methodInfo += ".forward()";
    try {
      this.driver.navigate().forward();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void back()
  {
    locate();
    this.methodInfo += ".back()";
    try {
      this.driver.navigate().back();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void quit()
  {
    locate();
    this.methodInfo += ".quit()";
    try {
      this.driver.quit();
      drivers.remove(this.driver);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public String title()
  {
    locate();
    String title = "";
    this.methodInfo += ".title()";
    try {
      title = this.driver.getTitle();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
    return title;
  }

  public void scrollToUp()
  {
    locate();
    this.methodInfo += ".scrollToUp()";
    try {
      JavascriptExecutor js = (JavascriptExecutor)this.driver;
      js.executeScript("window.scrollTo(0,0);", new Object[0]);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void scrollToDown()
  {
    locate();
    this.methodInfo += ".scrollToDown()";
    try {
      JavascriptExecutor js = (JavascriptExecutor)this.driver;
      js.executeScript("window.scrollTo(0,document.body.scrollHeight);", new Object[0]);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void sendKeys(CharSequence... value)
  {
    locate();
    this.methodInfo += ".sendKeys()";
    try {
      this.driver.switchTo().activeElement().sendKeys(value);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void close()
  {
    locate();
    this.methodInfo += ".close()";
    try {
      if (this.driver.getWindowHandles().size() > 1) {
        this.driver.close();
        this.driver.switchTo().window(this.driver.getWindowHandles().toArray()[0].toString());
      } else {
        this.driver.quit();
        drivers.remove(this.driver);
        if (drivers.size() > 0)
          this.driver = ((WebDriver)drivers.get(0));
      }
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public String version(String browserType)
  {
    locate();
    String version = "";
    this.methodInfo = (this.methodInfo + ".version(" + browserType + ")");
    try {
      JavascriptExecutor js = (JavascriptExecutor)this.driver;
      String userAgent = (String)js.executeScript("return window.navigator.userAgent;", new Object[0]);
      Pattern pattern = Pattern.compile(browserType + "\\/(\\d*)");
      Matcher matcher = pattern.matcher(userAgent);
      while (matcher.find()) {
        String v = matcher.group();
        version = v.split("\\/")[1];
      }
      version = browserType + version;
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
    return version;
  }

  public void clearCookie()
  {
    locate();
    this.methodInfo += ".clearCookie()";
    try {
      this.driver.manage().deleteAllCookies();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public Boolean existByTitle(final String title, final int index, int timeout)
  {
    locate();
    this.methodInfo = (this.methodInfo + ".existByTitle(\"" + title + "\"," + index + "," + timeout + ")");
    Boolean existed = Boolean.valueOf(false);
    try {
      existed = (Boolean)new WebDriverWait(this.driver, timeout).until(new ExpectedCondition()
      {
        public Boolean apply(Object input) {
        	List ids;
          for (WebDriver dr : Browser.drivers) {
            Browser.this.driver = dr;
            input = dr;
            Set<String> handles = ((WebDriver)input).getWindowHandles();
            ids = new ArrayList();
            for (String handle : handles) {
              ((WebDriver)input).switchTo().window(handle);
              if (((WebDriver)input).getTitle().contains(title)) {
                ids.add(handle);
                if (ids.size() == index) {
                  Browser.currentDriver = (WebDriver)input;
                  return Boolean.valueOf(true);
                }
              }
            }
          }
          return Boolean.valueOf(false);
        }
      });
      if (existed.booleanValue())
      {
        executeJs("window.blur();return window.focus();", new Object[0]);
        this.driver.manage().window().maximize();
      }
    } catch (TimeoutException e) {
      return Boolean.valueOf(false);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    return existed;
  }

  public Boolean existByTitle(String title, int timeout)
  {
    return existByTitle(title, 1, timeout);
  }

  public Boolean existByTitle(String title)
  {
    return existByTitle(title, 1, 1);
  }

  public Boolean existByUrl(final String url, final int index, int timeout)
  {
    locate();
    this.methodInfo = (this.methodInfo + ".existByUrl(\"" + url + "\"," + index + "," + timeout + ")");
    Boolean existed = Boolean.valueOf(false);
    try {
      existed = (Boolean)new WebDriverWait(this.driver, timeout).until(new ExpectedCondition()
      {
        public Boolean apply(Object input) {
            List ids;
          for (WebDriver dr : Browser.drivers) {
            Browser.this.driver = dr;
           input = dr;
            Set<String> handles = ((WebDriver)input).getWindowHandles();
            ids = new ArrayList();
            for (String handle : handles) {
             ((WebDriver) input).switchTo().window(handle);
              if (((WebDriver)input).getCurrentUrl().contains(url)) {
                ids.add(handle);
                if (ids.size() == index) {
                  Browser.currentDriver =(WebDriver) input;
                  return Boolean.valueOf(true);
                }
              }
            }
          }
          return Boolean.valueOf(false);
        }
      });
      if (existed.booleanValue())
      {
        executeJs("window.blur();return window.focus();", new Object[0]);
        this.driver.manage().window().maximize();
      }
    } catch (TimeoutException e) {
      return Boolean.valueOf(false);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    return existed;
  }

  public Boolean existByUrl(String url, int timeout)
  {
    return existByUrl(url, 1, timeout);
  }

  public Boolean existByUrl(String url)
  {
    return existByUrl(url, 1, 1);
  }

  public void attachByUrl(final String url, final int index)
  {
    locate();
    this.methodInfo = (this.methodInfo + ".attachByUrl(\"" + url + "\"," + index + ")");
    Boolean attached = Boolean.valueOf(false);
    try {
      attached = (Boolean)new WebDriverWait(this.driver, TIMEOUT).until(new ExpectedCondition()
      {
        public Boolean apply(Object input) {
          for (WebDriver dr : Browser.drivers) {
            Browser.this.driver = dr;
            input = dr;
            Set<String> handles = ((WebDriver)input).getWindowHandles();
            List ids;
            ids = new ArrayList();
            for (String handle : handles) {
             ((WebDriver) input).switchTo().window(handle);
              if (((WebDriver)input).getCurrentUrl().contains(url)) {
                ids.add(handle);
                if (ids.size() == index) {
                  Browser.currentDriver = (WebDriver)input;
                  return Boolean.valueOf(true);
                }
              }
            }
          }
          return Boolean.valueOf(false);
        }
      });
      if (!attached.booleanValue()) {
        LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, "找不到指定的浏览器页面", this.driver);
      } else {
        executeJs("window.blur();return window.focus();", new Object[0]);
        this.driver.manage().window().maximize();
      }
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void attachByUrl(String url)
  {
    attachByUrl(url, 1);
  }

  public void attachByTitle(final String title, final int index)
  {
    locate();
    this.methodInfo = (this.methodInfo + ".attachByTitle(\"" + title + "\"," + index + ")");
    Boolean attached = Boolean.valueOf(false);
    try {
      attached = (Boolean)new WebDriverWait(this.driver, TIMEOUT).until(new ExpectedCondition()
      {
        public Boolean apply(Object input) {
            List ids;
          for (WebDriver dr : Browser.drivers) {
            Browser.this.driver = dr;
            input = dr;
            Set<String> handles = ((WebDriver)input).getWindowHandles();
            ids = new ArrayList();
            for (String handle : handles) {
              ((WebDriver)input).switchTo().window(handle);
              if (((WebDriver)input).getTitle().contains(title)) {
                ids.add(handle);
                if (ids.size() == index) {
                  Browser.currentDriver =(WebDriver) input;
                  return Boolean.valueOf(true);
                }
              }
            }
          }
          return Boolean.valueOf(false);
        }
      });
      if (!attached.booleanValue()) {
        LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, "找不到指定的浏览器页面", this.driver);
      } else {
        executeJs("return window.focus();", new Object[0]);
        this.driver.manage().window().maximize();
      }
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void attachByTitle(String title)
  {
    attachByTitle(title, 1);
  }

  public void attachById(String id)
  {
    locate();
    this.methodInfo = (this.methodInfo + ".attachById(\"" + id + "\")");
    try {
      for (WebDriver dr : drivers) {
        if (dr.getWindowHandles().contains(id)) {
          this.driver = dr;
          this.driver.switchTo().window(id);
          currentDriver = this.driver;
          break;
        }
      }
      this.driver.manage().window().maximize();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!");
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public Alert alert()
  {
    Boolean exist = Boolean.valueOf(hasAlert(TIMEOUT));
    String methodInfo = "弹出框";
    if (exist.booleanValue()) {
      Alert alert = this.driver.switchTo().alert();
      String text = alert.getText();
      LogModule.logStepPass(StepType.ACTION, methodInfo + ":" + text, RunResult.PASS);
      return alert;
    }
    LogModule.logStepFail(StepType.ACTION, methodInfo, RunResult.FAIL, "不存在", this.driver);
    throw new StepException(methodInfo + "定位失败!");
  }

  protected void fileupload(String title, String filePath)
  {
    try {
      Runtime rt = Runtime.getRuntime();
      rt.exec(FileUtil.getFilePath("/downloadfile.exe ") + title + " " + filePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void savefile(String title, String filePath)
  {
    try {
      Runtime rt = Runtime.getRuntime();
      rt.exec(FileUtil.getFilePath("/savefile.exe ") + title + " " + filePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sleep(int s)
  {
    try
    {
      Thread.sleep(1000 * s);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public boolean hasAlert()
  {
    try
    {
      this.driver.switchTo().alert();
      return true; } catch (NoAlertPresentException e) {
    }
    return false;
  }

  public boolean hasAlert(int timeout)
  {
    Boolean exist = Boolean.valueOf(false);
    exist = (Boolean)new WebDriverWait(this.driver, timeout).until(new ExpectedCondition()
    {
      public Object apply(Object input) {
        return Boolean.valueOf(Browser.this.hasAlert());
      }
    });
    return exist.booleanValue();
  }
}