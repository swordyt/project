package com.travelsky.autotest.autosky.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClassUtil
{
  private static final Log log = LogFactory.getLog(ClassUtil.class);
  private static String classPath = "";

  public static Set<Class<?>> getClasses(String path) throws IOException {
    classPath = path;

    Set classes = new LinkedHashSet();
    findAndAddClassesInPath(path, classes);
    return classes;
  }

  public static void findAndAddClassesInPath(String Path, Set<Class<?>> classes) throws IOException
  {
    File dir = new File(Path);

    if ((!dir.exists()) || (!dir.isDirectory())) {
      return;
    }

    File[] dirfiles = dir.listFiles(new FileFilter()
    {
      public boolean accept(File file) {
        return (file.isDirectory()) || (file.getName().endsWith(".class"));
      }
    });
    for (File file : dirfiles)
    {
      if (file.isDirectory()) {
        findAndAddClassesInPath(file.getAbsolutePath(), classes);
      }
      else
      {
        FileInputStream classIs = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4];

        for (int i = 0; (i = classIs.read(buf)) != -1; ) {
          baos.write(buf, 0, i);
        }

        byte[] data = baos.toByteArray();
        String className = classNameAnalyzer(data);
        try
        {
          classes.add(Class.forName(className));
        } catch (ClassNotFoundException e) {
          log.error("添加用户自定义视图类错误 找不到此类的.class文件");
          e.printStackTrace();
        }
      }
    }
  }

  public static String classNameAnalyzer(byte[] data)
  {
    HashMap constIndex = new HashMap();

    HashMap stringIndex = new HashMap();

    short dataIndex = 10;
    byte constType = data[dataIndex];
    for (short index = 1; index < getShort(new byte[] { data[8], data[9] }, false); index = (short)(index + 1)) {
      switch (constType) {
      case 1:
        short d = getShort(new byte[] { data[(dataIndex + 1)], data[(dataIndex + 2)] }, false);
        stringIndex.put(Short.valueOf(index), new String(data, dataIndex + 3, d));
        dataIndex = (short)(dataIndex + (d + 3));
        constType = data[dataIndex];
        break;
      case 3:
        dataIndex = (short)(dataIndex + 5);
        constType = data[dataIndex];
        break;
      case 4:
        dataIndex = (short)(dataIndex + 5);
        constType = data[dataIndex];
        break;
      case 5:
        index = (short)(index + 1);
        dataIndex = (short)(dataIndex + 9);
        constType = data[dataIndex];
        break;
      case 6:
        index = (short)(index + 1);
        dataIndex = (short)(dataIndex + 9);
        constType = data[dataIndex];
        break;
      case 7:
        constIndex.put(Short.valueOf(index), Short.valueOf(getShort(new byte[] { data[(dataIndex + 1)], data[(dataIndex + 2)] }, false)));
        dataIndex = (short)(dataIndex + 3);
        constType = data[dataIndex];
        break;
      case 8:
        constIndex.put(Short.valueOf(index), Short.valueOf(getShort(new byte[] { data[(dataIndex + 1)], data[(dataIndex + 2)] }, false)));
        dataIndex = (short)(dataIndex + 3);
        constType = data[dataIndex];
        break;
      case 9:
        dataIndex = (short)(dataIndex + 5);
        constType = data[dataIndex];
        break;
      case 10:
        dataIndex = (short)(dataIndex + 5);
        constType = data[dataIndex];
        break;
      case 11:
        dataIndex = (short)(dataIndex + 5);
        constType = data[dataIndex];
        break;
      case 12:
        dataIndex = (short)(dataIndex + 5);
        constType = data[dataIndex];
        break;
      case 15:
        dataIndex = (short)(dataIndex + 4);
        constType = data[dataIndex];
        break;
      case 16:
        dataIndex = (short)(dataIndex + 4);
        constType = data[dataIndex];
        break;
      case 18:
        dataIndex = (short)(dataIndex + 5);
        constType = data[dataIndex];
        break;
      case 2:
      case 13:
      case 14:
      case 17:
      default:
        throw new RuntimeException("Invalid constant pool flag: " + constType);
      }
    }

    Short indexOfThisClass = Short.valueOf(getShort(new byte[] { data[(dataIndex + 2)], data[(dataIndex + 3)] }, false));

    if (!constIndex.containsKey(indexOfThisClass)) {
      throw new RuntimeException("class文件解析错误,获取当前类全限定名index错误");
    }

    short resultIndex = ((Short)constIndex.get(indexOfThisClass)).shortValue();
    if (!stringIndex.containsKey(Short.valueOf(resultIndex))) {
      throw new RuntimeException("class文件解析错误，,获取当前类全限定名Stringindex错误");
    }

    return ((String)stringIndex.get(Short.valueOf(resultIndex))).replace("/", ".");
  }

  public static short getShort(byte[] buf, boolean asc)
  {
    if (buf == null) {
      throw new IllegalArgumentException("byte array is null!");
    }
    if (buf.length > 2) {
      throw new IllegalArgumentException("byte array size > 2 !");
    }
    short r = 0;
    if (asc)
      for (int i = buf.length - 1; i >= 0; i--) {
        r = (short)(r << 8);
        r = (short)(r | buf[i] & 0xFF);
      }
    else
      for (int i = 0; i < buf.length; i++) {
        r = (short)(r << 8);
        r = (short)(r | buf[i] & 0xFF);
      }
    return r;
  }

  public static String[] getPackageAllClassName(String classLocation, String packageName)
  {
    String[] packagePathSplit = packageName.split("[.]");
    String realClassLocation = classLocation;
    int packageLength = packagePathSplit.length;
    for (int i = 0; i < packageLength; i++) {
      realClassLocation = realClassLocation + File.separator + packagePathSplit[i];
    }
    File packeageDir = new File(realClassLocation);
    if (packeageDir.isDirectory()) {
      String[] allClassName = packeageDir.list();
      return allClassName;
    }
    return null;
  }

  public static Set<Class<?>> getClasses(Package pack)
  {
    Set classes = new LinkedHashSet();

    boolean recursive = true;

    String packageName = pack.getName();
    String packageDirName = packageName.replace('.', '/');
    try
    {
      Enumeration dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

      URL url = (URL)dirs.nextElement();

      String protocol = url.getProtocol();

      if ("file".equals(protocol))
      {
        String filePath = URLDecoder.decode(url.getFile(), "UTF-8");

        findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
      } else if ("jar".equals(protocol))
      {
        try
        {
          JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();

          Enumeration entries = jar.entries();

          while (entries.hasMoreElements())
          {
            JarEntry entry = (JarEntry)entries.nextElement();
            String name = entry.getName();

            if (name.charAt(0) == '/')
            {
              name = name.substring(1);
            }

            if (name.startsWith(packageDirName)) {
              int idx = name.lastIndexOf(47);

              if (idx != -1)
              {
                packageName = name.substring(0, idx).replace('/', '.');
              }

              if ((idx != -1) || (recursive))
              {
                if ((name.endsWith(".class")) && (!entry.isDirectory()))
                {
                  String className = name.substring(packageName.length() + 1, name.length() - 6);
                  try
                  {
                    classes.add(Class.forName(packageName + '.' + className));
                  } catch (ClassNotFoundException e) {
                    log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                  }
                }
              }
            }
          }
        } catch (IOException e) {
          log.error("在扫描用户定义视图时从jar包获取文件出错");
          e.printStackTrace();
        }
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    return classes;
  }

  public  static void findAndAddClassesInPackageByFile(String packageName, String packagePath,final boolean recursive, Set<Class<?>> classes)
  {
    File dir = new File(packagePath);

    if ((!dir.exists()) || (!dir.isDirectory())) {
      log.warn("用户定义包名 " + packageName + " 下没有任何文件");
      return;
    }

    File[] dirfiles = dir.listFiles(new FileFilter()
    {
      public boolean accept(File file) { 
        return ((recursive) && (file.isDirectory())) || (file.getName().endsWith(".class"));
      }
    });
    for (File file : dirfiles)
    {
      if (file.isDirectory()) {
        findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
      }
      else {
        String className = file.getName().substring(0, file.getName().length() - 6);
        try
        {
          classes.add(Class.forName(packageName + '.' + className));
        } catch (ClassNotFoundException e) {
          log.error("添加用户自定义视图类错误 找不到此类的.class文件");
          e.printStackTrace();
        }
      }
    }
  }
}