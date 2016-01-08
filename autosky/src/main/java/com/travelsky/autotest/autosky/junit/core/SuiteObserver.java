package com.travelsky.autotest.autosky.junit.core;

import com.alibaba.fastjson.JSON;
import com.travelsky.autotest.autosky.junit.annotations.DataSource;
import com.travelsky.autotest.autosky.junit.enums.BrowserType;
import com.travelsky.autotest.autosky.junit.enums.DataSourceType;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.RunStatus;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.junit.enums.TestSuiteType;
import com.travelsky.autotest.autosky.junit.modules.CaseInfo;
import com.travelsky.autotest.autosky.junit.modules.LogStepInfo;
import com.travelsky.autotest.autosky.junit.modules.ModuleInfo;
import com.travelsky.autotest.autosky.junit.modules.SuiteInfo;
import com.travelsky.autotest.autosky.junit.rules.DriverService;
import com.travelsky.autotest.autosky.utils.ClassUtil;
import com.travelsky.autotest.autosky.utils.DateUtil;
import com.travelsky.autotest.autosky.utils.ExcelUtil;
import com.travelsky.autotest.autosky.utils.FileUtil;
import com.travelsky.autotest.autosky.utils.XmlUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dom4j.DocumentException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;

public class SuiteObserver implements Observer {
	private Properties properties = DriverService.PROPERTIES;

	private String logDir = DriverService.PROPERTIES.getProperty("logDir",
			"C:/autosky_log/");

	private String sourcesDir = DriverService.PROPERTIES.getProperty(
			"sourcesDir", "src/test/resources/");

	private SuiteInfo planedSuiteInfo = new SuiteInfo();

	public static Map<String, Map<String, String>> websiteMap = DriverService.PROPERTIES_MAP;

	public List<LogStepInfo> logStepInfoList = new ArrayList();

	List<Throwable> errors = new ArrayList();

