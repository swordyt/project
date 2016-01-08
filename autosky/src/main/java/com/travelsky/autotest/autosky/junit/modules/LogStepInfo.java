package com.travelsky.autotest.autosky.junit.modules;

import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;

public class LogStepInfo
{
  private SuiteInfo suiteInfo;
  private ModuleInfo moduleInfo;
  private String caseName;
  private int stepId;
  private StepType stepType;
  private String stepDesc;
  private RunResult stepResult;
  private String failReason;
  private String picture;
  private String pictureName;
  private String pictureRelative;
  private String stepTime;
  private String url;
  private String failType;

  public SuiteInfo getSuiteInfo()
  {
    return this.suiteInfo;
  }

  public void setSuiteInfo(SuiteInfo suiteInfo) {
    this.suiteInfo = suiteInfo;
  }

  public ModuleInfo getModuleInfo() {
    return this.moduleInfo;
  }

  public void setModuleInfo(ModuleInfo moduleInfo) {
    this.moduleInfo = moduleInfo;
  }

  public String getCaseName() {
    return this.caseName;
  }

  public void setCaseName(String caseName) {
    this.caseName = caseName;
  }

  public String getStepDesc() {
    return this.stepDesc;
  }

  public void setStepDesc(String stepDesc) {
    this.stepDesc = stepDesc;
  }

  public RunResult getStepResult() {
    return this.stepResult;
  }

  public void setStepResult(RunResult stepResult) {
    this.stepResult = stepResult;
  }

  public String getFailReason() {
    return this.failReason;
  }

  public void setFailReason(String failReason) {
    this.failReason = failReason;
  }

  public String getPicture() {
    return this.picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getStepTime() {
    return this.stepTime;
  }

  public void setStepTime(String stepTime) {
    this.stepTime = stepTime;
  }

  public StepType getStepType() {
    return this.stepType;
  }

  public void setStepType(StepType stepType) {
    this.stepType = stepType;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setFailType(String failType) {
    this.failType = failType;
  }

  public String getFailType() {
    return this.failType;
  }

  public void setStepId(int stepId) {
    this.stepId = stepId;
  }

  public int getStepId() {
    return this.stepId;
  }

  public void setPictureRelative(String pictureRelative) {
    this.pictureRelative = pictureRelative;
  }

  public String getPictureRelative() {
    return this.pictureRelative;
  }

  public void setPictureName(String pictureName) {
    this.pictureName = pictureName;
  }

  public String getPictureName() {
    return this.pictureName;
  }
}