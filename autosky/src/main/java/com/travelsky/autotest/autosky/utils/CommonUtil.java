package com.travelsky.autotest.autosky.utils;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Random;

public class CommonUtil
{
  private static Boolean waitUntil(Boolean condition, int s)
  {
    Calendar now = Calendar.getInstance();
    Calendar end = (Calendar)now.clone();
    end.add(13, s);
    while ((!condition.booleanValue()) && (now.before(end))) {
      try {
        Thread.sleep(500L);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      now = Calendar.getInstance();
    }
    return condition;
  }

  private static Boolean waitUntil(Boolean condition)
  {
    return waitUntil(condition, 60);
  }

  public static void main(String[] args) {
    System.out.println(Calendar.getInstance().toString());
    if (waitUntil(Boolean.valueOf(false), 5).booleanValue())
      System.out.println("true");
    else {
      System.out.println("false");
    }
    System.out.println(Calendar.getInstance().toString());
  }

  public static String getRandomCharacterAndNumber(int length)
  {
    String val = "";

    Random random = new Random();
    for (int i = 0; i < length; i++) {
      String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";

      if ("char".equalsIgnoreCase(charOrNum))
      {
        int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
        val = val + (char)(choice + random.nextInt(26));
      } else if ("num".equalsIgnoreCase(charOrNum))
      {
        val = val + String.valueOf(random.nextInt(10));
      }
    }

    return val;
  }

  public static String getRandomCharacter(int length)
  {
    String val = "";

    Random random = new Random();
    for (int i = 0; i < length; i++) {
      int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
      val = val + (char)(choice + random.nextInt(26));
    }

    return val;
  }

  public static String getRandomNumber(int length)
  {
    String val = "";
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      val = val + String.valueOf(random.nextInt(10));
    }

    return val;
  }
}