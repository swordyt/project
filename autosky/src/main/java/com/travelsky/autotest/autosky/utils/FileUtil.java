package com.travelsky.autotest.autosky.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.imageio.stream.FileImageInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.reflect.Reflection;

public class FileUtil
{
  protected static final Log logger = LogFactory.getLog(FileUtil.class);
  private static final String TEST_PROPERTIES = "/test.properties";

  public static Class<?> getCallerClass(int i)
  {
    return Reflection.getCallerClass(i);
  }

  public static byte[] getImageByteArray(String imagepath)
  {
    byte[] image = null;
    try
    {
      FileImageInputStream fiis = new FileImageInputStream(new File(imagepath));
      image = new byte[(int)fiis.length()];
      fiis.read(image);
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    return image;
  }

  public static String getFilePath(String filePath)
  {
    try
    {
      return new File(Reflection.getCallerClass(2).getResource(filePath).toURI()).getAbsolutePath();
    } catch (URISyntaxException ex) {
      throw new RuntimeException(ex);
    } catch (NullPointerException ex) {
      logger.warn("The file main/resources" + filePath + " is not exist!");
    }
    return null;
  }

  public static Properties getProperties()
  {
    return getProperties(null);
  }

  public static Properties getProperties(String filePath)
  {
    InputStream inputStream = null;
    if ((filePath == null) || ("".equals(filePath)))
      inputStream = FileUtil.class.getResourceAsStream("/test.properties");
    else
      inputStream = FileUtil.class.getResourceAsStream(filePath);
    Properties properties = new Properties();
    try {
      properties.load(inputStream);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
    return properties;
  }

  public static List<String> chooseFile(String downloadFileDir, String chooseFiles) {
    List fileList = new ArrayList();
    String[] downFileArry = chooseFiles.split(",");
    if (!downloadFileDir.endsWith(File.separator)) {
      downloadFileDir = downloadFileDir + File.separator;
    }
    File dirFile = new File(downloadFileDir);

    if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
      logger.debug("要删除的文件夹【" + downloadFileDir + "】不存在，不需要删除操作！");
      return fileList;
    }

    File[] files = dirFile.listFiles();
    for (int i = 0; i < files.length; i++) {
      boolean flag = false;
      String fileName = files[i].getName();
      for (int j = 0; j < downFileArry.length; j++) {
        String downloadFileName = downFileArry[j];
        if (fileName.equals(downloadFileName)) {
          flag = true;
          break;
        }
      }
      if (!flag) {
        fileList.add(fileName);
      }
    }
    return fileList;
  }

  public static boolean deleteDirectory(String fileDir)
  {
    if (!fileDir.endsWith(File.separator)) {
      fileDir = fileDir + File.separator;
    }
    File dirFile = new File(fileDir);

    if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
      logger.debug("删除目录失败" + fileDir + "目录不存在！");
      return false;
    }
    boolean flag = true;

    File[] files = dirFile.listFiles();
    for (int i = 0; i < files.length; i++)
    {
      if (files[i].isFile()) {
        flag = deleteFile(files[i].getAbsolutePath());
        if (!flag) {
          break;
        }
      }
      else
      {
        flag = deleteDirectory(files[i].getAbsolutePath());
        if (!flag) {
          break;
        }
      }
    }
    if (!flag) {
      logger.debug("删除目录失败");
      return false;
    }

    if (dirFile.delete()) {
      logger.debug("删除目录" + fileDir + "成功！");
      return true;
    }
    logger.debug("删除目录" + fileDir + "失败！");
    return false;
  }

  public static void exist(String directory)
  {
    try
    {
      if (!new File(directory).isDirectory())
        new File(directory).mkdir();
    }
    catch (SecurityException e) {
      e.printStackTrace();
    }
  }

  private static boolean deleteFile(String fileName)
  {
    File file = new File(fileName);
    if ((file.isFile()) && (file.exists())) {
      file.delete();
      return true;
    }
    logger.debug("删除单个文件" + fileName + "失败！");
    return false;
  }

