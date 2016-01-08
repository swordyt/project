package com.travelsky.autotest.autosky.junit.core;

import com.travelsky.autotest.autosky.autoit.AutoItX;
import com.travelsky.autotest.autosky.browser.Browser;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.junit.enums.TestSuiteType;
import com.travelsky.autotest.autosky.junit.modules.CaseInfo;
import com.travelsky.autotest.autosky.junit.modules.LogStepInfo;
import com.travelsky.autotest.autosky.junit.modules.ModuleInfo;
import com.travelsky.autotest.autosky.junit.modules.SuiteInfo;
import com.travelsky.autotest.autosky.utils.DateUtil;
import com.travelsky.autotest.autosky.utils.ScreenShotUtil;
import com.travelsky.autotest.autosky.window.Window;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

public class LogModule
{
  public static SuiteInfo SUITE_INFO = new SuiteInfo();
  public static ModuleInfo MODULE_INFO = new ModuleInfo();
  public static CaseInfo CASE_INFO = new CaseInfo();
  public static String SUITE_PATH = "";
  public static List<LogStepInfo> logStepInfoList = new ArrayList();

  public static List<LogStepInfo> onLogStep(LogStepInfo logStepInfo) {
    logStepInfo.setStepId(logStepInfoList.size() + 1);
    logStepInfoList.add(logStepInfo);
    return logStepInfoList;
  }

  public static List<LogStepInfo> logStepFail(StepType stepType, String stepDesc, RunResult stepResult, String failReason, TestSuiteType testSuiteType)
  {
    switch (testSuiteType.ordinal()) {
    case 1:
      return logStepFail(stepType, stepDesc, stepResult, failReason, Browser.currentDriver);
    case 2:
      return logStepFail(stepType, stepDesc, stepResult, failReason, Window.HWND);
    }
    return logStepFail(stepType, stepDesc, stepResult, failReason);
  }

  public static List<LogStepInfo> logStepFail(StepType stepType, String stepDesc, RunResult stepResult, String failReason)
  {
    LogStepInfo logStepInfo = new LogStepInfo();
    logStepInfo.setStepType(stepType);
    logStepInfo.setStepDesc(stepDesc.replace("<", "&lt;").replace(">", "&gt;"));
    logStepInfo.setStepResult(stepResult);
    logStepInfo.setFailReason(failReason);
    String url = "";
    logStepInfo.setUrl(url);
    String stepTime = DateUtil.dateToStr(new Date(), "HH:mm:ss");
    logStepInfo.setStepTime(stepTime);
    String failType = "";
    logStepInfo.setFailType(failType);
    System.err.println(stepDesc + "\n" + failReason);
    return onLogStep(logStepInfo);
  }

  public static List<LogStepInfo> logStepFail(StepType stepType, String stepDesc, RunResult stepResult, String failReason, String hWnd) {
    return logStepFail(stepType, stepDesc, stepResult, failReason, hWnd, "");
  }

  public static List<LogStepInfo> logStepFail(StepType stepType, String stepDesc, RunResult stepResult, String failReason, String hWnd, String failType)
  {
    LogStepInfo logStepInfo = new LogStepInfo();
    List logStepInfoList = logStepFail(stepType, stepDesc, stepResult, failReason);
    logStepInfo = (LogStepInfo)logStepInfoList.remove(logStepInfoList.size() - 1);
    String url = "";
    try {
      url = AutoItX.getInstance().winGetTitle("[Handle:" + hWnd + "]");
    } catch (Exception e) {
    }
    logStepInfo.setUrl(url);
    String picture = "";
    picture = ScreenShotUtil.screenShotByDesktop(SUITE_PATH);
    logStepInfo.setPicture(picture);
    if (!picture.equals("")) {
      String[] pictureAddress = picture.split("screenshot");
      String pictureName = pictureAddress[(pictureAddress.length - 1)].substring(1);
      logStepInfo.setPictureName(pictureName);
      String pictureRelative = "../screenshot/" + pictureName;
      logStepInfo.setPictureRelative(pictureRelative);
    }
    logStepInfo.setFailType(failType);
    return onLogStep(logStepInfo);
  }

  public static List<LogStepInfo> logStepFail(StepType stepType, String stepDesc, RunResult stepResult, String failReason, WebDriver driver) {
    return logStepFail(stepType, stepDesc, stepResult, failReason, driver, "");
  }

