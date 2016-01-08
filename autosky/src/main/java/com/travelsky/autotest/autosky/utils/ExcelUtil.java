package com.travelsky.autotest.autosky.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil
{
  private int totalRows = 0;

  private int totalCells = 0;
  private String errorInfo;
  private int sheetIndex = 0;

  private static boolean isExcel2003(String filePath)
  {
    return filePath.matches("^.+\\.(?i)(xls)$");
  }

  private static boolean isExcel2007(String filePath)
  {
    return filePath.matches("^.+\\.(?i)(xlsx)$");
  }

  private int getTotalRows()
  {
    return this.totalRows;
  }

  private int getTotalCells()
  {
    return this.totalCells;
  }

  private String getErrorInfo()
  {
    return this.errorInfo;
  }

  public boolean validateExcel(String filePath)
  {
    if ((filePath == null) || ((!isExcel2003(filePath)) && (!isExcel2007(filePath))))
    {
      this.errorInfo = "文件名不是excel格式";

      return false;
    }

    System.out.println(filePath);

    File file = new File(filePath);

    if ((file == null) || (!file.exists()))
    {
      this.errorInfo = "文件不存在";

      return false;
    }

    return true;
  }

  public List<List<String>> read(String filePath, String sheetName)
  {
    List dataLst = new ArrayList();

    InputStream is = null;

    if (!validateExcel(filePath)) {
      System.out.println(this.errorInfo);
      return null;
    }

    boolean isExcel2003 = true;

    if (isExcel2007(filePath))
    {
      isExcel2003 = false;
    }

    try
    {
      File file = new File(filePath);

      is = new FileInputStream(file);

      Workbook wb = null;

      if (isExcel2003)
        wb = new HSSFWorkbook(is);
      else {
        wb = new XSSFWorkbook(is);
      }
      dataLst = read(wb, sheetName);

      is.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (is != null)
      {
        try
        {
          is.close();
        }
        catch (IOException e)
        {
          is = null;

          e.printStackTrace();
        }

      }

    }

    return dataLst;
  }

  public List<List<String>> read(InputStream inputStream, boolean isExcel2003)
  {
    List dataLst = null;
    try
    {
      Workbook wb = null;

      if (isExcel2003)
        wb = new HSSFWorkbook(inputStream);
      else {
        wb = new XSSFWorkbook(inputStream);
      }
      dataLst = read(wb);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return dataLst;
  }

  private List<List<String>> read(Workbook wb, int sheetIndex)
  {
    List dataLst = new ArrayList();

    Sheet sheet = wb.getSheetAt(sheetIndex);

    this.totalRows = sheet.getPhysicalNumberOfRows();

    if ((this.totalRows >= 1) && (sheet.getRow(0) != null))
    {
      this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
    }

    for (int r = 0; r < this.totalRows; r++)
    {
      Row row = sheet.getRow(r);

      if (row != null)
      {
        List rowLst = new ArrayList();

        for (int c = 0; c < getTotalCells(); c++)
        {
          Cell cell = row.getCell(c);

          String cellValue = "";

          if (null != cell)
          {
            switch (cell.getCellType())
            {
            case 0:
              if (HSSFDateUtil.isCellDateFormatted(cell))
              {
                Date date = cell.getDateCellValue();

                cellValue = DateUtil.dateToStr(date, "yyyy-MM-dd HH:mm:ss").toString();
                System.out.println(cellValue);
              }
              else
              {
                Integer num = new Integer((int)cell.getNumericCellValue());
                cellValue = String.valueOf(num);
              }

              break;
            case 1:
              cellValue = cell.getStringCellValue().trim();
              break;
            case 4:
              cellValue = cell.getBooleanCellValue() + "";
              break;
            case 2:
              cellValue = cell.getCellFormula() + "";
              break;
            case 3:
              cellValue = "";
              break;
            case 5:
              cellValue = "非法字符";
              break;
            default:
              cellValue = "未知类型";
            }

          }

          rowLst.add(cellValue);
        }

        dataLst.add(rowLst);
      }
    }

    return dataLst;
  }

  private List<List<String>> read(Workbook wb, String sheetName)
  {
    int sheetIndex = 0;
    try {
      sheetIndex = wb.getSheetIndex(sheetName);
    }
    catch (Exception e)
    {
    }
    if (sheetIndex < 0) {
      sheetIndex = 0;
    }
    return read(wb, sheetIndex);
  }

  private List<List<String>> read(Workbook wb) {
    return read(wb, 0);
  }

  public static List<Map<String, String>> reflectMapList(List<List<String>> list)
  {
    List mlist = new ArrayList();

    Map map = new HashMap();
    if (list != null)
    {
      for (int i = 1; i < list.size(); i++) {
        map = new HashMap();
        //获取其中某行的数据
        List cellList = (List)list.get(i);
        //将每行数据与第一行标题对应放入map中
        for (int j = 0; j < cellList.size(); j++) {
          map.put(((List)list.get(0)).get(j), cellList.get(j));
        }
        mlist.add(map);
      }

    }

    return mlist;
  }

  public List<Map<String, String>> excelDatas(String filePath, String sheetName) {
    List lists = read(filePath, sheetName); //得到excl表其中某个sheet的数据

    List datas = reflectMapList(lists);
    return datas;
  }
}