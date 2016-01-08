package com.travelsky.autotest.autosky.utils;

import com.travelsky.autotest.autosky.junit.enums.BrowserProcessType;
import com.travelsky.autotest.autosky.junit.enums.BrowserType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SystemUtil
{
  public static Map<BrowserType, BrowserProcessType> browserProcessMap = new HashMap();

  public static void killProcessOld(String processName) {
    Runtime rt = Runtime.getRuntime();
    try {
      rt.exec("cmd.exe /C start wmic process where name='" + processName + "' call terminate");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void killProcess(String processName) {
    Runtime rt = Runtime.getRuntime();
    try {
      rt.exec("cmd.exe /C taskkill /f /im " + processName);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void killBrowserProcess()
  {
  }
}