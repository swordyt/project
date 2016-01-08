package com.travelsky.autotest.autosky.junit.core;

import com.travelsky.autotest.autosky.database.DBUtil;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.junit.modules.CaseInfo;
import com.travelsky.autotest.autosky.junit.modules.ModuleInfo;
import com.travelsky.autotest.autosky.junit.modules.SuiteInfo;
import com.travelsky.autotest.autosky.junit.rules.DriverService;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

public class DriverModule
  implements Observer
{
  private String sourcesDir = DriverService.PROPERTIES.getProperty("sourcesDir", "src/test/resources/");
  private static DriverModule singleton;
  //存储各个套件对象 所有的套件对象均需要实现了Observer类
  private Vector<Observer> observersVector = new Vector();

  public static DriverModule getInstance()
  {
    if (singleton == null) {
      singleton = new DriverModule();
    }
    return singleton;
  }

  public void attach(Observer observer)
  {
    this.observersVector.addElement(observer);
  }

  public void detach(Observer observer) {
    this.observersVector.removeElement(observer);
  }

  public void detachAll() {
    this.observersVector.clear();
  }

  public Enumeration<Observer> observers()
  {
    return ((Vector)this.observersVector.clone()).elements();
  }

  public void suiteRunStart(SuiteInfo suiteInfo)
  {
    Enumeration enumeration = observers();
    while (enumeration.hasMoreElements())
      ((Observer)enumeration.nextElement()).suiteRunStart(suiteInfo);
  }

  public void moduleRunStart(ModuleInfo moduleInfo)
  {
    Enumeration enumeration = observers();
    while (enumeration.hasMoreElements())
      ((Observer)enumeration.nextElement()).moduleRunStart(moduleInfo);
  }

  public void caseRunStart(CaseInfo caseInfo)
  {
    Enumeration enumeration = observers();
    while (enumeration.hasMoreElements())
      ((Observer)enumeration.nextElement()).caseRunStart(caseInfo);
  }

  public void suiteRunStop(SuiteInfo suiteInfo)
  {
    Enumeration enumeration = observers();
    while (enumeration.hasMoreElements())
      ((Observer)enumeration.nextElement()).suiteRunStop(suiteInfo);
  }

  public void moduleRunStop(ModuleInfo moduleInfo)
  {
    Enumeration enumeration = observers();
    while (enumeration.hasMoreElements())
      ((Observer)enumeration.nextElement()).moduleRunStop(moduleInfo);
  }

  public void caseRunStop(CaseInfo caseInfo)
  {
    Enumeration enumeration = observers();
    while (enumeration.hasMoreElements())
      ((Observer)enumeration.nextElement()).caseRunStop(caseInfo);
  }

  public void initialData(CaseInfo caseInfo)
  {
    String fileName = (String)caseInfo.getCaseParams().get("initialData");
    if (fileName == null) {
      return;
    }
    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
    fileName = findFile(fileName);
    if (fileName == null) {
      LogModule.logStepFail(StepType.DATABASE, "initialData操作失败", RunResult.FAIL, (String)caseInfo.getCaseParams().get("initialData") + "文件不存在!");
      throw new StepException("initialData操作失败!" + (String)caseInfo.getCaseParams().get("initialData") + "文件不存在!");
    }
    switch (com.travelsky.autotest.autosky.junit.enums.FileType.valueOf(fileType).ordinal()) {
    case 1:
      DBUtil.executeSqlFile(fileName, caseInfo.getCaseParams());
      break;
    case 2:
    }
  }

  public void destroyData(CaseInfo caseInfo)
  {
    String fileName = (String)caseInfo.getCaseParams().get("destroyData");
    if (fileName == null) {
      return;
    }
    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
    fileName = findFile(fileName);
    if (fileName == null) {
      LogModule.logStepFail(StepType.DATABASE, "destroyData操作失败", RunResult.FAIL, (String)caseInfo.getCaseParams().get("destroyData") + "文件不存在!");
      throw new StepException("destroyData操作失败!" + (String)caseInfo.getCaseParams().get("destroyData") + "文件不存在!");
    }
    switch (com.travelsky.autotest.autosky.junit.enums.FileType.valueOf(fileType).ordinal()) {
    case 1:
      DBUtil.executeSqlFile(fileName, caseInfo.getCaseParams());
      break;
    case 2:
    }
  }

  private String findFile(String fileName)
  {
    String filePath = System.getProperty("user.dir") + "/" + this.sourcesDir + fileName;
    File file = new File(filePath);

    if (!file.exists()) {
      filePath = System.getProperty("user.dir") + "/" + this.sourcesDir + DriverService.DESCRIPTION.getTestClass().getSimpleName() + "/" + fileName;
      file = new File(filePath);
    }
    if (!file.exists()) {
      filePath = null;
    }
    return filePath;
  }
}