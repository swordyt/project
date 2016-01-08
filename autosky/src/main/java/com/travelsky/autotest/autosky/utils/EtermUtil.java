package com.travelsky.autotest.autosky.utils;

import eterm.EtermProxy;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EtermUtil
{
  private static String DEFAULT_USER_NAME = "pengzhong";
  private static String DEFAULT_PASSWORD = "123456789asd";
  private static String DEFAULT_HOST = "10.6.185.118";
  private static String DEFAULT_SI = "si 59477 12345a 9 ";
  private static Boolean connected = Boolean.valueOf(false);
  private static EtermProxy etermProxy = new EtermProxy();

  public static void main(String[] args) {
    Map params = new HashMap();
    params.put("etermHost", "10.6.185.118");
    params.put("etermUserName", "liuxiao");
    params.put("etermPassword", "1qaz2wsx");
    params.put("etermSession", "1");
    params.put("etermTip", "tipd1");
    params.put("airlineCode", "CA");
    params.put("officeCode", "PEK001");
    params.put("groupType", "0");
    params.put("segments", "SHA-PEK-SHA");

    params.put("cabins", "C,Y");
    params.put("isinter", "0");
    params.put("adultCount", "4");
    params.put("childCount", "3");
    params.put("infantCount", "0");
    connectTerm(params);
    etermLogin(params);
    String pnrNo = etermBook(params);
  }

  public static EtermProxy connectTerm(Map<String, String> params)
  {
    int sessionNo = params.get("etermSession") == null ? 0 : Integer.parseInt((String)params.get("etermSession"));
    System.out.println((String)params.get("etermHost"));
    String host = params.get("etermHost") == null ? DEFAULT_HOST : (String)params.get("etermHost");
    String userName = params.get("etermUserName") == null ? DEFAULT_USER_NAME : (String)params.get("etermUserName");
    String password = params.get("etermPassword") == null ? DEFAULT_PASSWORD : (String)params.get("etermPassword");
    connectTerm(host, userName, password, sessionNo);
    return etermProxy;
  }

  public static EtermProxy connectTerm(String host, String userName, String password, int sessionNo) {
    if (!connected.booleanValue()) {
      String connMsg = etermProxy.ConnectEterm(host, userName, password, sessionNo);

      if (connMsg.equals("连接成功")) {
        connected = Boolean.valueOf(true);
      }
    }
    return etermProxy;
  }

  public static EtermProxy connectTerm(String host, String userName, String password) {
    connectTerm(host, userName, password, 0);
    return etermProxy;
  }

  public static EtermProxy connectTerm() {
    connectTerm(DEFAULT_HOST, DEFAULT_USER_NAME, DEFAULT_PASSWORD, 0);
    return etermProxy;
  }

  public static void etermLogin(Map<String, String> params) {
    etermProxy.SendCmd("so");
    String si = params.get("si") == null ? DEFAULT_SI : (String)params.get("si");
    etermProxy.SendCmd(si + (String)params.get("officeCode"));
    String daInfo = etermProxy.SendCmd("DA");
    etermProxy.SendCmd("$$open " + (String)params.get("etermTip"));
    etermProxy.SendCmd(si + (String)params.get("officeCode"));
    etermProxy.SendCmd("IG");
    if (!daInfo.substring(daInfo.indexOf("AIRLINE") + 7, daInfo.indexOf("AIRLINE") + 14).contains((CharSequence)params.get("airlineCode"))) {
      etermProxy.SendCmd("UAL:" + (String)params.get("airlineCode"));
    }
    etermProxy.SendCmd("UC:" + (String)params.get("officeCode"));
  }

  public static String etermBook(Map<String, String> params)
  {
    int customerTotle = 0;
    int adultCount = params.get("adultCount") == null ? 0 : Integer.parseInt((String)params.get("adultCount"));
    int childCount = params.get("childCount") == null ? 0 : Integer.parseInt((String)params.get("childCount"));
    int infantCount = params.get("infantCount") == null ? 0 : Integer.parseInt((String)params.get("infantCount"));
    customerTotle = adultCount + childCount + infantCount;
    params.put("etermPassengerCount", String.valueOf(customerTotle));
    List<String> kkFlightNoList = new ArrayList();
    int etermIntervalDay = 10 + new Random().nextInt(30);
    params.put("etermIntervalDay", String.valueOf(etermIntervalDay));

    List segmentList = parseSegments(params);
    if (Integer.parseInt((String)params.get("groupType")) == 1) {
      etermProxy.SendCmd("gn:" + customerTotle + " " + CommonUtil.getRandomCharacter(5));
    }

    for (int i = 1; i <= segmentList.size(); i++) {
      params.put("etermSegIndex", String.valueOf(i));
      params.put("etermOrgCityCode", ((String)segmentList.get(i - 1)).split("-")[0]);
      params.put("etermDstCityCode", ((String)segmentList.get(i - 1)).split("-")[1]);
      if (i > 0) {
        int intervalDay = Integer.parseInt((String)params.get("etermIntervalDay"));
        params.put("etermIntervalDay", String.valueOf(intervalDay + 1));
      }
      findFlight(params, etermProxy);
      if (Boolean.valueOf((String)params.get("etermNeedHK")).booleanValue()) {
        kkFlightNoList.add(params.get("etermFlightNo"));
      }
    }
    StringBuffer passgerName = new StringBuffer();
    List<String> audltPnrName = new ArrayList();
    List<String> childPnrName = new ArrayList();

    for (int i = 0; i < Integer.parseInt((String)params.get("adultCount")); i++) {
      String audltFamName = CommonUtil.getRandomCharacter(2).toUpperCase();
      String audltGivName = CommonUtil.getRandomCharacter(2).toUpperCase();
      String adultNames = "1" + audltFamName + "/" + audltGivName;
      audltPnrName.add(audltFamName + "/" + audltGivName);
      if (Integer.parseInt((String)params.get("groupType")) == 0) {
        passgerName.append(adultNames);
      } else {
        etermProxy.AddMultiLineCmd("NM" + adultNames);
        if (i == Integer.parseInt((String)params.get("adultCount")) - 1) {
          etermProxy.SendMultiLineCmd();
        }
      }
    }

    for (int i = 0; i < Integer.parseInt((String)params.get("childCount")); i++) {
      String childFamName = CommonUtil.getRandomCharacter(2).toUpperCase();
      String childGivName = CommonUtil.getRandomCharacter(2).toUpperCase();
      String childNames = "1" + childFamName + "/" + childGivName + " " + "CHD";
      if (Integer.parseInt((String)params.get("groupType")) == 0) {
        passgerName.append(childNames);
      } else {
        etermProxy.AddMultiLineCmd("NM" + childNames);
        if (i == Integer.parseInt((String)params.get("childCount")) - 1) {
          etermProxy.SendMultiLineCmd();
        }
      }
      childPnrName.add(childFamName + "/" + childGivName);
    }

    String passengerInfo = "";
    if (Integer.parseInt((String)params.get("groupType")) == 0)
      passengerInfo = etermProxy.SendCmd("NM " + passgerName.toString());
    else {
      passengerInfo = etermProxy.SendCmd("rtn");
    }
    if (Integer.parseInt((String)params.get("isinter")) == 0) {
      for (int i = 0; i < customerTotle; i++) {
        int j = i + 1;
        etermProxy.AddMultiLineCmd("SSR FOID " + (String)params.get("airlineCode") + " " + "HK/NI" + CommonUtil.getRandomNumber(10) + "/P" + j);
      }
      etermProxy.SendMultiLineCmd();
    } else if (Integer.parseInt((String)params.get("isinter")) == 1) {
      int audltCount = 0;
      String audltBirthDay = DateUtil.getSpecifyYear(-25, "ddMMMyy", Locale.ENGLISH);
      String adultPassportDate = DateUtil.getSpecifyYear(5, "ddMMMyy", Locale.ENGLISH);
      for (String adultame : audltPnrName) {
        audltCount++;
        etermProxy.AddMultiLineCmd("SSR DOCS " + (String)params.get("airlineCode") + " " + "HK1 P/CN/" + CommonUtil.getRandomNumber(10) + "/CN/" + audltBirthDay + "/M/" + adultPassportDate + "/" + adultame + "/P" + audltCount);
      }

      String childBirthDay = DateUtil.getSpecifyYear(-5, "ddMMMyy", Locale.ENGLISH);
      String childPassportDate = DateUtil.getSpecifyYear(5, "ddMMMyy", Locale.ENGLISH);
      for (String childName : childPnrName) {
        int i = passengerInfo.indexOf(childName) - 3;
        int passengerIndex = Integer.parseInt(passengerInfo.substring(i, i + 2).trim());

        etermProxy.AddMultiLineCmd("SSR DOCS " + (String)params.get("airlineCode") + " " + "HK1 P/CN/" + CommonUtil.getRandomNumber(10) + "/CN/" + childBirthDay + "/M/" + childPassportDate + "/" + childName + "/P" + passengerIndex);
      }

      etermProxy.SendMultiLineCmd();
    }
    for (String name : childPnrName) {
      int i = passengerInfo.indexOf(name) - 3;
      int passengerIndex = Integer.parseInt(passengerInfo.substring(i, i + 2).trim());
      String birthDay = DateUtil.getSpecifyYear(-5, "ddMMMyy", Locale.ENGLISH);

      etermProxy.AddMultiLineCmd("SSR CHLD " + (String)params.get("airlineCode") + "HK1 " + birthDay + "/P" + passengerIndex);
    }

    if (childPnrName.size() > 0) {
      etermProxy.SendMultiLineCmd();
    }
    etermProxy.SendCmd("OSI " + (String)params.get("airlineCode") + "CTCT13512345678");
    etermProxy.SendCmd("CT 123456789");
    etermProxy.SendCmd("TK:TL 1200/" + DateUtil.getSpecifyDate(5, "ddMMM", Locale.ENGLISH) + "/" + (String)params.get("officeCode"));

    String temp = etermProxy.SendCmd("@");
    String pnrNo = temp.substring(0, 6);
    if (pnrNo.trim().length() < 6) {
      String[] tempLine = temp.split("\n");
      pnrNo = tempLine[(tempLine.length - 2)].substring(0, 6);
    }

    for (String flightNo : kkFlightNoList) {
      String roMessage = "";
      String pnr = etermProxy.SendCmd("rt:" + pnrNo);
      String[] messages = pnr.split("\n");
      Pattern pattern = Pattern.compile(".* " + flightNo + ".*" + "HN" + (String)params.get("etermPassengerCount") + ".*", 32);
      for (String message : messages) {
        Matcher m = pattern.matcher(message);
        if (m.matches()) {
          int index = Integer.parseInt(pnr.substring(pnr.indexOf(flightNo) - 5, pnr.indexOf(flightNo) - 3).trim());
          if (Integer.parseInt((String)params.get("isinter")) == 0)
            roMessage = etermProxy.SendCmd("ro " + flightNo + " " + DateUtil.getSpecifyDate(15, "ddMMM", Locale.ENGLISH));
          else {
            roMessage = etermProxy.SendCmd("ro " + flightNo);
          }
          int i = 1;
          while ((!roMessage.contains("BROW")) && (i < 7)) {
            roMessage = etermProxy.SendCmd("ro " + flightNo + " " + DateUtil.getSpecifyDate(15 + i, "ddMMM", Locale.ENGLISH));
            i++;
          }
          String office = roMessage.substring(roMessage.indexOf("BROW") - 7, roMessage.indexOf("BROW") - 1);
          etermProxy.SendCmd("UC:" + office);
          etermProxy.SendCmd(index + "hk");
          etermProxy.SendCmd("@");
          etermProxy.SendCmd("UC:" + (String)params.get("officeCode"));
        }
      }
    }

    etermProxy.SendCmd("rt:" + pnrNo);
    etermProxy.SendCmd("@");
    System.out.println("预定的PNR为：" + pnrNo);
    return pnrNo;
  }

  private static int findFlight(Map<String, String> params, EtermProxy etermProxy) {
    String orgCityCode = (String)params.get("etermOrgCityCode");
    String dstCityCode = (String)params.get("etermDstCityCode");
    int intervalDay = Integer.parseInt((String)params.get("etermIntervalDay"));
    String cabins = params.get("cabins") == null ? "Y" : (String)params.get("cabins");
    Pattern pattern = Pattern.compile(".* " + (String)params.get("airlineCode") + ".*" + orgCityCode + dstCityCode + ".*", 32);
    String avMessage = "";
    Boolean avFind = Boolean.valueOf(false);
    Boolean hasFlight = Boolean.valueOf(false);
    int sdIndex = 1;
    int tryNum = 100;
    while ((!avFind.booleanValue()) && (tryNum > 0)) {
      String departDate = DateUtil.getSpecifyDate(intervalDay, "ddMMM", Locale.ENGLISH);
      avMessage = etermProxy.SendCmd("AV:" + orgCityCode + dstCityCode + "/" + departDate + "/" + (String)params.get("airlineCode"));
      String[] flightInfo = avMessage.split("\n");
      for (int i = 0; i < flightInfo.length; i++) {
        String a = flightInfo[i];
        Matcher m = pattern.matcher(flightInfo[i]);
        if (m.matches()) {
          int index = flightInfo[i].indexOf(orgCityCode + dstCityCode);
          avMessage = flightInfo[i].substring(index + 6) + flightInfo[(i + 1)];
          for (String cabinInfo : avMessage.split(" ")) {
            if (!cabinInfo.trim().equals(""))
            {
              for (String cabin : cabins.split(",")) {
                if ((cabinInfo.startsWith(cabin + "A")) || (cabinInfo.endsWith(cabin + "A"))) {
                  sdIndex = Integer.parseInt(flightInfo[i].substring(0, 1));
                  String flight = etermProxy.SendCmd("SD:" + sdIndex + cabin + (String)params.get("etermPassengerCount"));
                  if ((flight.contains("SHARE")) || (flight.contains("\"AV\""))) {
                    etermProxy.SendCmd("XE:" + (String)params.get("etermSegIndex"));
                    break;
                  }
                  try {
                    flight = flight.substring(flight.indexOf((String)params.get("etermSegIndex") + "."));

                    if (flight.contains("HK" + (String)params.get("etermPassengerCount"))) {
                      avFind = Boolean.valueOf(true);
                      String flightNo = flightInfo[i].substring(4, index).trim();
                      params.put("etermFlightNo", flightNo);
                      params.put("etermNeedHK", "false");
                      params.put("etermCabin", cabin);
                    } else if (flight.contains("HN" + (String)params.get("etermPassengerCount"))) {
                      avFind = Boolean.valueOf(true);
                      String flightNo = flightInfo[i].substring(4, index).trim();
                      params.put("etermFlightNo", flightNo);
                      params.put("etermNeedHK", "true");
                      params.put("etermCabin", cabin);
                    } else {
                      etermProxy.SendCmd("XE:" + (String)params.get("etermSegIndex"));
                    }
                  }
                  catch (Exception e)
                  {
                  }
                }
              }

              if (avFind.booleanValue())
                break;
            }
          }
          if (avFind.booleanValue()) {
            break;
          }
        }
      }
      intervalDay++;
      params.put("etermIntervalDay", String.valueOf(intervalDay));
      tryNum--;
    }
    if (!avFind.booleanValue()) {
      throw new RuntimeException("无法查找到指定的有效航班！");
    }
    return sdIndex;
  }

  private static List<String> parseSegments(Map<String, String> params)
  {
    String segments = (String)params.get("segments");
    String[] segmentArray = segments.split("-");
    int segmentsCount = params.get("segmentsCount") == null ? segmentArray.length : Integer.parseInt((String)params.get("segmentsCount"));
    List segmentList = new ArrayList();

    int count = segmentsCount < segmentArray.length ? segmentsCount : segmentArray.length - 1;
    for (int i = 0; i < count; i++) {
      segmentList.add(segmentArray[i] + "-" + segmentArray[(i + 1)]);
    }
    return segmentList;
  }
}