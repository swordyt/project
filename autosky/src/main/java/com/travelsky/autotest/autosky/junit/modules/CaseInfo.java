package com.travelsky.autotest.autosky.junit.modules;

import com.travelsky.autotest.autosky.junit.enums.BrowserType;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.RunStatus;
import com.travelsky.autotest.autosky.utils.DateUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CaseInfo {
	private SuiteInfo suiteInfo;
	private ModuleInfo moduleInfo;
	private String caseName;
	private String caseDesc;
	private Boolean caseRun;
	private RunStatus caseStatus;
	private RunResult caseResult;
	private Map<String, String> caseParams;
	private String caseLevel;
	private BrowserType caseBrowserType;
	private Date caseStartTime;
	private Date caseStopTime;
	private String casePriority;
	private int caseRerunNum;
	private List<LogStepInfo> LogStepInfoList;
	private boolean caseAssert;

	public RunStatus getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(RunStatus caseStatus) {
		this.caseStatus = caseStatus;
	}

	public RunResult getCaseResult() {
		return this.caseResult;
	}

	public void setCaseResult(RunResult caseResult) {
		this.caseResult = caseResult;
	}

	public SuiteInfo getSuiteInfo() {
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

	public String getCaseDesc() {
		return this.caseDesc;
	}

	public void setCaseDesc(String caseDesc) {
		this.caseDesc = caseDesc;
	}

	public Boolean getCaseRun() {
		return this.caseRun;
	}

	public void setCaseRun(Boolean caseRun) {
		this.caseRun = caseRun;
	}

	public Map<String, String> getCaseParams() {
		return this.caseParams;
	}

	public void setCaseParams(Map<String, String> caseParams) {
		this.caseParams = caseParams;
	}

	public String getCaseLevel() {
		return this.caseLevel;
	}

	public void setCaseLevel(String caseLevel) {
		this.caseLevel = caseLevel;
	}

	public void setCaseStartTime(Date caseStartTime) {
		this.caseStartTime = caseStartTime;
	}

	public Date getCaseStartTime() {
		return this.caseStartTime;
	}

	public void setCaseStopTime(Date caseStopTime) {
		this.caseStopTime = caseStopTime;
	}

	public Date getCaseStopTime() {
		return this.caseStopTime;
	}

	public String getCaseRunTimeStr() {
		return String.valueOf((this.caseStopTime.getTime() - this.caseStartTime
				.getTime()) / 1000L);
	}

	public String getCaseStartTimeStr() {
		return DateUtil.dateToStr(this.caseStartTime, "HH:mm:ss");
	}

	public String getCaseStopTimeStr() {
		return DateUtil.dateToStr(this.caseStopTime, "HH:mm:ss");
	}

	public void setCaseBrowserType(BrowserType caseBrowserType) {
		this.caseBrowserType = caseBrowserType;
	}

	public BrowserType getCaseBrowserType() {
		return this.caseBrowserType;
	}

	public void setCasePriority(String casePriority) {
		this.casePriority = casePriority;
	}

	public String getCasePriority() {
		return this.casePriority;
	}

	public void setCaseRerunNum(int caseRerunNum) {
		this.caseRerunNum = caseRerunNum;
	}

	public int getCaseRerunNum() {
		return this.caseRerunNum;
	}

	public void setLogStepInfoList(List<LogStepInfo> logStepInfoList) {
		this.LogStepInfoList = logStepInfoList;
	}

	public List<LogStepInfo> getLogStepInfoList() {
		return this.LogStepInfoList;
	}

	public boolean isCaseAssert() {
		return this.caseAssert;
	}

	public void setCaseAssert(boolean caseAssert) {
		this.caseAssert = caseAssert;
	}
}