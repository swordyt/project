package com.travelsky.autotest.autosky.junit.rules;

import com.travelsky.autotest.autosky.browser.Browser;
import com.travelsky.autotest.autosky.database.DBUtil;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.DriverModule;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.core.PlanObserver;
import com.travelsky.autotest.autosky.junit.core.SuiteObserver;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.RunStatus;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.junit.enums.TestSuiteType;
import com.travelsky.autotest.autosky.junit.log.DatabaseLog;
import com.travelsky.autotest.autosky.junit.log.LocalFileLog;
import com.travelsky.autotest.autosky.junit.modules.CaseInfo;
import com.travelsky.autotest.autosky.junit.modules.ModuleInfo;
import com.travelsky.autotest.autosky.junit.modules.SuiteInfo;
import com.travelsky.autotest.autosky.utils.FileUtil;
import com.travelsky.autotest.autosky.window.Application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

public class DriverService implements TestRule {
	public static String PLAN_VM_CONTENT = "<?xml version=\"1.0\" encoding=\"$!encode\"?>\n<suite name=\"$!suiteInfo.suiteName\">\n#foreach ($!moduleInfo in $!suiteInfo.moduleInfoList)\n<module  run=\"$!moduleInfo.moduleRun\" name=\"$!moduleInfo.moduleName\">\n#foreach ($caseInfo in $moduleInfo.caseInfoList)\n<case  run=\"$!caseInfo.caseRun\" name=\"$!caseInfo.caseName\"> \n</case>\n#end\n </module>\n#end\n</suite>";
	public static String ENCODE = "GBK";
	// test.properties 系統属性
	public static Properties PROPERTIES = FileUtil.getProperties();

	public static Map<String, Map<String, String>> PROPERTIES_MAP = new HashMap();
	public static Description DESCRIPTION = null;
	public static Statement STATEMENT = null;
	public static Boolean SUITE_MERGED = Boolean.valueOf(System
			.getProperty("suiteMerged") == null ? "false" : System.getProperty(
			"suiteMerged").toLowerCase());
	public Map<String, String> param = new HashMap();
	public Map<String, Map<String, String>> propertiesMap = new HashMap();
	private List<Throwable> errors = new ArrayList();
	private DriverModule driverModule = DriverModule.getInstance();

