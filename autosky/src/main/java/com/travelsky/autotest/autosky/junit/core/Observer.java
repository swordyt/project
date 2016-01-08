package com.travelsky.autotest.autosky.junit.core;

import com.travelsky.autotest.autosky.junit.modules.CaseInfo;
import com.travelsky.autotest.autosky.junit.modules.ModuleInfo;
import com.travelsky.autotest.autosky.junit.modules.SuiteInfo;

public abstract interface Observer
{
  public abstract void suiteRunStart(SuiteInfo paramSuiteInfo);

  public abstract void moduleRunStart(ModuleInfo paramModuleInfo);

  public abstract void caseRunStart(CaseInfo paramCaseInfo);

  public abstract void suiteRunStop(SuiteInfo paramSuiteInfo);

  public abstract void moduleRunStop(ModuleInfo paramModuleInfo);

  public abstract void caseRunStop(CaseInfo paramCaseInfo);
}