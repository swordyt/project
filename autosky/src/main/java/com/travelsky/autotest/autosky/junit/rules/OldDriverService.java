package com.travelsky.autotest.autosky.junit.rules;

import com.alibaba.fastjson.JSON;
import com.travelsky.autotest.autosky.browser.Browser;
import com.travelsky.autotest.autosky.database.DBUtil;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.annotations.DataSource;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.BrowserType;
import com.travelsky.autotest.autosky.junit.enums.DataSourceType;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.RunStatus;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.junit.modules.CaseInfo;
import com.travelsky.autotest.autosky.junit.modules.LogStepInfo;
import com.travelsky.autotest.autosky.junit.modules.ModuleInfo;
import com.travelsky.autotest.autosky.junit.modules.SuiteInfo;
import com.travelsky.autotest.autosky.utils.ClassUtil;
import com.travelsky.autotest.autosky.utils.DateUtil;
import com.travelsky.autotest.autosky.utils.ExcelUtil;
import com.travelsky.autotest.autosky.utils.FileUtil;
import com.travelsky.autotest.autosky.utils.XmlUtil;
import com.travelsky.autotest.autosky.utils.ZipUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

public class OldDriverService
  implements TestRule
{
  private static String SOURCES_DIR = "src/test/resources/";
  private static String TEMPLATES_DIR = "src/main/resources/templates/";
  private static String PLAN_VM_CONTENT = "<?xml version=\"1.0\" encoding=\"$!encode\"?>\n<suite name=\"$!suiteInfo.suiteName\">\n#foreach ($!moduleInfo in $!suiteInfo.moduleInfoList)\n<module  run=\"$!moduleInfo.moduleRun\" name=\"$!moduleInfo.moduleName\">\n#foreach ($caseInfo in $moduleInfo.caseInfoList)\n<case  run=\"$!caseInfo.caseRun\" name=\"$!caseInfo.caseName\"> \n</case>\n#end\n </module>\n#end\n</suite>";
  private static String LOG_DIR = "C:/autosky_log/";
  private static String MODULE_DIR = "";
  private static String CASE_DIR = "";
  private static String encode = "utf-8";
  private static SuiteInfo planedSuiteInfo = new SuiteInfo();
  public static Map<String, Map<String, String>> websiteMap = new HashMap();
  private static Properties properties = FileUtil.getProperties();
  public Map<String, String> param = new HashMap();
  public Map<String, String> globalParam = new HashMap();
  public List<LogStepInfo> logStepInfoList = new ArrayList();
  List<Throwable> errors = new ArrayList();
  static Boolean suiteMerged = Boolean.valueOf(System.getProperty("suiteMerged") == null ? "false" : System.getProperty("suiteMerged").toLowerCase());

  public Statement apply(final Statement base, final Description des)
  {
    return new Statement()
    {
      public void evaluate() throws Throwable {
        Set a = ClassUtil.getClasses(getClass().getResource("/").getPath().replaceFirst("/", "").replace("/", "\\"));
        if ((LogModule.SUITE_INFO.getSuiteName() == null) || ((!OldDriverService.suiteMerged.booleanValue()) && (!des.getTestClass().getSimpleName().equals(LogModule.SUITE_INFO.getSuiteName()))))
        {
          OldDriverService.this.suiteRunStart(LogModule.SUITE_INFO, base, des);
        }

        for (ModuleInfo planedModuleInfo : LogModule.SUITE_INFO.getModuleInfoList())
          if (planedModuleInfo.getModuleName().equals(des.getTestClass().getSimpleName() + "." + des.getMethodName())) {
            OldDriverService.this.moduleRunStart(LogModule.MODULE_INFO, base, des);
            List caseInfoList = LogModule.MODULE_INFO.getCaseInfoList();
            List<CaseInfo> caseInfoListUnrun = new ArrayList();

            for (int i = 0; i < caseInfoList.size(); i++)
            {
              CaseInfo caseInfo = (CaseInfo)caseInfoList.get(i);
              if ((caseInfo.getCaseRun().booleanValue()) && ((caseInfo.getCasePriority() == null) || (OldDriverService.properties.getProperty("casePriority") == null) || (OldDriverService.properties.getProperty("casePriority").trim().equals("")) || (OldDriverService.properties.getProperty("casePriority").toLowerCase().contains(caseInfo.getCasePriority().toLowerCase()))))
              {
                Browser.currentBrowserType = caseInfo.getCaseBrowserType();
                Browser.killBrowserProcess(caseInfo.getCaseBrowserType());
                LogModule.CASE_INFO = caseInfo;
                OldDriverService.this.caseRunStart(LogModule.CASE_INFO, base, des);
                OldDriverService.this.param = caseInfo.getCaseParams();
                try {
                  base.evaluate();
                } catch (StepException e) {
                  String error = "测试用例（ " + (String)OldDriverService.this.param.get("caseName") + " ）执行失败! \n";
                  OldDriverService.this.errors.add(new Throwable(error, e));
                } catch (RuntimeException e) {
                  String error = "测试用例（ " + (String)OldDriverService.this.param.get("caseName") + " ）执行失败! \n";
                  LogModule.logStepFail(StepType.CUSTOM, OldDriverService.this.errorMessage(e) + "语句执行错误", RunResult.FAIL, "出错原因为" + e.getClass().getSimpleName() + ":" + e.getMessage() + "!");

                  OldDriverService.this.errors.add(new Throwable(error, e));
                } finally {
                  OldDriverService.this.caseRunStop(LogModule.CASE_INFO, base, des);
                  if ((caseInfo.getCaseResult().equals(RunResult.FAIL)) && (caseInfo.getCaseRerunNum() >= 0))
                    i--;
                  try
                  {
                    Browser.closeAll();
                  } catch (Exception e) {
                  }
                  DBUtil.closeAllConnection();
                }
              } else {
                System.out.println("测试用例（" + caseInfo.getCaseName() + "）被设置成不执行。");
                caseInfoListUnrun.add(caseInfo);
              }
            }
            List caseInfoListRuned = LogModule.MODULE_INFO.getCaseInfoList();
            for (CaseInfo caseInfo : caseInfoListUnrun) {
              caseInfoListRuned.remove(caseInfo);
            }
            LogModule.MODULE_INFO.setCaseInfoList(caseInfoListRuned);

            OldDriverService.this.moduleRunStop(LogModule.MODULE_INFO, base, des);
            Boolean suiteRunCompleted = Boolean.valueOf(false);
            for (ModuleInfo moduleInfo : LogModule.SUITE_INFO.getModuleInfoList()) {
              if (moduleInfo.getModuleStatus() != RunStatus.COMPLETED) {
                suiteRunCompleted = Boolean.valueOf(false);
                break;
              }
              suiteRunCompleted = Boolean.valueOf(true);
            }

            if (!suiteRunCompleted.booleanValue()) break;
            OldDriverService.this.suiteRunStop(LogModule.SUITE_INFO, base, des);
            MultipleFailureException.assertEmpty(OldDriverService.this.errors); break;
          }
      }
    };
  }

  private String errorMessage(RuntimeException e)
  {
    String message = "";
    for (StackTraceElement ee : e.getStackTrace()) {
      message = message + ee.toString() + "\n";
      if (ee.toString().contains("com.travelsky."))
      {
        break;
      }
    }
    return message;
  }

  private String getExceptionMessage(Exception e) {
    String message = "";
    e.printStackTrace();
    for (StackTraceElement ste : e.getStackTrace()) {
      message = message + ste.toString() + "\n";
    }
    return message;
  }

  public List<Map<String, String>> getDatas(Statement base, Description des) {
    List datas = new ArrayList();
    if (des.getAnnotation(DataSource.class) == null) {
      return datas;
    }
    DataSourceType type = ((DataSource)des.getAnnotation(DataSource.class)).type();
    if (type == null) {
      return datas;
    }
    String filePath = ((DataSource)des.getAnnotation(DataSource.class)).file().toString().trim();
    Properties properties = FileUtil.getProperties();
    SOURCES_DIR = properties.getProperty("sourcesDir", SOURCES_DIR);

    filePath = SOURCES_DIR + des.getTestClass().getSimpleName() + "/" + filePath;
    switch (type.ordinal()) {
    case 1:
      String sheetName = ((DataSource)des.getAnnotation(DataSource.class)).sheetName().toString().trim();
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

  public void suiteRunStart(SuiteInfo suiteInfo, Statement base, Description des)
  {
    if ((System.getProperty("website") == null) || (System.getProperty("website").trim().equals(""))) {
      Properties props = System.getProperties();
      props.remove("website");
      System.setProperties(props);
    }
    properties.putAll(System.getProperties());

    suiteInfo = new SuiteInfo();
    suiteInfo.setSuiteResult(RunResult.RUNNING);
    suiteInfo.setSuiteStatus(RunStatus.RUNNING);

    suiteInfo.setReportMerged(suiteMerged);
    if (suiteMerged.booleanValue())
    {
      suiteInfo.setSuiteName(properties.getProperty("project"));
    }
    else {
      suiteInfo.setSuiteName(des.getTestClass().getSimpleName());
    }

    suiteInfo.setSuiteStartTime(new Date());
    List moduleInfoList = new ArrayList();
    suiteInfo.setModuleInfoList(moduleInfoList);

    addSuiteModules(suiteInfo, base, des);
    LogModule.SUITE_INFO = suiteInfo;
    websiteMap = getWebsiteMap();
    System.out.println("测试套件（" + suiteInfo.getSuiteName() + "）执行开始。");

    suiteRunStartLog(suiteInfo);
  }

  public void suiteRunStop(SuiteInfo suiteInfo, Statement base, Description des)
  {
    suiteInfo.setSuiteResult(RunResult.PASS);
    suiteInfo.setSuiteStatus(RunStatus.COMPLETED);
    for (ModuleInfo moduleInfo : suiteInfo.getModuleInfoList()) {
      if (moduleInfo.getModuleStatus() != RunStatus.COMPLETED)
      {
        suiteInfo.setSuiteStatus(RunStatus.RUNNING);
        suiteInfo.setSuiteResult(RunResult.RUNNING);
        break;
      }
    }

    if (suiteInfo.getSuiteStatus() == RunStatus.COMPLETED) {
      for (ModuleInfo moduleInfo : suiteInfo.getModuleInfoList()) {
        if (moduleInfo.getModuleResult() != RunResult.FAIL)
        {
          suiteInfo.setSuiteResult(RunResult.FAIL);
          break;
        }

      }

      intialPlanError();

      System.out.println("测试套件（" + suiteInfo.getSuiteName() + "）执行结束。");
      suiteInfo.setSuiteStopTime(new Date());
      NumberFormat numberFormat = NumberFormat.getInstance();
      numberFormat.setMaximumFractionDigits(2);
      float percent = 100.0F;
      if (suiteInfo.getSuiteCaseNum() > 0) {
        percent = Float.parseFloat(numberFormat.format(suiteInfo.getPassCaseNum() / suiteInfo.getSuiteCaseNum() * 100.0F));
      }
      suiteInfo.setPassPercent(percent);

      suiteRunStopLog(suiteInfo);
      LogModule.SUITE_INFO = new SuiteInfo();
    }
  }

  private Map<String, Map<String, String>> getWebsiteMap()
  {
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
        Map.Entry entry = (Map.Entry)iter.next();
        String websiteKey = (String)entry.getKey();
        if ((websiteKey != null) && (!websiteKey.trim().equals("")) && (websiteKey.contains("."))) {
          String[] aa = websiteKey.split("\\.");
          String websiteName = websiteKey.split("\\.")[0].trim();
          if (websiteMap.get(websiteName) == null) {
            websiteMap.put(websiteName, new HashMap());
          }
          String websiteInfoKey = websiteKey.split("\\.")[1].trim();
          String websiteInfoValue = ((String)entry.getValue()).trim();
          websiteInfoMap = (Map)websiteMap.get(websiteName);
          websiteInfoMap.put(websiteInfoKey, websiteInfoValue);

          map.put(websiteName, websiteInfoMap);
        }
      }
      websiteMap = map;
    }
    return map;
  }

  public List<ModuleInfo> addSuiteModules(SuiteInfo suiteInfo, Statement base, Description des) {
    List moduleInfoList = new ArrayList();
    Set classes = new HashSet();
    if (suiteInfo.getReportMerged().booleanValue()) {
      try {
        classes = ClassUtil.getClasses(getClass().getResource("/").getPath().replaceFirst("/", "").replace("/", "\\"));
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    else {
      classes.add(des.getTestClass());
    }

    if (classes.size() > 1);
    for (Iterator i = classes.iterator(); i.hasNext(); ) {
    	Class cls;
    	cls = (Class)i.next();
    	
      if (cls.getAnnotation(Ignore.class) == null)
      {
        List<String> moduleNameList = getSuiteModuleNameList(cls);

        for (String moduleName : moduleNameList) {
          ModuleInfo moduleInfo = new ModuleInfo();
          moduleInfo.setModuleName(cls.getSimpleName() + "." + moduleName);
          moduleInfo.setModuleRun(Boolean.valueOf(true));
          moduleInfo.setModuleStatus(RunStatus.WAITING);
          moduleInfo.setModuleResult(RunResult.WAITING);
          moduleInfoList.add(moduleInfo);
        }
      }
    }
    Class cls;
    List planedModuleInfoList = mergePlanModuleInfo(moduleInfoList);
    suiteInfo.setModuleInfoList(planedModuleInfoList);
    return moduleInfoList;
  }

  private List<ModuleInfo> mergePlanModuleInfo(List<ModuleInfo> moduleInfoList) {
    List planedModuleInfoList = new ArrayList();
    String testPlan = properties.getProperty("plan", "create");
    if (testPlan.trim().toLowerCase().equals("error"))
    {
      if ((planedSuiteInfo.getModuleInfoList() == null) || (planedSuiteInfo.getModuleInfoList().size() == 0)) {
        planedSuiteInfo.setModuleInfoList(planedModuleInfoList);
        return planedModuleInfoList;
      }
    } else if (testPlan.trim().toLowerCase().equals("all")) {
      try {
        planedSuiteInfo = XmlUtil.parsePlanXml(SOURCES_DIR + "plan.xml");
      }
      catch (Exception e) {
      }
      if ((planedSuiteInfo.getModuleInfoList() == null) || (planedSuiteInfo.getModuleInfoList().size() == 0)) {
        planedSuiteInfo.setModuleInfoList(planedModuleInfoList);
        return moduleInfoList;
      }
    } else { if (testPlan.contains("{")) {
        System.out.println(testPlan);
        SuiteInfo suite = (SuiteInfo)JSON.parseObject(testPlan, SuiteInfo.class);
        createPlan(suite);
        planedSuiteInfo.setModuleInfoList(suite.getModuleInfoList());
        return suite.getModuleInfoList();
      }
      planedSuiteInfo.setModuleInfoList(moduleInfoList);
      return moduleInfoList;
    }

    for (Iterator i = planedSuiteInfo.getModuleInfoList().iterator(); i.hasNext(); ) { ModuleInfo planedModuleInfo = (ModuleInfo)i.next();
      if (planedModuleInfo.getModuleRun().booleanValue())
        for (ModuleInfo moduleInfo : moduleInfoList)
          if (moduleInfo.getModuleName().equals(planedModuleInfo.getModuleName())) {
            planedModuleInfoList.add(moduleInfo);
            break;
          }
    }
    ModuleInfo planedModuleInfo;
    return planedModuleInfoList;
  }

  public void moduleRunStart(ModuleInfo moduleInfo, Statement base, Description des)
  {
    moduleInfo = new ModuleInfo();
    moduleInfo.setModuleName(des.getTestClass().getSimpleName() + "." + des.getMethodName());
    moduleInfo.setModuleStatus(RunStatus.RUNNING);
    moduleInfo.setModuleResult(RunResult.RUNNING);
    moduleInfo.setModuleRun(Boolean.valueOf(true));
    moduleInfo.setModuleStartTime(new Date());
    addModuleCases(moduleInfo, base, des);
    updateSuiteInfo(LogModule.SUITE_INFO, moduleInfo);
    System.out.println("测试模块（" + moduleInfo.getModuleName() + "）执行开始。");
    moduleRunStartLog(moduleInfo);
    LogModule.MODULE_INFO = moduleInfo;
  }

  public List<CaseInfo> addModuleCases(ModuleInfo moduleInfo, Statement base, Description des) {
    List datas = getDatas(base, des);
    List caseInfoList = new ArrayList();
    if (datas.size() == 0) {
      Map data = new HashMap();
      data.put("caseName", "用例一");
      data.put("caseDescription", "用例一描述信息");
      datas.add(data);
    }

    for (Iterator i = datas.iterator(); i.hasNext(); ) { Map data = (Map)i.next();
      String website = properties.getProperty("website");
      if ((website != null) && (!websiteMap.isEmpty())) {
        Map websiteInfo = (Map)websiteMap.get(website);
        if ((websiteInfo != null) && (!websiteInfo.isEmpty())) {
          data.putAll(websiteInfo);
        }
      }
      List<Map<String,String>> browserTypeCases = getBrowserTypeCases(data);
      for (Map browserTypeCase : browserTypeCases) {
        CaseInfo caseInfo = new CaseInfo();
        String caseName = (String)data.get("caseName");
        if (caseName != null) {
          Pattern pa = Pattern.compile("\\s*|\t|\r|\n");
          Matcher m = pa.matcher(caseName);
          caseName = m.replaceAll("");
        } else {
          caseName = "无用例名称";
        }
        if (browserTypeCase.get("browserType") != null) {
          caseName = caseName + "_" + (String)browserTypeCase.get("browserType");
          caseInfo.setCaseBrowserType(BrowserType.valueOf((String)browserTypeCase.get("browserType")));
        } else {
          caseInfo.setCaseBrowserType(BrowserType.IE);
        }
        caseInfo.setCaseName(caseName);
        caseInfo.setCaseDesc((String)data.get("caseDescription"));
        caseInfo.setCaseRun(Boolean.valueOf((data.get("caseRun") == null) || ("Y".equals(((String)data.get("caseRun")).toUpperCase().trim()))));
        caseInfo.setCasePriority((String)data.get("casePriority"));
        String caseRerunNum = (properties.getProperty("caseRerunNum") == null) || (properties.getProperty("caseRerunNum").trim().equals("")) ? "0" : properties.getProperty("caseRerunNum");

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
    if (planedSuiteInfo.getModuleInfoList().size() == 0)
      return moduleInfo.getCaseInfoList();
    for (ModuleInfo module : planedSuiteInfo.getModuleInfoList())
    {
      if (moduleInfo.getModuleName().equals(module.getModuleName())) {
        planedModuleInfo = module;
        break;
      }
    }
    if (planedModuleInfo == null) {
      return planedCaseInfoList;
    }
    if ((planedModuleInfo.getCaseInfoList() == null) || (planedModuleInfo.getCaseInfoList().size() == 0)) {
      return moduleInfo.getCaseInfoList();
    }
    for (Iterator i = planedModuleInfo.getCaseInfoList().iterator(); i.hasNext(); ) { CaseInfo planedCaseInfo = (CaseInfo)i.next();
      if (planedCaseInfo.getCaseRun().booleanValue())
        for (CaseInfo caseInfo : moduleInfo.getCaseInfoList())
          if (caseInfo.getCaseName().equals(planedCaseInfo.getCaseName())) {
            planedCaseInfoList.add(caseInfo);
            break;
          }
    }
    CaseInfo planedCaseInfo;
    return planedCaseInfoList;
  }

  private List<Map<String, String>> getBrowserTypeCases(Map<String, String> data) {
    List browserTypeCases = new ArrayList();

    if (data.get("browserType") != null) {
      String[] types = ((String)data.get("browserType")).split(",");
      for (String type : types) {
        Map browserTypeCase = new HashMap();
        browserTypeCase.putAll(data);
        if (type.trim().toUpperCase().equals("IE")) {
          browserTypeCase.put("browserType", "IE");
        } else if ((type.trim().toUpperCase().equals("FF")) || (type.toUpperCase().equals("FIREFOX"))) {
          browserTypeCase.put("browserType", "FIREFOX");
        } else if (type.trim().toUpperCase().equals("CHROME")) {
          browserTypeCase.put("browserType", "CHROME");
        } else if (type.trim().equals("")) {
          browserTypeCase.put("browserType", "IE");
        } else {
          System.err.println("设置的浏览器类型为:" + type + "，格式有问题！请参照：单个浏览器如IE；多个浏览器用英文逗号分隔，如IE,FF,CHROME。");
          throw new RuntimeException("数据池中的browserType格式有问题，请改正！\n设置的浏览器类型为:" + type + "，格式有问题！请参照：单个浏览器如IE；多个浏览器用英文逗号分隔，如IE,FF,CHROME。");
        }
        browserTypeCases.add(browserTypeCase);
      }
    } else {
      browserTypeCases.add(data);
    }
    return browserTypeCases;
  }

  public void updateSuiteInfo(SuiteInfo suiteInfo, ModuleInfo currentModuleInfo) {
    List moduleInfoList = suiteInfo.getModuleInfoList();
    for (int i = 0; i < moduleInfoList.size(); i++) {
      if (((ModuleInfo)moduleInfoList.get(i)).getModuleName().equals(currentModuleInfo.getModuleName())) {
        if (currentModuleInfo.getModuleStatus() == RunStatus.COMPLETED) {
          suiteInfo.setPassCaseNum(suiteInfo.getPassCaseNum() + currentModuleInfo.getPassCaseNum());
          suiteInfo.setFailCaseNum(suiteInfo.getFailCaseNum() + currentModuleInfo.getFailCaseNum());
          suiteInfo.setSuiteCaseNum(suiteInfo.getSuiteCaseNum() + currentModuleInfo.getModuleCaseNum());
          suiteInfo.setSuiteModuleNum(suiteInfo.getSuiteModuleNum() + 1);
        }
        moduleInfoList.set(i, currentModuleInfo);
        break;
      }
    }
    suiteInfo.setModuleInfoList(moduleInfoList);
    LogModule.SUITE_INFO = suiteInfo;
  }

  public void moduleRunStop(ModuleInfo moduleInfo, Statement base, Description des)
  {
    moduleInfo.setModuleStatus(RunStatus.COMPLETED);
    moduleInfo.setModuleResult(RunResult.PASS);
    moduleInfo.setModuleStopTime(new Date());

    NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumFractionDigits(2);
    float percent = 100.0F;
    if (moduleInfo.getModuleCaseNum() > 0) {
      percent = Float.parseFloat(numberFormat.format(moduleInfo.getPassCaseNum() / moduleInfo.getModuleCaseNum() * 100.0F));
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

    moduleRunStopLog(moduleInfo);
  }

  public void caseRunStart(CaseInfo caseInfo, Statement base, Description des)
  {
    LogModule.logStepInfoList.clear();
    caseInfo.setCaseResult(RunResult.RUNNING);
    caseInfo.setCaseStatus(RunStatus.RUNNING);
    caseInfo.setCaseStartTime(new Date());
    updateModuleInfo(LogModule.MODULE_INFO, caseInfo);
    System.out.println("测试用例（" + caseInfo.getCaseName() + "）执行开始。");

    String plan = properties.getProperty("plan", "create").trim();
    if (plan.equals("create")) {
      createPlan(LogModule.SUITE_INFO);
    }
    caseRunStartLog(caseInfo);
  }

  public void updateModuleInfo(ModuleInfo moduleInfo, CaseInfo currentCaseInfo) {
    List caseInfoList = moduleInfo.getCaseInfoList();
    for (int i = 0; i < caseInfoList.size(); i++) {
      if (((CaseInfo)caseInfoList.get(i)).getCaseName().equals(currentCaseInfo.getCaseName())) {
        if (currentCaseInfo.getCaseResult() == RunResult.PASS) {
          moduleInfo.setPassCaseNum(moduleInfo.getPassCaseNum() + 1);
          moduleInfo.setModuleCaseNum(moduleInfo.getModuleCaseNum() + 1);
        } else if (currentCaseInfo.getCaseResult() == RunResult.FAIL) {
          moduleInfo.setFailCaseNum(moduleInfo.getFailCaseNum() + 1);
          moduleInfo.setModuleCaseNum(moduleInfo.getModuleCaseNum() + 1);
        }
        caseInfoList.set(i, currentCaseInfo);
        break;
      }
    }
    moduleInfo.setCaseInfoList(caseInfoList);
    LogModule.MODULE_INFO = moduleInfo;
  }

  public void caseRunStop(CaseInfo caseInfo, Statement base, Description des)
  {
    caseInfo.setCaseResult(RunResult.PASS);
    caseInfo.setCaseStopTime(new Date());
    for (LogStepInfo logStepInfo : LogModule.logStepInfoList) {
      if (logStepInfo.getStepResult() == RunResult.FAIL) {
        caseInfo.setCaseResult(RunResult.FAIL);
        break;
      }
    }
    String logLevel = properties.getProperty("logLevel", "error");

    if ((logLevel.toLowerCase().trim().equals("error")) && (caseInfo.getCaseResult() == RunResult.PASS)) {
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
    caseInfo.setCaseStatus(RunStatus.COMPLETED);
    System.out.println("测试用例（" + caseInfo.getCaseName() + "）执行结束。");
    caseInfo.setCaseRerunNum(caseInfo.getCaseRerunNum() - 1);
    if ((caseInfo.getCaseResult().equals(RunResult.PASS)) || (caseInfo.getCaseRerunNum() < 0)) {
      updateModuleInfo(LogModule.MODULE_INFO, caseInfo);
      caseRunStopLog(caseInfo);
    }
  }

  private static Map<String, String> getTestMethods(Class<?> Clazz)
  {
    Method[] methods = Clazz.getMethods();
    Map map = new LinkedHashMap();
    int i = 0;
    for (Method method : methods) {
      String methodName = method.getName();
      if ((method.isAnnotationPresent(BeforeClass.class)) || (method.isAnnotationPresent(AfterClass.class))) {
        map.put(method.getAnnotations()[0].annotationType().getSimpleName(), methodName);
      } else if (method.isAnnotationPresent(Test.class)) {
        map.put(String.valueOf(i), methodName);
        i++;
      }
    }
    return map;
  }

  private List<String> getSuiteModuleNameList(Class<?> Clazz)
  {
    List moduleNameList = new ArrayList();
    Method[] methods = Clazz.getMethods();

    for (Method method : methods) {
      String methodName = method.getName();
      if ((method.isAnnotationPresent(Test.class)) && (method.getAnnotation(Ignore.class) == null)) {
        moduleNameList.add(methodName);
      }
    }
    return moduleNameList;
  }

  private void suiteRunStartLog(SuiteInfo suiteInfo) {
    LOG_DIR = properties.getProperty("logDir", LOG_DIR);
    LOG_DIR = LOG_DIR + suiteInfo.getSuiteName() + "/";
    String buildNumber = System.getProperty("build.number");
    if (buildNumber != null)
      LOG_DIR = LOG_DIR + buildNumber + "/";
    else {
      LOG_DIR = LOG_DIR + DateUtil.dateToStr(new Date(), "yyyy-MM-dd_HH-mm-ss") + "/";
    }
    LogModule.SUITE_PATH = LOG_DIR;
    File directory = new File(LOG_DIR);
    if (!directory.exists()) {
      directory.mkdirs();
    }

    StackTraceElement[] stack = Thread.currentThread().getStackTrace();
    for (StackTraceElement ste : stack)
    {
      if (ste.getClassName().contains("maven")) {
        encode = "GBK";
        break;
      }
    }
  }

  private void createPlan(SuiteInfo suiteInfo)
  {
    Properties p = new Properties();
    p.setProperty("input.encoding", "UTF-8");
    p.setProperty("output.encoding", "UTF-8");
    Velocity.init(p);

    VelocityContext context = new VelocityContext();
    String project = (String)(properties.get("project") == null ? "" : properties.get("project"));
    String runner = (String)(properties.get("runner") == null ? "" : properties.get("runner"));
    context.put("suiteInfo", suiteInfo);

    context.put("encode", encode);
    TEMPLATES_DIR = properties.getProperty("templatesDir", TEMPLATES_DIR);
    File file = new File(TEMPLATES_DIR + "plan.vm");
    if (!file.exists()) {
      try {
        FileWriter writer = new FileWriter(TEMPLATES_DIR + "plan.vm", true);
        writer.write(PLAN_VM_CONTENT);
        writer.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    Template template = Velocity.getTemplate(TEMPLATES_DIR + "plan.vm");
    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    try
    {
      PrintWriter filewriter = new PrintWriter(new FileOutputStream(SOURCES_DIR + "plan.xml"), true);
      filewriter.println(writer.toString());
      filewriter.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void intialPlanError() {
    Properties p = new Properties();
    p.setProperty("input.encoding", "UTF-8");
    p.setProperty("output.encoding", "UTF-8");
    Velocity.init(p);

    VelocityContext context = new VelocityContext();
    String project = (String)(properties.get("project") == null ? "" : properties.get("project"));
    String runner = (String)(properties.get("runner") == null ? "" : properties.get("runner"));
    List errorModuleInfoList = new ArrayList();
    for (ModuleInfo moduleInfo : LogModule.SUITE_INFO.getModuleInfoList()) {
      if (!moduleInfo.getModuleResult().equals(RunResult.PASS)) {
        List errorCaseInfoList = new ArrayList();
        for (CaseInfo caseInfo : moduleInfo.getCaseInfoList()) {
          if (!caseInfo.getCaseResult().equals(RunResult.PASS)) {
            errorCaseInfoList.add(caseInfo);
          }
        }
        moduleInfo.setCaseInfoList(errorCaseInfoList);
        errorModuleInfoList.add(moduleInfo);
      }
    }
    SuiteInfo suiteInfo = new SuiteInfo();
    suiteInfo.setSuiteName(LogModule.SUITE_INFO.getSuiteName());
    suiteInfo.setModuleInfoList(errorModuleInfoList);

    context.put("suiteInfo", suiteInfo);

    context.put("encode", encode);
    TEMPLATES_DIR = properties.getProperty("templatesDir", TEMPLATES_DIR);
    Template template = Velocity.getTemplate(TEMPLATES_DIR + "plan.vm");
    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    try
    {
      PrintWriter filewriter = new PrintWriter(new FileOutputStream(SOURCES_DIR + "plan.error.xml"), true);
      filewriter.println(writer.toString());
      filewriter.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void suiteRunStopLog(SuiteInfo suiteInfo)
  {
    Properties p = new Properties();
    p.setProperty("input.encoding", "UTF-8");
    p.setProperty("output.encoding", "UTF-8");
    Velocity.init(p);

    VelocityContext context = new VelocityContext();
    String project = (String)(properties.get("project") == null ? "" : properties.get("project"));
    String runner = (String)(properties.get("runner") == null ? "" : properties.get("runner"));
    context.put("suiteInfo", suiteInfo);
    context.put("project", project);
    context.put("runner", runner);
    context.put("encode", encode);

    TEMPLATES_DIR = properties.getProperty("templatesDir", TEMPLATES_DIR);
    Template template = Velocity.getTemplate(TEMPLATES_DIR + "suiteResult.vm");
    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    try
    {
      PrintWriter filewriter = new PrintWriter(new FileOutputStream(LOG_DIR + suiteInfo.getSuiteName() + ".html"), true);
      filewriter.println(writer.toString());
      filewriter.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    if ((System.getProperty("report.zip") != null) && (!System.getProperty("report.zip").toLowerCase().trim().equals("false")))
      ZipUtil.zip(LOG_DIR, "report.zip");
  }

  private void moduleRunStartLog(ModuleInfo moduleInfo)
  {
    MODULE_DIR = LOG_DIR + "测试模块_" + moduleInfo.getModuleName() + "/";
    File directory = new File(MODULE_DIR);
    if (!directory.exists())
      directory.mkdirs();
  }

  private void moduleRunStopLog(ModuleInfo moduleInfo)
  {
    Properties p = new Properties();
    p.setProperty("input.encoding", "UTF-8");
    p.setProperty("output.encoding", "UTF-8");
    Velocity.init(p);

    VelocityContext context = new VelocityContext();
    context.put("moduleInfo", moduleInfo);
    context.put("encode", encode);
    TEMPLATES_DIR = properties.getProperty("templatesDir", TEMPLATES_DIR);
    Template template = Velocity.getTemplate(TEMPLATES_DIR + "moduleResult.vm");
    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    try
    {
      PrintWriter filewriter = new PrintWriter(new FileOutputStream(LOG_DIR + "测试模块_" + moduleInfo.getModuleName() + ".html"), true);
      filewriter.println(writer.toString());
      filewriter.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void caseRunStartLog(CaseInfo caseInfo)
  {
  }

  private void caseRunStopLog(CaseInfo caseInfo) {
    Properties p = new Properties();

    p.setProperty("input.encoding", "UTF-8");
    p.setProperty("output.encoding", "UTF-8");

    Velocity.init(p);

    VelocityContext context = new VelocityContext();
    context.put("logStepInfoList", LogModule.logStepInfoList);
    context.put("caseInfo", caseInfo);
    context.put("encode", encode);
    TEMPLATES_DIR = properties.getProperty("templatesDir", TEMPLATES_DIR);
    Template template = Velocity.getTemplate(TEMPLATES_DIR + "caseResult.vm");

    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    try
    {
      PrintWriter filewriter = new PrintWriter(new FileOutputStream(MODULE_DIR + "测试用例_" + caseInfo.getCaseName() + ".html"), true);
      filewriter.println(writer.toString());
      filewriter.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}