	public Statement apply(final Statement base, final Description des) {
		return new Statement() {
			public void evaluate() throws Throwable {
				DriverService.DESCRIPTION = des;
				DriverService.STATEMENT = base;
				if ((LogModule.SUITE_INFO.getSuiteName() == null)
						|| ((!DriverService.SUITE_MERGED.booleanValue()) && (!des
								.getTestClass().getSimpleName()
								.equals(LogModule.SUITE_INFO.getSuiteName())))) {
					Properties props = System.getProperties();
					if ((System.getProperty("website") == null)
							|| (System.getProperty("website").trim().equals(""))) {
						props.remove("website");
					}
					if ((System.getProperty("casePriority") == null)
							|| (System.getProperty("casePriority").trim()
									.equals(""))) {
						props.remove("casePriority");
					}
					System.setProperties(props);
					DriverService.PROPERTIES.putAll(System.getProperties());
					DriverService.this.driverModule.detachAll();
					DriverService.this.driverModule.attach(new SuiteObserver());// DriverService->DriverModule->SuiteObserver
					DriverService.this.driverModule.attach(new PlanObserver());// DriverService->DriverModule->PlanObserver
					// 检查存储的日志类别ALL (DATABASE 本地)
					if (DriverService.PROPERTIES.getProperty("logType") != null) {
						if (DriverService.PROPERTIES.getProperty("logType")
								.toUpperCase().equals("ALL")) {
							DriverService.this.driverModule
									.attach(new DatabaseLog());// DriverService->DriverModule->DatabaseLog
							DriverService.this.driverModule
									.attach(new LocalFileLog());// DriverService->DriverModule->LocalFileLog
						} else if (DriverService.PROPERTIES
								.getProperty("logType").toUpperCase()
								.equals("DATABASE")) {
							DriverService.this.driverModule
									.attach(new DatabaseLog());
						} else {
							DriverService.this.driverModule
									.attach(new LocalFileLog());
						}
					} else {
						DriverService.this.driverModule
								.attach(new LocalFileLog());
					}
					// 运行日志套件
					LogModule.SUITE_INFO = new SuiteInfo();// DriverService->SuiteInfo
					DriverService.this.driverModule
							.suiteRunStart(LogModule.SUITE_INFO);// SuiteInfo让上面生成的测试套件全部运行
				}
				for (ModuleInfo planedModuleInfo : LogModule.SUITE_INFO
						.getModuleInfoList())
					if (planedModuleInfo.getModuleName().equals(
							des.getTestClass().getSimpleName() + "."
									+ des.getMethodName())) {
						LogModule.MODULE_INFO = new ModuleInfo();
						DriverService.this.driverModule
								.moduleRunStart(LogModule.MODULE_INFO);
						List caseInfoList = LogModule.MODULE_INFO
								.getCaseInfoList();
						List<CaseInfo> caseInfoListUnrun = new ArrayList();

						for (int i = 0; i < caseInfoList.size(); i++) {
							CaseInfo caseInfo = (CaseInfo) caseInfoList.get(i);

							if ((caseInfo.getCaseRun().booleanValue())
									&& ((caseInfo.getCasePriority() == null)
											|| (DriverService.PROPERTIES
													.getProperty("casePriority") == null)
											|| (DriverService.PROPERTIES
													.getProperty("casePriority")
													.trim().equals("")) || (DriverService.PROPERTIES
											.getProperty("casePriority")
											.toLowerCase().contains(caseInfo
											.getCasePriority().toLowerCase())))) {
								if ((DriverService.PROPERTIES
										.getProperty("testSuiteType") == null)
										|| (DriverService.PROPERTIES
												.getProperty("testSuiteType")
												.trim().equals(""))
										|| (TestSuiteType
												.valueOf(DriverService.PROPERTIES
														.getProperty(
																"testSuiteType")
														.toUpperCase()) == TestSuiteType.WEB_UI)) {
									Browser.currentBrowserType = caseInfo
											.getCaseBrowserType();
									Browser.closeAll();
									Browser.killBrowserProcess(caseInfo
											.getCaseBrowserType());
								} else if (TestSuiteType
										.valueOf(DriverService.PROPERTIES
												.getProperty("testSuiteType")
												.toUpperCase()) == TestSuiteType.WINDOWS_UI) {
									Application.closeAllPID();
									Application.closeAllProcess();
								}
								LogModule.CASE_INFO = caseInfo;
								DriverService.this.driverModule
										.caseRunStart(LogModule.CASE_INFO);
								DriverService.this.param = caseInfo
										.getCaseParams();
								DriverService.this.propertiesMap = DriverService.PROPERTIES_MAP;
								DriverService.this
										.assertConfig(DriverService.this.param);
								try {
									DriverService.this.driverModule
											.initialData(LogModule.CASE_INFO);
									base.evaluate();
								} catch (StepException e) {
									String error = "测试用例（ "
											+ (String) DriverService.this.param
													.get("caseName")
											+ " ）执行失败! \n";
									DriverService.this.errors
											.add(new Throwable(error, e));
								} catch (RuntimeException e) {
									String error = "测试用例（ "
											+ (String) DriverService.this.param
													.get("caseName")
											+ " ）执行失败! \n";
									LogModule.logStepFail(StepType.CUSTOM,
											DriverService.this.errorMessage(e)
													+ "语句执行错误", RunResult.FAIL,
											"出错原因为"
													+ e.getClass()
															.getSimpleName()
													+ ":" + e.getMessage()
													+ "!");
									DriverService.this.errors
											.add(new Throwable(error, e));
								} finally {
									DriverService.this.driverModule
											.caseRunStop(LogModule.CASE_INFO);

									if ((caseInfo.getCaseResult()
											.equals(RunResult.FAIL))
											&& (caseInfo.getCaseRerunNum() >= 0))
										i--;
									try {
										Browser.closeAll();
									} catch (Exception e) {
									}
									DBUtil.closeAllConnection();
								}
							} else {
								System.out.println("测试用例（"
										+ caseInfo.getCaseName() + "）被设置成不执行。");
								caseInfoListUnrun.add(caseInfo);
							}
						}
						List caseInfoListRuned = LogModule.MODULE_INFO
								.getCaseInfoList();
						for (CaseInfo caseInfo : caseInfoListUnrun) {
							caseInfoListRuned.remove(caseInfo);
						}
						LogModule.MODULE_INFO
								.setCaseInfoList(caseInfoListRuned);
						DriverService.this.driverModule
								.moduleRunStop(LogModule.MODULE_INFO);

						Boolean suiteRunCompleted = Boolean.valueOf(true);
						for (ModuleInfo moduleInfo : LogModule.SUITE_INFO
								.getModuleInfoList()) {
							if (moduleInfo.getModuleStatus() != RunStatus.COMPLETED) {
								suiteRunCompleted = Boolean.valueOf(false);
								break;
							}
						}
						if (suiteRunCompleted.booleanValue()) {
							DriverService.this.driverModule
									.suiteRunStop(LogModule.SUITE_INFO);
							MultipleFailureException
									.assertEmpty(DriverService.this.errors);
							break;
						}
					}
			}
		};
	}

	private String errorMessage(RuntimeException e) {
		String message = "";
		for (StackTraceElement ee : e.getStackTrace()) {
			message = message + ee.toString() + "\n";
			if (ee.toString().contains("com.travelsky.")) {
				break;
			}
		}
		return message;
	}

	private void assertConfig(Map<String, String> param) {
		if ((param.get("caseAssert") != null)
				&& (((String) param.get("caseAssert")).trim().equals("N"))) {
			PROPERTIES.put("caseAssert", "false");
			return;
		}

		if ((param.get("caseAssert") != null)
				&& (!((String) param.get("caseAssert")).trim().equals("Y"))
				&& (PROPERTIES.get("caseAssert") != null)
				&& ((PROPERTIES.get("caseAssert").toString().trim().equals("N")) || (PROPERTIES
						.get("caseAssert").toString().trim().equals("false")))) {
			PROPERTIES.put("caseAssert", "false");
			return;
		}
		PROPERTIES.put("caseAssert", "true");
	}
}