	public void suiteRunStart(SuiteInfo suiteInfo) {
		suiteInfo.setSuiteResult(RunResult.RUNNING);
		suiteInfo.setSuiteStatus(RunStatus.RUNNING);
		Boolean suiteMerged = DriverService.SUITE_MERGED;
		suiteInfo.setReportMerged(suiteMerged);
		if (suiteMerged.booleanValue()) {
			suiteInfo.setSuiteName(this.properties.getProperty("project"));
		} else {
			suiteInfo.setSuiteName(DriverService.DESCRIPTION.getTestClass()
					.getSimpleName());
		}
		String runId = DriverService.PROPERTIES.getProperty("runId");
		if ((runId == null) || (runId.trim().equals(""))) {
			runId = "buildId:0:buildTaskId:0:buildTestSuiteId:0";
		}
		suiteInfo.setBuildId(Long.parseLong(runId.split(":")[1]));
		suiteInfo.setBuildTaskId(Long.parseLong(runId.split(":")[3]));
		suiteInfo.setBuildTestSuiteId(Long.parseLong(runId.split(":")[5]));

		suiteInfo.setSuiteStartTime(new Date());
		List moduleInfoList = new ArrayList();
		suiteInfo.setModuleInfoList(moduleInfoList);

		addSuiteModules(suiteInfo);//初始化suiteinfo中的moduleinfolist

		websiteMap = getWebsiteMap();

		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for (StackTraceElement ste : stack) {
			if (ste.getClassName().contains("maven")) {
				DriverService.ENCODE = "GBK";
				break;
			}
		}
		this.logDir = (this.logDir + suiteInfo.getSuiteName() + "/");
		String buildNumber = System.getProperty("build.number");
		if (buildNumber != null) {
			this.logDir = (this.logDir + buildNumber + "/");
		} else
			this.logDir = (this.logDir
					+ DateUtil.dateToStr(new Date(), "yyyy-MM-dd_HH-mm-ss") + "/");

		LogModule.SUITE_PATH = this.logDir;
		File directory = new File(this.logDir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		System.out.println("测试套件（" + suiteInfo.getSuiteName() + "）执行开始。");
	}

	public List<ModuleInfo> addSuiteModules(SuiteInfo suiteInfo) {
		List moduleInfoList = new ArrayList();
		Set classes = new HashSet();
		if (suiteInfo.getReportMerged().booleanValue()) {
			try {
				classes = ClassUtil.getClasses(getClass().getResource("/")
						.getPath().replaceFirst("/", "").replace("/", "\\")
						.replace("%20", " "));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			classes.add(DriverService.DESCRIPTION.getTestClass());

		for (Iterator i = classes.iterator(); i.hasNext();) {
			Class cls;
			cls = (Class) i.next();

			if (cls.getAnnotation(Ignore.class) == null) {
				List<String> moduleNameList = getSuiteModuleNameList(cls);
				for (String moduleName : moduleNameList) {
					ModuleInfo moduleInfo = new ModuleInfo();
					moduleInfo.setSuiteInfo(suiteInfo);
					moduleInfo.setModuleName(cls.getSimpleName() + "."
							+ moduleName);
					moduleInfo.setModuleRun(Boolean.valueOf(true));
					moduleInfo.setModuleStatus(RunStatus.WAITING);
					moduleInfo.setModuleResult(RunResult.WAITING);
					moduleInfoList.add(moduleInfo);
				}
			}
		}
		List planedModuleInfoList = mergePlanModuleInfo(moduleInfoList);
		suiteInfo.setModuleInfoList(planedModuleInfoList);
		return moduleInfoList;
	}

	public void moduleRunStart(ModuleInfo moduleInfo) {
		moduleInfo.setModuleName(DriverService.DESCRIPTION.getTestClass()
				.getSimpleName()
				+ "."
				+ DriverService.DESCRIPTION.getMethodName());
		moduleInfo.setModuleStatus(RunStatus.RUNNING);
		moduleInfo.setModuleResult(RunResult.RUNNING);
		moduleInfo.setModuleRun(Boolean.valueOf(true));
		moduleInfo.setModuleStartTime(new Date());
		addModuleCases(moduleInfo);
		updateSuiteInfo(LogModule.SUITE_INFO, moduleInfo);
		System.out.println("测试模块（" + moduleInfo.getModuleName() + "）执行开始。");
	}

	public void caseRunStart(CaseInfo caseInfo) {
		LogModule.logStepInfoList.clear();
		caseInfo.setCaseResult(RunResult.RUNNING);
		caseInfo.setCaseStatus(RunStatus.RUNNING);
		caseInfo.setCaseStartTime(new Date());
		updateModuleInfo(LogModule.MODULE_INFO, caseInfo);
		System.out.println("测试用例（" + caseInfo.getCaseName() + "）执行开始。");
	}

	public void suiteRunStop(SuiteInfo suiteInfo) {
		suiteInfo.setSuiteResult(RunResult.PASS);
		suiteInfo.setSuiteStatus(RunStatus.COMPLETED);
		for (ModuleInfo moduleInfo : suiteInfo.getModuleInfoList()) {
			if (moduleInfo.getModuleStatus() != RunStatus.COMPLETED) {
				suiteInfo.setSuiteStatus(RunStatus.RUNNING);
				suiteInfo.setSuiteResult(RunResult.RUNNING);
				break;
			}
		}
		if (suiteInfo.getSuiteStatus() == RunStatus.COMPLETED) {
			for (ModuleInfo moduleInfo : suiteInfo.getModuleInfoList()) {
				if (moduleInfo.getModuleResult() != RunResult.FAIL) {
					suiteInfo.setSuiteResult(RunResult.FAIL);
					break;
				}
			}

			System.out.println("测试套件（" + suiteInfo.getSuiteName() + "）执行结束。");
			suiteInfo.setSuiteStopTime(new Date());
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(2);
			float percent = 100.0F;
			if (suiteInfo.getSuiteCaseNum() > 0) {
				percent = Float.parseFloat(numberFormat.format(suiteInfo
						.getPassCaseNum()
						/ suiteInfo.getSuiteCaseNum()
						* 100.0F));
			}
			suiteInfo.setPassPercent(percent);
		}
	}

	public void moduleRunStop(ModuleInfo moduleInfo) {
		moduleInfo.setModuleStatus(RunStatus.COMPLETED);
		moduleInfo.setModuleResult(RunResult.PASS);
		moduleInfo.setModuleStopTime(new Date());
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		float percent = 100.0F;
		if (moduleInfo.getModuleCaseNum() > 0) {
			percent = Float
					.parseFloat(numberFormat.format(moduleInfo.getPassCaseNum()
							/ moduleInfo.getModuleCaseNum() * 100.0F));
		}
		moduleInfo.setPassPercent(percent);
		for (CaseInfo caseinfo : moduleInfo.getCaseInfoList()) {
			if (caseinfo.getCaseResult() == RunResult.FAIL) {
				moduleInfo.setModuleResult(RunResult.FAIL);
				break;
			}
		}
		updateSuiteInfo(LogModule.SUITE_INFO, moduleInfo);
		System.out.println("测试模块（" + moduleInfo.getModuleName() + "）执行结束。");
	}

	public void caseRunStop(CaseInfo caseInfo) {
		caseInfo.setCaseResult(RunResult.PASS);
		caseInfo.setCaseStopTime(new Date());
		caseInfo.setLogStepInfoList(LogModule.logStepInfoList);
		for (LogStepInfo logStepInfo : LogModule.logStepInfoList) {
			if (logStepInfo.getStepResult() == RunResult.FAIL) {
				caseInfo.setCaseResult(RunResult.FAIL);
				break;
			}
		}
		String logLevel = this.properties.getProperty("logLevel", "error");

		if ((logLevel.toLowerCase().trim().equals("error"))
				&& (caseInfo.getCaseResult() == RunResult.PASS)) {
			List neededLogStepInfoList = new ArrayList();
			int id = 0;
			for (LogStepInfo logStepInfo : LogModule.logStepInfoList) {
				if (logStepInfo.getStepType() == StepType.ASSERT) {
					id++;
					logStepInfo.setStepId(id);
					neededLogStepInfoList.add(logStepInfo);
				}
			}
			LogModule.logStepInfoList = neededLogStepInfoList;
		}
		caseInfo.setLogStepInfoList(LogModule.logStepInfoList);
		caseInfo.setCaseStatus(RunStatus.COMPLETED);
		System.out.println("测试用例（" + caseInfo.getCaseName() + "）执行结束。");
		caseInfo.setCaseRerunNum(caseInfo.getCaseRerunNum() - 1);
		if ((caseInfo.getCaseResult().equals(RunResult.PASS))
				|| (caseInfo.getCaseRerunNum() < 0))
			updateModuleInfo(LogModule.MODULE_INFO, caseInfo);
	}

	private Map<String, Map<String, String>> getWebsiteMap() {
		Map websiteInfoMap = new HashMap();
		Map map = new HashMap();
		Properties properties = new Properties();
		if (websiteMap.isEmpty()) {
			try {
				properties = FileUtil.getProperties("/webSite.properties");
			} catch (Exception e) {
				return map;
			}
			Iterator iter = properties.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String websiteKey = (String) entry.getKey();
				if ((websiteKey != null) && (!websiteKey.trim().equals(""))
						&& (websiteKey.contains("."))) {
					String websiteName = websiteKey.split("\\.")[0].trim();
					if (websiteMap.get(websiteName) == null) {
						websiteMap.put(websiteName, new HashMap());
					}
					String websiteInfoKey = websiteKey.split("\\.")[1].trim();
					String websiteInfoValue = ((String) entry.getValue())
							.trim();
					websiteInfoMap = (Map) websiteMap.get(websiteName);
					websiteInfoMap.put(websiteInfoKey, websiteInfoValue);
					map.put(websiteName, websiteInfoMap);
				}
			}
			websiteMap = map;
		}
		return map;
	}

	private Map<String, Map<String, String>> getPropertiesMap() {
		Map websiteInfoMap = new HashMap();
		Map map = new HashMap();
		Properties properties = new Properties();
		if (websiteMap.isEmpty()) {
			try {
				properties = FileUtil.getProperties("/webSite.properties");
			} catch (Exception e) {
				return map;
			}
			Iterator iter = properties.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String websiteKey = (String) entry.getKey();
				if ((websiteKey != null) && (!websiteKey.trim().equals(""))
						&& (websiteKey.contains("."))) {
					String[] aa = websiteKey.split("\\.");
					String websiteName = websiteKey.split("\\.")[0].trim();
					if (websiteMap.get(websiteName) == null) {
						websiteMap.put(websiteName, new HashMap());
					}
					String websiteInfoKey = websiteKey.split("\\.")[1].trim();
					String websiteInfoValue = ((String) entry.getValue())
							.trim();
					websiteInfoMap = (Map) websiteMap.get(websiteName);
					websiteInfoMap.put(websiteInfoKey, websiteInfoValue);

					map.put(websiteName, websiteInfoMap);
				}
			}
			websiteMap = map;
		}
		return map;
	}

	private List<ModuleInfo> mergePlanModuleInfo(List<ModuleInfo> moduleInfoList) {
		List planedModuleInfoList = new ArrayList();

		String testPlan = this.properties.getProperty("plan", "create");
		if (testPlan.trim().toLowerCase().equals("error")) {
			System.out.println("提示：本次计划执行的是plan.error.xml中的用例.");
			try {
				this.planedSuiteInfo = XmlUtil.parsePlanXml(this.sourcesDir
						+ "plan.error.xml");
			} catch (DocumentException e) {
				this.planedSuiteInfo = new SuiteInfo();
			}
			if ((this.planedSuiteInfo.getModuleInfoList() == null)
					|| (this.planedSuiteInfo.getModuleInfoList().size() == 0)) {
				System.out.println("提示：plan.error.xml中无用例数据.");
				this.planedSuiteInfo.setModuleInfoList(planedModuleInfoList);
				return planedModuleInfoList;
			}
		} else if (testPlan.trim().toLowerCase().equals("all")) {
			System.out.println("提示：本次计划执行的是plan.xml中的用例.");
			try {
				this.planedSuiteInfo = XmlUtil.parsePlanXml(this.sourcesDir
						+ "plan.xml");
			} catch (DocumentException e) {
				this.planedSuiteInfo = new SuiteInfo();
			}
			if ((this.planedSuiteInfo.getModuleInfoList() == null)
					|| (this.planedSuiteInfo.getModuleInfoList().size() == 0)) {
				System.out.println("提示：由于您未手动增加plan.xml,默认执行所有.");
				this.planedSuiteInfo.setModuleInfoList(planedModuleInfoList);
				return moduleInfoList;
			}
		} else {
			if (testPlan.contains("{")) {
				SuiteInfo suite = (SuiteInfo) JSON.parseObject(testPlan,
						SuiteInfo.class);
				this.planedSuiteInfo.setModuleInfoList(suite
						.getModuleInfoList());
				return suite.getModuleInfoList();
			}
			this.planedSuiteInfo.setModuleInfoList(moduleInfoList);
			return moduleInfoList;
		}
		ModuleInfo planedModuleInfo;
		for (Iterator i = this.planedSuiteInfo.getModuleInfoList().iterator(); i
				.hasNext();) {
			planedModuleInfo = (ModuleInfo) i.next();
			if (planedModuleInfo.getModuleRun().booleanValue())
				for (ModuleInfo moduleInfo : moduleInfoList)
					if (moduleInfo.getModuleName().equals(
							planedModuleInfo.getModuleName())) {
						planedModuleInfoList.add(moduleInfo);
						break;
					}
		}
		return planedModuleInfoList;
	}

	public List<CaseInfo> addModuleCases(ModuleInfo moduleInfo) {
		List datas = getDatas(DriverService.DESCRIPTION);
		List caseInfoList = new ArrayList();
		if (datas.size() == 0) {
			Map data = new HashMap();
			data.put("caseName", "用例一");
			data.put("caseDescription", "用例一描述信息");
			datas.add(data);
		}
		for (Iterator i = datas.iterator(); i.hasNext();) {
			Map data;
			data = (Map) i.next();
			String website = this.properties.getProperty("website");
			if ((website != null) && (!websiteMap.isEmpty())) {
				Map websiteInfo = (Map) websiteMap.get(website);
				if ((websiteInfo != null) && (!websiteInfo.isEmpty())) {
					data.putAll(websiteInfo);
				}

			}

			List<Map<String, String>> browserTypeCases = new ArrayList();
			if ((this.properties.getProperty("testSuiteType") != null)
					&& (!this.properties.getProperty("testSuiteType").trim()
							.equals(""))
					&& (TestSuiteType.valueOf(this.properties
							.getProperty("testSuiteType")) != TestSuiteType.WEB_UI)) {
				data.remove("browserType");
				System.out
						.println("test.properties中设置了客户端程序，因此移除了browserType属性。");
				browserTypeCases.add(data);
			} else {
				browserTypeCases = getBrowserTypeCases(data);
			}
			for (Map browserTypeCase : browserTypeCases) {
				CaseInfo caseInfo = new CaseInfo();
				String caseName = (String) data.get("caseName");
				if (caseName != null) {
					Pattern pa = Pattern.compile("\\s*|\t|\r|\n");
					Matcher m = pa.matcher(caseName);

					caseName = m.replaceAll("");
				} else {
					caseName = "无用例名称";
				}
				if ((this.properties.getProperty("testSuiteType") == null)
						|| (this.properties.getProperty("testSuiteType").trim()
								.equals(""))
						|| (TestSuiteType.valueOf(this.properties
								.getProperty("testSuiteType")) == TestSuiteType.WEB_UI)) {
					caseName = caseName + "_"
							+ (String) browserTypeCase.get("browserType");
					caseInfo.setCaseBrowserType(BrowserType
							.valueOf((String) browserTypeCase
									.get("browserType")));
				}
				caseInfo.setModuleInfo(moduleInfo);
				caseInfo.setSuiteInfo(moduleInfo.getSuiteInfo());
				caseInfo.setCaseName(caseName);
				caseInfo.setCaseAssert(data.get("caseAssert") == null);
				caseInfo.setCaseDesc((String) data.get("caseDescription"));
				caseInfo.setCaseRun(Boolean.valueOf((data.get("caseRun") == null)
						|| ("Y".equals(((String) data.get("caseRun"))
								.toUpperCase().trim()))));
				caseInfo.setCasePriority((String) data.get("casePriority"));
				String caseRerunNum = (this.properties
						.getProperty("caseRerunNum") == null)
						|| (this.properties.getProperty("caseRerunNum").trim()
								.equals("")) ? "0" : this.properties
						.getProperty("caseRerunNum");
				caseInfo.setCaseRerunNum(Integer.parseInt(caseRerunNum));
				caseInfo.setCaseResult(RunResult.WAITING);
				caseInfo.setCaseStatus(RunStatus.WAITING);
				caseInfo.setCaseParams(data);
				caseInfo.setModuleInfo(moduleInfo);
				caseInfoList.add(caseInfo);
			}
		}
		Map data;
		moduleInfo.setCaseInfoList(caseInfoList);
		List planedCaseInfoList = mergePlanCaseInfo(moduleInfo);
		moduleInfo.setCaseInfoList(planedCaseInfoList);
		return caseInfoList;
	}

	private List<CaseInfo> mergePlanCaseInfo(ModuleInfo moduleInfo) {
		List planedCaseInfoList = new ArrayList();
		ModuleInfo planedModuleInfo = null;
		if (this.planedSuiteInfo.getModuleInfoList().size() == 0)
			return moduleInfo.getCaseInfoList();
		for (ModuleInfo module : this.planedSuiteInfo.getModuleInfoList()) {
			if (moduleInfo.getModuleName().equals(module.getModuleName())) {
				planedModuleInfo = module;
				break;
			}
		}
		if (planedModuleInfo == null) {
			return planedCaseInfoList;
		}
		if ((planedModuleInfo.getCaseInfoList() == null)
				|| (planedModuleInfo.getCaseInfoList().size() == 0)) {
			return moduleInfo.getCaseInfoList();
		}
		CaseInfo planedCaseInfo;
		for (Iterator i$ = planedModuleInfo.getCaseInfoList().iterator(); i$
				.hasNext();) {
			planedCaseInfo = (CaseInfo) i$.next();
			if ((planedCaseInfo.getCaseRun() == null)
					|| (planedCaseInfo.getCaseRun().booleanValue()))
				for (CaseInfo caseInfo : moduleInfo.getCaseInfoList())
					if (caseInfo.getCaseName().toUpperCase()
							.equals(planedCaseInfo.getCaseName().toUpperCase())) {
						planedCaseInfoList.add(caseInfo);
						break;
					}
		}

		return planedCaseInfoList;
	}

	private List<Map<String, String>> getBrowserTypeCases(
			Map<String, String> data) {
		List browserTypeCases = new ArrayList();
		if (data.get("browserType") != null) {
			String[] types = ((String) data.get("browserType")).split(",");
			for (String type : types) {
				Map browserTypeCase = new HashMap();
				browserTypeCase.putAll(data);
				if (type.trim().toUpperCase().equals("IE")) {
					browserTypeCase.put("browserType", "IE");
				} else if ((type.trim().toUpperCase().equals("FF"))
						|| (type.toUpperCase().equals("FIREFOX"))) {
					browserTypeCase.put("browserType", "FIREFOX");
				} else if (type.trim().toUpperCase().equals("CHROME")) {
					browserTypeCase.put("browserType", "CHROME");
				} else if (type.trim().equals("")) {
					browserTypeCase.put("browserType", "IE");
				} else {
					System.err
							.println("设置的浏览器类型为:"
									+ type
									+ "，格式有问题！请参照：单个浏览器如IE；多个浏览器用英文逗号分隔，如IE,FF,CHROME。");
					throw new RuntimeException(
							"数据池中的browserType格式有问题，请改正！\n设置的浏览器类型为:"
									+ type
									+ "，格式有问题！请参照：单个浏览器如IE；多个浏览器用英文逗号分隔，如IE,FF,CHROME。");
				}
				browserTypeCases.add(browserTypeCase);
			}
		} else {
			data.put("browserType", "IE");
			browserTypeCases.add(data);
		}
		return browserTypeCases;
	}

	public void updateSuiteInfo(SuiteInfo suiteInfo,
			ModuleInfo currentModuleInfo) {
		List moduleInfoList = suiteInfo.getModuleInfoList();
		for (int i = 0; i < moduleInfoList.size(); i++) {
			if (((ModuleInfo) moduleInfoList.get(i)).getModuleName().equals(
					currentModuleInfo.getModuleName())) {
				if (currentModuleInfo.getModuleStatus() == RunStatus.COMPLETED) {
					suiteInfo.setPassCaseNum(suiteInfo.getPassCaseNum()
							+ currentModuleInfo.getPassCaseNum());
					suiteInfo.setFailCaseNum(suiteInfo.getFailCaseNum()
							+ currentModuleInfo.getFailCaseNum());
					suiteInfo.setSuiteCaseNum(suiteInfo.getSuiteCaseNum()
							+ currentModuleInfo.getModuleCaseNum());
					suiteInfo
							.setSuiteModuleNum(suiteInfo.getSuiteModuleNum() + 1);
				}
				moduleInfoList.set(i, currentModuleInfo);
				break;
			}
		}
		suiteInfo.setModuleInfoList(moduleInfoList);
		LogModule.SUITE_INFO = suiteInfo;
	}

	public void updateModuleInfo(ModuleInfo moduleInfo, CaseInfo currentCaseInfo) {
		List caseInfoList = moduleInfo.getCaseInfoList();
		for (int i = 0; i < caseInfoList.size(); i++) {
			if (((CaseInfo) caseInfoList.get(i)).getCaseName().equals(
					currentCaseInfo.getCaseName())) {
				if (currentCaseInfo.getCaseResult() == RunResult.PASS) {
					moduleInfo.setPassCaseNum(moduleInfo.getPassCaseNum() + 1);
					moduleInfo
							.setModuleCaseNum(moduleInfo.getModuleCaseNum() + 1);
				} else if (currentCaseInfo.getCaseResult() == RunResult.FAIL) {
					moduleInfo.setFailCaseNum(moduleInfo.getFailCaseNum() + 1);
					moduleInfo
							.setModuleCaseNum(moduleInfo.getModuleCaseNum() + 1);
				}
				caseInfoList.set(i, currentCaseInfo);
				break;
			}
		}
		moduleInfo.setCaseInfoList(caseInfoList);
		LogModule.MODULE_INFO = moduleInfo;
	}

	private List<String> getSuiteModuleNameList(Class<?> Clazz) {
		List moduleNameList = new ArrayList();
		Method[] methods = Clazz.getMethods();

		for (Method method : methods) {
			String methodName = method.getName();
			if ((method.isAnnotationPresent(Test.class))
					&& (method.getAnnotation(Ignore.class) == null)) {
				moduleNameList.add(methodName);
			}
		}
		return moduleNameList;
	}

	// 根据注解获取测试数据
	public List<Map<String, String>> getDatas(Description des) {
		List datas = new ArrayList();
		if (des.getAnnotation(DataSource.class) == null) {
			return datas;
		}
		DataSourceType type = ((DataSource) des.getAnnotation(DataSource.class))
				.type();
		if (type == null) {
			return datas;
		}
		String filePath = ((DataSource) des.getAnnotation(DataSource.class))
				.file().toString().trim();
		/* 根据指定路径和默认文件名找到该文件 */
		filePath = this.sourcesDir + des.getTestClass().getSimpleName() + "/"
				+ filePath;
		switch (type.ordinal()) {
		case 1:
			String sheetName = ((DataSource) des
					.getAnnotation(DataSource.class)).sheetName().toString()
					.trim();
			ExcelUtil excelUtil = new ExcelUtil();
			datas = excelUtil.excelDatas(filePath, sheetName);
			break;
		case 2:
			datas = XmlUtil.xmlDatas(filePath);
			break;
		case 3:
			break;
		case 4:
			break;
		}

		return datas;
	}

	private static Map<String, String> getTestMethods(Class<?> Clazz) {
		Method[] methods = Clazz.getMethods();
		Map map = new LinkedHashMap();
		int i = 0;
		for (Method method : methods) {
			String methodName = method.getName();
			if ((method.isAnnotationPresent(BeforeClass.class))
					|| (method.isAnnotationPresent(AfterClass.class))) {
				map.put(method.getAnnotations()[0].annotationType()
						.getSimpleName(), methodName);
			} else if (method.isAnnotationPresent(Test.class)) {
				map.put(String.valueOf(i), methodName);
				i++;
			}
		}
		return map;
	}
}