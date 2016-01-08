package com.travelsky.autotest.autosky.junit.modules;

import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.RunStatus;
import com.travelsky.autotest.autosky.utils.DateUtil;
import java.util.Date;
import java.util.List;

public class ModuleInfo
{
  private SuiteInfo suiteInfo;
  private String moduleName;
  private String moduleDesc;
  private String moduleAuthor;
  private String moduleExpert;
  private Boolean moduleRun;
  private RunStatus moduleStatus;
  private RunResult moduleResult;
  private String moduleBrowser;
  private List<CaseInfo> caseInfoList;
  private int moduleCaseNum;
  private int passCaseNum;
  private int failCaseNum;
  private float passPercent;
  private Date moduleStartTime;
  private Date moduleStopTime;

  public SuiteInfo getSuiteInfo()
  {
    return this.suiteInfo;
  }

  public void setSuiteInfo(SuiteInfo suiteInfo) {
    this.suiteInfo = suiteInfo;
  }

  public RunStatus getModuleStatus() {
    return this.moduleStatus;
  }

  public void setModuleStatus(RunStatus moduleStatus) {
    this.moduleStatus = moduleStatus;
  }

  public String getModuleName() {
    return this.moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public RunResult getModuleResult() {
    return this.moduleResult;
  }

  public void setModuleResult(RunResult moduleResult) {
    this.moduleResult = moduleResult;
  }

  public String getModuleBrowser() {
    return this.moduleBrowser;
  }

  public void setModuleBrowser(String moduleBrowser) {
    this.moduleBrowser = moduleBrowser;
  }

  public List<CaseInfo> getCaseInfoList() {
    return this.caseInfoList;
  }

  public void setCaseInfoList(List<CaseInfo> caseInfoList) {
    this.caseInfoList = caseInfoList;
  }

  public int getModuleCaseNum() {
    return this.moduleCaseNum;
  }

  public void setModuleCaseNum(int moduleCaseNum) {
    this.moduleCaseNum = moduleCaseNum;
  }

  public int getPassCaseNum() {
    return this.passCaseNum;
  }

  public void setPassCaseNum(int passCaseNum) {
    this.passCaseNum = passCaseNum;
  }

  public int getFailCaseNum() {
    return this.failCaseNum;
  }

  public void setFailCaseNum(int failCaseNum) {
    this.failCaseNum = failCaseNum;
  }

  public float getPassPercent() {
    return this.passPercent;
  }

  public void setPassPercent(float passPercent) {
    this.passPercent = passPercent;
  }

  public Date getModuleStopTime() {
    return this.moduleStopTime;
  }

  public void setModuleStopTime(Date moduleStopTime) {
    this.moduleStopTime = moduleStopTime;
  }

  public Date getModuleStartTime() {
    return this.moduleStartTime;
  }

  public void setModuleStartTime(Date moduleStartTime) {
    this.moduleStartTime = moduleStartTime;
  }

  public String getModuleAuthor() {
    return this.moduleAuthor;
  }

  public void setModuleAuthor(String moduleAuthor) {
    this.moduleAuthor = moduleAuthor;
  }

  public String getModuleBusinessAuthor() {
    return this.moduleExpert;
  }

  public void setModuleBusinessAuthor(String moduleExpert) {
    this.moduleExpert = moduleExpert;
  }

  public void setModuleDesc(String moduleDesc) {
    this.moduleDesc = moduleDesc;
  }

  public String getModuleDesc() {
    return this.moduleDesc;
  }

  public String getModuleRunTimeStr() {
    long times = (this.moduleStopTime.getTime() - this.moduleStartTime.getTime()) / 1000L;
    long minutes = times / 60L;
    long second = times % 60L;
    String timeStr = String.valueOf(second) + "秒";
    long hour = minutes / 60L;
    long minute = minutes % 60L;
    if (hour > 0L)
      timeStr = String.valueOf(hour) + "小时" + String.valueOf(minute) + "分" + timeStr;
    else if (minute > 0L) {
      timeStr = String.valueOf(minute) + "分" + timeStr;
    }
    return timeStr;
  }

  public String getModuleStartTimeStr() {
    return DateUtil.dateToStr(this.moduleStartTime, "HH:mm:ss");
  }

  public String getModuleStopTimeStr() {
    return DateUtil.dateToStr(this.moduleStopTime, "HH:mm:ss");
  }

  public void setModuleRun(Boolean moduleRun) {
    this.moduleRun = moduleRun;
  }

  public Boolean getModuleRun() {
    return this.moduleRun;
  }
}