package com.travelsky.autotest.autosky.junit.core;

import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.RunStatus;
import com.travelsky.autotest.autosky.junit.modules.CaseInfo;
import com.travelsky.autotest.autosky.junit.modules.ModuleInfo;
import com.travelsky.autotest.autosky.junit.modules.SuiteInfo;
import com.travelsky.autotest.autosky.junit.rules.DriverService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class PlanObserver
  implements Observer
{
  private Properties properties = DriverService.PROPERTIES;
  private String templatesDir = DriverService.PROPERTIES.getProperty("templatesDir", "src/main/resources/templates/");
  private String sourcesDir = DriverService.PROPERTIES.getProperty("sourcesDir", "src/test/resources/");

  public void suiteRunStart(SuiteInfo suiteInfo)
  {
  }

  public void moduleRunStart(ModuleInfo moduleInfo)
  {
    String plan = this.properties.getProperty("plan", "create").trim();
    if ((plan.equals("create")) || (plan.contains("{")))
      createPlan(LogModule.SUITE_INFO);
  }

  public void caseRunStart(CaseInfo caseInfo)
  {
  }

  public void suiteRunStop(SuiteInfo suiteInfo)
  {
    if (suiteInfo.getSuiteStatus() == RunStatus.COMPLETED)
      errorPlan();
  }

  public void moduleRunStop(ModuleInfo moduleInfo)
  {
  }

  public void caseRunStop(CaseInfo caseInfo)
  {
  }

  private void createPlan(SuiteInfo suiteInfo)
  {
    Properties p = new Properties();
    p.setProperty("input.encoding", "UTF-8");
    p.setProperty("output.encoding", "UTF-8");
    Velocity.init(p);

    VelocityContext context = new VelocityContext();
    String project = (String)(this.properties.get("project") == null ? "" : this.properties.get("project"));
    String runner = (String)(this.properties.get("runner") == null ? "" : this.properties.get("runner"));
    context.put("suiteInfo", suiteInfo);

    context.put("encode", DriverService.ENCODE);

    File file = new File(this.templatesDir + "plan.vm");
    if (!file.exists()) {
      try {
        FileWriter writer = new FileWriter(this.templatesDir + "plan.vm", true);
        writer.write(DriverService.PLAN_VM_CONTENT);
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    Template template = Velocity.getTemplate(this.templatesDir + "plan.vm");
    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    try
    {
      PrintWriter filewriter = new PrintWriter(new FileOutputStream(this.sourcesDir + "plan.xml"), true);
      filewriter.println(writer.toString());
      filewriter.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void errorPlan() {
    Properties p = new Properties();
    p.setProperty("input.encoding", "UTF-8");
    p.setProperty("output.encoding", "UTF-8");
    Velocity.init(p);

    VelocityContext context = new VelocityContext();
    String project = (String)(this.properties.get("project") == null ? "" : this.properties.get("project"));
    String runner = (String)(this.properties.get("runner") == null ? "" : this.properties.get("runner"));
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

    context.put("encode", DriverService.ENCODE);
    Template template = Velocity.getTemplate(this.templatesDir + "plan.vm");
    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    try
    {
      PrintWriter filewriter = new PrintWriter(new FileOutputStream(this.sourcesDir + "plan.error.xml"), true);
      filewriter.println(writer.toString());
      filewriter.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}