package com.travelsky.autotest.autosky.junit.log;

import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.core.Observer;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.modules.CaseInfo;
import com.travelsky.autotest.autosky.junit.modules.LogStepInfo;
import com.travelsky.autotest.autosky.junit.modules.ModuleInfo;
import com.travelsky.autotest.autosky.junit.modules.SuiteInfo;
import com.travelsky.autotest.autosky.junit.rules.DriverService;
import com.travelsky.autotest.autosky.utils.ZipUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class LocalFileLog
  implements Observer
{
  private String templatesDir = DriverService.PROPERTIES.getProperty("templatesDir", "src/main/resources/templates/");
  private String moduleDir = "";
  private String encode = DriverService.ENCODE;
  private Properties properties = DriverService.PROPERTIES;

  public Map<String, String> param = new HashMap();
  public List<LogStepInfo> logStepInfoList = new ArrayList();

  public void suiteRunStart(SuiteInfo suiteInfo)
  {
    this.encode = "GBK";
  }

  public void suiteRunStop(SuiteInfo suiteInfo)
  {
    Properties p = new Properties();
    p.setProperty("input.encoding", "GBK");
    p.setProperty("output.encoding", "GBK");
    Velocity.init(p);

    VelocityContext context = new VelocityContext();
    String project = (String)(this.properties.get("project") == null ? "" : this.properties.get("project"));
    String runner = (String)(this.properties.get("runner") == null ? "" : this.properties.get("runner"));
    context.put("suiteInfo", suiteInfo);
    context.put("project", project);
    context.put("runner", runner);

    context.put("encode", this.encode);
    Template template = Velocity.getTemplate(this.templatesDir + "suiteResult.vm");
    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    try
    {
      PrintWriter filewriter = new PrintWriter(new FileOutputStream(LogModule.SUITE_PATH + suiteInfo.getSuiteName() + ".html"), true);
      filewriter.println(writer.toString());
      filewriter.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    if ((this.properties.getProperty("reportZip") != null) && (!this.properties.getProperty("reportZip").toLowerCase().trim().equals("false")))
      ZipUtil.zip(LogModule.SUITE_PATH, "report.zip");
  }

  public void moduleRunStart(ModuleInfo moduleInfo)
  {
    this.moduleDir = (LogModule.SUITE_PATH + "测试模块_" + moduleInfo.getModuleName() + "/");
    File directory = new File(this.moduleDir);
    if (!directory.exists())
      directory.mkdirs();
  }

  public void moduleRunStop(ModuleInfo moduleInfo)
  {
    Properties p = new Properties();
    p.setProperty("input.encoding", "GBK");
    p.setProperty("output.encoding", "GBK");
    Velocity.init(p);

    VelocityContext context = new VelocityContext();
    context.put("moduleInfo", moduleInfo);
    context.put("encode", this.encode);
    this.templatesDir = this.properties.getProperty("templatesDir", this.templatesDir);
    Template template = Velocity.getTemplate(this.templatesDir + "moduleResult.vm");
    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    try
    {
      PrintWriter filewriter = new PrintWriter(new FileOutputStream(LogModule.SUITE_PATH + "测试模块_" + moduleInfo.getModuleName() + ".html"), true);
      filewriter.println(writer.toString());
      filewriter.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void caseRunStart(CaseInfo caseInfo)
  {
  }

  public void caseRunStop(CaseInfo caseInfo)
  {
    if ((caseInfo.getCaseResult().equals(RunResult.FAIL)) && (caseInfo.getCaseRerunNum() >= 0)) {
      return;
    }
    Properties p = new Properties();

    p.setProperty("input.encoding", "GBK");
    p.setProperty("output.encoding", "GBK");

    Velocity.init(p);

    VelocityContext context = new VelocityContext();
    context.put("logStepInfoList", LogModule.logStepInfoList);
    context.put("caseInfo", caseInfo);
    context.put("encode", this.encode);
    this.templatesDir = this.properties.getProperty("templatesDir", this.templatesDir);
    Template template = Velocity.getTemplate(this.templatesDir + "caseResult.vm");

    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    try
    {
      PrintWriter filewriter = new PrintWriter(new FileOutputStream(this.moduleDir + "测试用例_" + caseInfo.getCaseName() + ".html"), true);
      filewriter.println(writer.toString());
      filewriter.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}