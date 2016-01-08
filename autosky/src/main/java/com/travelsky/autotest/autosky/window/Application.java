package com.travelsky.autotest.autosky.window;

import com.travelsky.autotest.autosky.autoit.AutoItX;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Application
{
  private static int pid;
  private static List<Integer> pidList = new ArrayList();
  private static String processName;
  private static List<String> processNameList = new ArrayList();

  public static void start(String appName)
  {
    try
    {
      processName = appName.substring(appName.lastIndexOf("\\") + 1);
    } catch (Exception e) {
      processName = appName;
    }
    pid = AutoItX.getInstance().run(appName);
    pidList.add(Integer.valueOf(pid));
    if (!processNameList.contains(processName))
      processNameList.add(processName);
  }

  public static void close(String processName)
  {
    AutoItX.getInstance().processClose(processName);
  }

  public static void close(int pid)
  {
    pidList.remove(pid);
    AutoItX.getInstance().processClose(String.valueOf(pid));
  }

  public static void close()
  {
    pidList.remove(pid);
    AutoItX.getInstance().processClose(String.valueOf(pid));
  }

  public static void closeAllPID()
  {
    for (Iterator i$ = pidList.iterator(); i$.hasNext(); ) { int pid = ((Integer)i$.next()).intValue();
      try {
        AutoItX.getInstance().processClose(String.valueOf(pid));
      }
      catch (Exception e) {
      } }
    pidList.clear();
  }

  public static void closeAllProcess()
  {
    for (String processName : processNameList)
      try {
        int pid = AutoItX.getInstance().processExists(processName);
        while (pid != 0) {
          AutoItX.getInstance().processClose(String.valueOf(pid));
          pid = AutoItX.getInstance().processExists(processName);
        }
      }
      catch (Exception e)
      {
      }
    processNameList.clear();
  }
}