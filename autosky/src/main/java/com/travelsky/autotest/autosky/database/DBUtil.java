package com.travelsky.autotest.autosky.database;

import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.utils.FileUtil;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBUtil
{
  public static Map<String, Map<String, String>> databaseMap = new HashMap();
  public static Map<String, Connection> databaseConnection = new HashMap();
  private static final Set<String> registered = new HashSet();
  public static Connection currentConnect = null;
  public static String currentDatabase = null;
  private static Statement stmt = null;
  private static ResultSet rs = null;
  private static PreparedStatement prs = null;

  private static void registerDriver(String driver)
  {
    try
    {
      if (registered.contains(driver)) {
        return;
      }
      DriverManager.registerDriver((Driver)Class.forName(driver).newInstance());
      registered.add(driver);
    } catch (Throwable e) {
      throw new StepException("Cannot register SQL driver " + driver);
    }
  }

  public static Connection setConnection(String database)
  {
    String methodInfo = "setConnection(" + database + ")";
    Map databaseInfoMap = (Map)databaseMap.get(database);
    if (databaseInfoMap == null) {
      getDatabaseMap();
      databaseInfoMap = (Map)databaseMap.get(database);
      if (databaseInfoMap == null) {
        String message = "查找不到" + database + "对应的数据库信息，请配置database.properties文件中数据库信息！";
        LogModule.logStepFail(StepType.DATABASE, methodInfo, RunResult.FAIL, message);
        throw new StepException(message);
      }
    }
    try
    {
      registerDriver((String)databaseInfoMap.get("driver"));

      currentDatabase = database;
      currentConnect = DriverManager.getConnection((String)databaseInfoMap.get("url"), (String)databaseInfoMap.get("userName"), (String)databaseInfoMap.get("password"));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.DATABASE, methodInfo, RunResult.FAIL, e.getMessage());
      throw new StepException(e.getMessage());
    }
    databaseConnection.put(currentDatabase, currentConnect);
    return currentConnect;
  }

  public static List<Map<String, Object>> queryForList(String sql)
  {
    List list = new ArrayList();
    String methodInfo = "queryForList(" + sql + ")";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = currentConnect.createStatement();
      rs = stmt.executeQuery(sql);
      ResultSetMetaData rsmd = rs.getMetaData();
      while (rs.next()) {
        Map map = new HashMap();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
          map.put(rsmd.getColumnName(i), rs.getObject(i));
        }
        list.add(map);
      }
    } catch (Exception e) {
      LogModule.logStepFail(StepType.DATABASE, methodInfo, RunResult.FAIL, e.getMessage());
      closeConnection();
      throw new StepException(sql + "语句执行失败!" + e.getMessage());
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return list;
  }

  public static Map<String, Object> queryForMap(String sql)
  {
    return (Map)queryForList(sql).get(0);
  }

  public static void execute(String sql)
  {
    String methodInfo = "execute(" + sql + ")";
    Statement stmt = null;
    try {
      stmt = currentConnect.createStatement();
      stmt.executeUpdate(sql);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.DATABASE, methodInfo, RunResult.FAIL, e.getMessage());
      closeConnection();
      throw new StepException(sql + "语句执行失败!" + e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  public static void closeConnection()
  {
    closeConnection(currentDatabase);
  }

  public static void closeConnection(String database)
  {
    try
    {
      Connection connection = (Connection)databaseConnection.remove(database);
      if (connection != null)
        connection.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void closeAllConnection()
  {
    Iterator iter = databaseConnection.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry)iter.next();
      try {
        Connection connection = (Connection)entry.getValue();
        if (connection != null)
          connection.close();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    databaseConnection.clear();
  }

  private static Map<String, Map<String, String>> getDatabaseMap()
  {
    Map databaseInfoMap = new HashMap();
    Map map = new HashMap();
    if (databaseMap.isEmpty()) {
      Properties properties = FileUtil.getProperties("/database.properties");
      Iterator iter = properties.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        String databaseKey = (String)entry.getKey();
        if ((databaseKey != null) && (!databaseKey.trim().equals("")) && (databaseKey.contains("."))) {
          String[] aa = databaseKey.split("\\.");
          String databaseName = databaseKey.split("\\.")[0].trim();
          if (databaseMap.get(databaseName) == null) {
            databaseMap.put(databaseName, new HashMap());
          }
          String databaseInfoKey = databaseKey.split("\\.")[1].trim();
          String databaseInfoValue = ((String)entry.getValue()).trim();
          databaseInfoMap = (Map)databaseMap.get(databaseName);
          databaseInfoMap.put(databaseInfoKey, databaseInfoValue);

          map.put(databaseName, databaseInfoMap);
        }
      }
      databaseMap = map;
    }
    return map;
  }

  private static Map<String, List<String>> parseSqlFile(String fileName, Map<String, String> param) {
    Map sqlFileMap = new HashMap();
    List<String> fileLineList = FileUtil.readFileByLines(fileName);

    String databaseName = null;

    for (String line : fileLineList) {
      line = line.trim();
      if ((!line.isEmpty()) && (line.lastIndexOf(";") == line.length() - 1)) {
        line = line.substring(0, line.length() - 1);
      }

      if (!line.replace("\r", "").replace("\n", "").equals(""))
      {
        if (line.matches("^\\[.*\\]$")) {
          int size = line.length();
          databaseName = line.substring(1, size - 1);
        }
        else if (databaseName != null)
        {
          Pattern p = Pattern.compile("\\$[^$]*\\$");
          Matcher m = p.matcher(line);
          while (m.find())
          {
            String paramKey = m.group().replace("$", "");
            try {
              line = line.replace(m.group(), ((String)param.get(paramKey)).toString());
            } catch (Exception e) {
              LogModule.logStepFail(StepType.DATABASE, line + " SQL语句参数化处理", RunResult.FAIL, e.getMessage() + "参数名称匹配失败");
              throw new StepException(line + " SQL语句参数化处理" + e.getMessage());
            }
          }
          if (sqlFileMap.get(databaseName) == null) {
            List sqlList = new ArrayList();
            sqlList.add(line);
            sqlFileMap.put(databaseName, sqlList);
          } else {
            List sqlList = (List)sqlFileMap.get(databaseName);
            sqlList.add(line);
            sqlFileMap.put(databaseName, sqlList);
          }
        }
      }
    }
    return sqlFileMap;
  }

  public static void executeSqlFile(String fileName, Map<String, String> param) {
    Map<String,List<String>> sqlFileMap = parseSqlFile(fileName, param);
    Iterator<Entry<String, List<String>>> iter = sqlFileMap.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry<String, List<String>> entry = (Map.Entry)iter.next();
      String databaseName = (String)entry.getKey();
      setConnection(databaseName);
      for (String sql : (List<String>)entry.getValue()) {
        execute(sql);
      }
      closeConnection(databaseName);
    }
  }

  public static void main(String[] args)
  {
    String line = " [sss] ";
    line = line.trim();

    if ((!line.replace("\r", "").replace("\n", "").equals("")) || 
      (line.matches("^\\[.*\\]$"))) {
      int size = line.length();
      String databaseName = line.substring(1, size - 1);
      System.out.println(databaseName);
    }

    Pattern p = Pattern.compile("\\$[^$]*\\$");
    Matcher m = p.matcher("$$ sec=$ccc$,gg=$eee$ff");

    while (m.find()) {
      System.out.println("find方法:  " + m.group());
    }

    setConnection("TEST_UTF10GT1");
    Boolean commit = Boolean.valueOf(false);
    try {
      commit = Boolean.valueOf(currentConnect.getAutoCommit());
    }
    catch (SQLException e) {
      e.printStackTrace();
    }

    Map map = queryForMap("select * from B2B_L_AREA  where rownum <= 5");
    Map map2 = queryForMap("select * from B2B_L_AREA  where rownum <= 10");
    closeConnection("BLUESKY");
    closeConnection("TEST_UTF10GT1");
    setConnection("TEST_UTF10GT1");
    setConnection("BLUESKY");
    closeConnection();
    setConnection("BLUESKY");
    closeAllConnection();
    map2 = queryForMap("select * from B2B_L_AREA  where rownum <= 10");
  }
}