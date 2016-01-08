package com.travelsky.autotest.autosky.junit.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.core.Observer;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.junit.modules.CaseInfo;
import com.travelsky.autotest.autosky.junit.modules.LogStepInfo;
import com.travelsky.autotest.autosky.junit.modules.ModuleInfo;
import com.travelsky.autotest.autosky.junit.modules.SuiteInfo;
import com.travelsky.autotest.autosky.junit.rules.DriverService;
import com.travelsky.autotest.autosky.utils.DateUtil;
import com.travelsky.autotest.autosky.utils.HttpUtil;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DatabaseLog implements Observer {
	private String logTestSuiteId;
	private String logTestModuleId;
	private String logTestCaseId;
	private long logPictureId = 0L;

	public void suiteRunStart(SuiteInfo suiteInfo) {
		Map param = new HashMap();
		Map params = new HashMap();
		String machineIp = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			machineIp = addr.getHostAddress().toString();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		param.put("buildId", String.valueOf(suiteInfo.getBuildId()));
		param.put("buildTaskId", String.valueOf(suiteInfo.getBuildTaskId()));
		param.put("buildTestSuiteId",
				String.valueOf(suiteInfo.getBuildTestSuiteId()));

		param.put("testSuiteId", "0");
		param.put("testSuiteName", suiteInfo.getSuiteName());
		param.put("machineIp", machineIp);
		param.put("runner", DriverService.PROPERTIES.getProperty("runner"));
		param.put("runStatus", String.valueOf(suiteInfo.getSuiteStatus()));
		param.put("runResult", String.valueOf(suiteInfo.getSuiteResult()));
		param.put("caseNum", "0");
		param.put("passNum", "0");
		param.put("failNum", "0");
		String dbLogUrl = DriverService.PROPERTIES.getProperty("dbLogUrl");
		try {
			params.put("cname", "admin");
			params.put("cpwd", "admin");

			String json = HttpUtil.postRequest(dbLogUrl
					+ "logTestSuiteAction!start.action", param, null, null);
			JSONObject result = JSON.parseObject(json);
			if (!result.getBoolean("result").booleanValue()) {
				throw new Exception(result.getString("msg"));
			}

			this.logTestSuiteId = result.getString("msg");
		} catch (Exception e) {
			LogModule.logStepFail(StepType.DATABASE,
					"测试套件（" + suiteInfo.getSuiteName() + "）开始运行时日志存储失败",
					RunResult.FAIL, e.getMessage());
		}
	}

	public void suiteRunStop(SuiteInfo suiteInfo) {
		Map param = new HashMap();
		param.put("runStatus", String.valueOf(suiteInfo.getSuiteStatus()));
		param.put("runResult", String.valueOf(suiteInfo.getSuiteResult()));
		param.put("moduleNum", String.valueOf(suiteInfo.getSuiteModuleNum()));
		param.put("caseNum", String.valueOf(suiteInfo.getSuiteCaseNum()));
		param.put("passNum", String.valueOf(suiteInfo.getPassCaseNum()));
		param.put("failNum", String.valueOf(suiteInfo.getFailCaseNum()));
		param.put("id", String.valueOf(this.logTestSuiteId));
		String dbLogUrl = DriverService.PROPERTIES.getProperty("dbLogUrl");
		try {
			String json = HttpUtil.postRequest(dbLogUrl
					+ "logTestSuiteAction!end.action", param, null, null);
			JSONObject result = JSON.parseObject(json);
			if (!result.getBoolean("result").booleanValue())
				throw new Exception(result.getString("msg"));
		} catch (Exception e) {
			LogModule.logStepFail(StepType.DATABASE,
					"测试套件（" + suiteInfo.getSuiteName() + "）结束运行时日志存储失败",
					RunResult.FAIL, e.getMessage());
		}
	}

	public void moduleRunStart(ModuleInfo moduleInfo) {
		Map param = new HashMap();
		String machineIp = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			machineIp = addr.getHostAddress().toString();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		param.put("machineIp", machineIp);
		param.put("logTestSuiteId", String.valueOf(this.logTestSuiteId));
		param.put("name", moduleInfo.getModuleName());
		param.put("runStatus", String.valueOf(moduleInfo.getModuleStatus()));
		param.put("runResult", String.valueOf(moduleInfo.getModuleResult()));
		String dbLogUrl = DriverService.PROPERTIES.getProperty("dbLogUrl");
		try {
			String json = HttpUtil.postRequest(dbLogUrl
					+ "logTestModuleAction!start.action", param, null, null);
			JSONObject result = JSON.parseObject(json);
			if (!result.getBoolean("result").booleanValue()) {
				throw new Exception(result.getString("msg"));
			}

			this.logTestModuleId = result.getString("msg");
		} catch (Exception e) {
			LogModule.logStepFail(StepType.DATABASE,
					"测试模块（" + moduleInfo.getModuleName() + "）开始运行时日志存储失败",
					RunResult.FAIL, e.getMessage());
		}
	}

	public void moduleRunStop(ModuleInfo moduleInfo) {
		Map param = new HashMap();
		param.put("runStatus", String.valueOf(moduleInfo.getModuleStatus()));
		param.put("runResult", String.valueOf(moduleInfo.getModuleResult()));
		param.put("caseNum", String.valueOf(moduleInfo.getModuleCaseNum()));
		param.put("passNum", String.valueOf(moduleInfo.getPassCaseNum()));
		param.put("failNum", String.valueOf(moduleInfo.getFailCaseNum()));
		param.put("id", String.valueOf(this.logTestModuleId));
		String dbLogUrl = DriverService.PROPERTIES.getProperty("dbLogUrl");
		try {
			String json = HttpUtil.postRequest(dbLogUrl
					+ "logTestModuleAction!end.action", param, null, null);
			JSONObject result = JSON.parseObject(json);
			if (!result.getBoolean("result").booleanValue())
				throw new Exception(result.getString("msg"));
		} catch (Exception e) {
			LogModule.logStepFail(StepType.DATABASE,
					"测试模块（" + moduleInfo.getModuleName() + "）结束运行时日志存储失败",
					RunResult.FAIL, e.getMessage());
		}
	}

	public void caseRunStart(CaseInfo caseInfo) {
		Map param = new HashMap();
		param.put("logTestSuiteId", String.valueOf(this.logTestSuiteId));
		param.put("logTestModuleId", String.valueOf(this.logTestModuleId));
		param.put("name", caseInfo.getCaseName());
		param.put("description", caseInfo.getCaseDesc());
		param.put("runStatus", String.valueOf(caseInfo.getCaseStatus()));
		param.put("runResult", String.valueOf(caseInfo.getCaseResult()));
		String dbLogUrl = DriverService.PROPERTIES.getProperty("dbLogUrl");
		try {
			String json = HttpUtil.postRequest(dbLogUrl
					+ "logTestCaseAction!start.action", param, null, null);
			JSONObject result = JSON.parseObject(json);
			if (!result.getBoolean("result").booleanValue()) {
				throw new Exception(result.getString("msg"));
			}

			this.logTestCaseId = result.getString("msg");
		} catch (Exception e) {
			LogModule.logStepFail(StepType.DATABASE,
					"测试用例（" + caseInfo.getCaseName() + "）开始运行时日志存储失败",
					RunResult.FAIL, e.getMessage());
		}
	}

	public void caseRunStop(CaseInfo caseInfo) {
		Map param = new HashMap();
		param.put("runStatus", String.valueOf(caseInfo.getCaseStatus()));
		param.put("runResult", String.valueOf(caseInfo.getCaseResult()));
		param.put("rerunNum", "");
		param.put("id", String.valueOf(this.logTestCaseId));
		String dbLogUrl = DriverService.PROPERTIES.getProperty("dbLogUrl");
		try {
			String json = HttpUtil.postRequest(dbLogUrl
					+ "logTestCaseAction!end.action", param, null, null);
			JSONObject result = JSON.parseObject(json);
			if (!result.getBoolean("result").booleanValue())
				throw new Exception(result.getString("msg"));
		} catch (Exception e) {
			LogModule.logStepFail(StepType.DATABASE,
					"测试用例（" + caseInfo.getCaseName() + "）结束运行时日志存储失败",
					RunResult.FAIL, e.getMessage());
		}

		List<LogStepInfo> logStepInfoList = caseInfo.getLogStepInfoList();
		for (LogStepInfo logStepInfo : logStepInfoList) {
			param.clear();
			String name = logStepInfo.getPicture();
			if ((name != null) && (!name.trim().equals(""))) {
				param.put("name", logStepInfo.getPictureName());
				try {
					String json = HttpUtil.uploadFile(new File(name), dbLogUrl
							+ "upload");
					JSONObject result = JSON.parseObject(json);
					if (!result.getBoolean("result").booleanValue())
						throw new Exception(result.getString("msg"));
				} catch (Exception e) {
					LogModule.logStepFail(StepType.DATABASE, "测试用例步骤截图（"
							+ logStepInfo.getPicture() + "）日志截图存储失败",
							RunResult.FAIL, e.getMessage());
				}
			} else {
				this.logPictureId = 0L;
			}

			param.clear();
			param.put("logTestSuiteId", String.valueOf(this.logTestSuiteId));
			param.put("logTestModuleId", String.valueOf(this.logTestModuleId));
			param.put("logTestCaseId", String.valueOf(this.logTestCaseId));
			param.put("stepId", String.valueOf(logStepInfo.getStepId()));
			param.put("name", String.valueOf(logStepInfo.getStepDesc()));
			param.put("comment", String.valueOf(logStepInfo.getUrl()));
			param.put("type", String.valueOf(logStepInfo.getStepType()));
			param.put("result", String.valueOf(logStepInfo.getStepResult()));

			param.put("logPictureId",
					String.valueOf(logStepInfo.getPictureName()));
			param.put("reason", logStepInfo.getFailReason());
			try {
				String today = DateUtil.dateToStr(new Date(), "yyyy-MM-dd");
				Date stepTime1 = DateUtil.strToDate(
						today + " " + logStepInfo.getStepTime(),
						"yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String stepTime = sf.format(stepTime1);
				param.put("stepTime", stepTime);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			try {
				String json = HttpUtil.postRequest(dbLogUrl
						+ "logTestStepAction!start.action", param, null, null);
				JSONObject result = JSON.parseObject(json);
				if (!result.getBoolean("result").booleanValue())
					throw new Exception(result.getString("msg"));
			} catch (Exception e) {
				LogModule.logStepFail(StepType.DATABASE, "测试用例步骤（"
						+ logStepInfo.getStepDesc() + "）日志存储失败",
						RunResult.FAIL, e.getMessage());
			}
		}
	}
}