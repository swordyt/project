package com.travelsky.autotest.autosky.junit.rules;

import org.junit.Rule;

public class DriverBase
{

  @Rule
  public static DriverService driverService = new DriverService();

  @Rule
  public static AssertCollector collector = new AssertCollector();
}