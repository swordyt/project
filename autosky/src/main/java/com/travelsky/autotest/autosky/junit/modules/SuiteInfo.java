package com.travelsky.autotest.autosky.junit.modules;

import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.RunStatus;
import com.travelsky.autotest.autosky.utils.DateUtil;
import java.util.Date;
import java.util.List;

public class SuiteInfo
{
  private String suiteName;
  private RunStatus suiteStatus;
  private RunResult suiteResult;
  private String suiteBrowser;
  private List<ModuleInfo> moduleInfoList;
  private String suiteAuthor;
  private String suiteExpert;
  private int suiteModuleNum;
  private int suiteCaseNum;
  private int passCaseNum;
  private int failCaseNum;
  private float passPercent;
  private Date suiteStartTime;
  private Date suiteStopTime;
  private Boolean reportMerged;
  private long buildId;
  private long buildTaskId;
  private long buildTestSuiteId;

  public String getSuiteName()
  {
    return this.suiteName;
  }

  public void setSuiteName(String suiteName) {
    this.suiteName = suiteName;
  }

  public RunStatus getSuiteStatus() {
    return this.suiteStatus;
  }

  public void setSuiteStatus(RunStatus suiteStatus) {
    this.suiteStatus = suiteStatus;
  }

  public RunResult getSuiteResult() {
    return this.suiteResult;
  }

  public void setSuiteResult(RunResult suiteResult) {
    this.suiteResult = suiteResult;
  }

  public String getSuiteBrowser() {
    return this.suiteBrowser;
  }

  public void setSuiteBrowser(String suiteBrowser) {
    this.suiteBrowser = suiteBrowser;
  }

  public List<ModuleInfo> getModuleInfoList() {
    return this.moduleInfoList;
  }

  public void setModuleInfoList(List<ModuleInfo> moduleInfoList) {
    this.moduleInfoList = moduleInfoList;
  }

  public String getSuiteAuthor() {
    return this.suiteAuthor;
  }

  public void setSuiteAuthor(String suiteAuthor) {
    this.suiteAuthor = suiteAuthor;
  }

  public String getSuiteExpert() {
    return this.suiteExpert;
  }

  public void setSuiteExpert(String suiteExpert) {
    this.suiteExpert = suiteExpert;
  }

  public int getSuiteCaseNum() {
    return this.suiteCaseNum;
  }

  public void setSuiteCaseNum(int suiteCaseNum) {
    this.suiteCaseNum = suiteCaseNum;
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

  public Date getSuiteStartTime() {
    return this.suiteStartTime;
  }

  public void setSuiteStartTime(Date suiteStartTime) {
    this.suiteStartTime = suiteStartTime;
  }

  public Date getSuiteStopTime() {
    return this.suiteStopTime;
  }

  public void setSuiteStopTime(Date suiteStopTime) {
    this.suiteStopTime = suiteStopTime;
  }

  public String getSuiteRunTimeStr() {
    long times = (this.suiteStopTime.getTime() - this.suiteStartTime.getTime()) / 1000L;
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

  public String getSuiteStartTimeStr() {
    return DateUtil.dateToStr(this.suiteStartTime, "HH:mm:ss");
  }

  public String getSuiteStopTimeStr() {
    return DateUtil.dateToStr(this.suiteStopTime, "HH:mm:ss");
  }

  public void setSuiteModuleNum(int suiteModuleNum) {
    this.suiteModuleNum = suiteModuleNum;
  }

  public int getSuiteModuleNum() {
    return this.suiteModuleNum;
  }

  public void setReportMerged(Boolean reportMerged) {
    this.reportMerged = reportMerged;
  }

  public Boolean getReportMerged() {
    return this.reportMerged;
  }

  public void setBuildId(long buildId) {
    this.buildId = buildId;
  }

  public long getBuildId() {
    return this.buildId;
  }

  public void setBuildTaskId(long buildTaskId) {
    this.buildTaskId = buildTaskId;
  }

  public long getBuildTaskId() {
    return this.buildTaskId;
  }

  public void setBuildTestSuiteId(long buildTestSuiteId) {
    this.buildTestSuiteId = buildTestSuiteId;
  }

  public long getBuildTestSuiteId() {
    return this.buildTestSuiteId;
  }
}