  public static List<LogStepInfo> logStepFail(StepType stepType, String stepDesc, RunResult stepResult, String failReason, WebDriver driver, String failType)
  {
    LogStepInfo logStepInfo = new LogStepInfo();
    List logStepInfoList = logStepFail(stepType, stepDesc, stepResult, failReason);
    logStepInfo = (LogStepInfo)logStepInfoList.remove(logStepInfoList.size() - 1);
    String url = "";
    String picture = "";
    try {
      if (isAlert(driver)) {
        url = driver.switchTo().alert().getText();
        picture = ScreenShotUtil.screenShotByDesktop(SUITE_PATH);
      }
      else {
        url = driver.getCurrentUrl();
        picture = ScreenShotUtil.screenShotByDriver(driver, SUITE_PATH);
      }
    } catch (Exception e) {
      if (picture.equals("")) {
        try {
          picture = ScreenShotUtil.screenShotByDesktop(SUITE_PATH);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    logStepInfo.setUrl(url);
    logStepInfo.setPicture(picture);
    if (!picture.equals("")) {
      String[] pictureAddress = picture.split("screenshot");
      String pictureName = pictureAddress[(pictureAddress.length - 1)].substring(1);
      logStepInfo.setPictureName(pictureName);
      String pictureRelative = "../screenshot/" + pictureName;
      logStepInfo.setPictureRelative(pictureRelative);
    }
    logStepInfo.setFailType(failType);
    return onLogStep(logStepInfo);
  }

  public static List<LogStepInfo> logStepPass(StepType stepType, String stepDesc, RunResult stepResult, TestSuiteType testSuiteType) {
    switch (testSuiteType.ordinal()) {
    case 1:
      return logStepPass(stepType, stepDesc, RunResult.PASS, Browser.currentDriver);
    case 2:
      return logStepPass(stepType, stepDesc, RunResult.PASS, Window.HWND);
    }
    return logStepPass(stepType, stepDesc, RunResult.PASS);
  }

  public static List<LogStepInfo> logStepPass(StepType stepType, String stepDesc, RunResult stepResult)
  {
    LogStepInfo logStepInfo = new LogStepInfo();
    logStepInfo.setStepType(stepType);
    logStepInfo.setStepDesc(stepDesc.replace("<", "&lt;").replace(">", "&gt;"));
    logStepInfo.setStepResult(stepResult);
    String url = "";
    logStepInfo.setUrl(url);
    String stepTime = DateUtil.dateToStr(new Date(), "HH:mm:ss");
    logStepInfo.setStepTime(stepTime);
    try {
      Thread.sleep(100L);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(stepDesc);
    return onLogStep(logStepInfo);
  }

  public static List<LogStepInfo> logStepPass(StepType stepType, String stepDesc, RunResult stepResult, String hWnd)
  {
    LogStepInfo logStepInfo = new LogStepInfo();
    List logStepInfoList = logStepPass(stepType, stepDesc, stepResult);
    logStepInfo = (LogStepInfo)logStepInfoList.remove(logStepInfoList.size() - 1);
    String url = "";
    try {
      url = AutoItX.getInstance().winGetTitle("[Handle:" + hWnd + "]");
    } catch (Exception e) {
    }
    logStepInfo.setUrl(url);
    return onLogStep(logStepInfo);
  }

  public static List<LogStepInfo> logStepPass(StepType stepType, String stepDesc, RunResult stepResult, WebDriver driver)
  {
    LogStepInfo logStepInfo = new LogStepInfo();
    List logStepInfoList = logStepPass(stepType, stepDesc, stepResult);
    logStepInfo = (LogStepInfo)logStepInfoList.remove(logStepInfoList.size() - 1);
    String url = "";
    try {
      if (isAlert(driver))
        url = "alert";
      else
        url = driver.getCurrentUrl();
    }
    catch (Exception e) {
    }
    logStepInfo.setUrl(url);
    return onLogStep(logStepInfo);
  }

  public static boolean isAlert(WebDriver driver)
  {
    try
    {
      driver.switchTo().alert();
      return true; } catch (NoAlertPresentException e) {
    }
    return false;
  }

  public static String encode(String str, String encoding)
  {
    try
    {
      return URLEncoder.encode(str, encoding); } catch (UnsupportedEncodingException e) {
    }
    return str;
  }
}