  public static void writeFile(InputStream in, String filePath)
  {
    try
    {
      String path = filePath.substring(0, filePath.lastIndexOf("/"));
      File file = new File(path);
      if (!file.exists()) {
        file.mkdirs();
      }
      FileOutputStream fos = null;
      BufferedInputStream bis = null;
      int BUFFER_SIZE = 1024;
      byte[] buf = new byte[BUFFER_SIZE];
      int size = 0;
      bis = new BufferedInputStream(in);
      fos = new FileOutputStream(filePath, false);
      while ((size = bis.read(buf)) != -1)
        fos.write(buf, 0, size);
      fos.close();
      bis.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void readFileByBytes(String fileName)
  {
    File file = new File(fileName);
    InputStream in = null;
    try {
      System.out.println("以字节为单位读取文件内容，一次读一个字节：");

      in = new FileInputStream(file);
      int tempbyte;
      while ((tempbyte = in.read()) != -1) {
        System.out.write(tempbyte);
      }
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    try {
      System.out.println("以字节为单位读取文件内容，一次读多个字节：");

      byte[] tempbytes = new byte[100];
      int byteread = 0;
      in = new FileInputStream(fileName);
      showAvailableBytes(in);

      while ((byteread = in.read(tempbytes)) != -1)
        System.out.write(tempbytes, 0, byteread);
    }
    catch (Exception e1) {
      e1.printStackTrace();
    } finally {
      if (in != null)
        try {
          in.close();
        }
        catch (IOException e1)
        {
        }
    }
  }

  public static void readFileByChars(String fileName)
  {
    File file = new File(fileName);
    Reader reader = null;
    try {
      System.out.println("以字符为单位读取文件内容，一次读一个字节：");

      reader = new InputStreamReader(new FileInputStream(file));
      int tempchar;
      while ((tempchar = reader.read()) != -1)
      {
        if ((char)tempchar != '\r') {
          System.out.print((char)tempchar);
        }
      }
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      System.out.println("以字符为单位读取文件内容，一次读多个字节：");

      char[] tempchars = new char[30];
      int charread = 0;
      reader = new InputStreamReader(new FileInputStream(fileName));

      while ((charread = reader.read(tempchars)) != -1)
      {
        if ((charread == tempchars.length) && (tempchars[(tempchars.length - 1)] != '\r'))
          System.out.print(tempchars);
        else {
          for (int i = 0; i < charread; i++)
            if (tempchars[i] != '\r')
            {
              System.out.print(tempchars[i]);
            }
        }
      }
    }
    catch (Exception e1)
    {
      e1.printStackTrace();
    } finally {
      if (reader != null)
        try {
          reader.close();
        }
        catch (IOException e1)
        {
        }
    }
  }

  public static List<String> readFileByLines(String fileName)
  {
    List fileLineList = new ArrayList();
    File file = new File(fileName);
    BufferedReader reader = null;
    try
    {
      reader = new BufferedReader(new FileReader(file));
      String tempString = null;

      while ((tempString = reader.readLine()) != null)
      {
        fileLineList.add(tempString);
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        try {
          reader.close();
        }
        catch (IOException e1)
        {
        }
    }
    return fileLineList;
  }

  public static void readFileByRandomAccess(String fileName)
  {
    RandomAccessFile randomFile = null;
    try {
      System.out.println("随机读取一段文件内容：");

      randomFile = new RandomAccessFile(fileName, "r");

      long fileLength = randomFile.length();

      int beginIndex = fileLength > 4L ? 4 : 0;

      randomFile.seek(beginIndex);
      byte[] bytes = new byte[10];
      int byteread = 0;

      while ((byteread = randomFile.read(bytes)) != -1)
        System.out.write(bytes, 0, byteread);
    }
    catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (randomFile != null)
        try {
          randomFile.close();
        }
        catch (IOException e1)
        {
        }
    }
  }

  private static void showAvailableBytes(InputStream in)
  {
    try
    {
      System.out.println("当前字节输入流中的字节数为:" + in.available());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void appendMethodA(String fileName, String content)
  {
    try
    {
      RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");

      long fileLength = randomFile.length();

      randomFile.seek(fileLength);
      randomFile.writeBytes(content);
      randomFile.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void appendMethodB(String fileName, String content)
  {
    try
    {
      FileWriter writer = new FileWriter(fileName, true);
      writer.write(content);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String fileName = "C:/temp/newTemp.txt";
    String content = "new append!";

    readFileByBytes(fileName);
    readFileByChars(fileName);
    readFileByLines(fileName);
    readFileByRandomAccess(fileName);

    appendMethodA(fileName, content);
    appendMethodA(fileName, "append end. \n");

    readFileByLines(fileName);

    appendMethodB(fileName, content);
    appendMethodB(fileName, "append end. \n");

    readFileByLines(fileName);
  }
}