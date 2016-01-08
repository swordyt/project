package com.travelsky.autotest.autosky.utils;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShotUtil
{
  private static String BASE_DIR = "C:\\autosky_log\\";
  private static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

  public static String screenShotByDriver(WebDriver driver, String suitePath) {
    String picPath = "";

    String screenShotPath = createScreenShotPath(suitePath);
    String time = DateUtil.dateToStr(new Date(), "yyyyMMdd-HHmmss");
    try {
      File source_file = (File)((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
      File file = new File(screenShotPath + File.separator + time + ".png");
      FileUtils.copyFile(source_file, file);
      return file.getAbsolutePath();
    } catch (Exception e) {
    }
    return screenShotByDesktop(suitePath);
  }

  public static String screenShotByDesktop(String suitePath)
  {
    String picPath = "";
    String time = DateUtil.dateToStr(new Date(), "yyyyMMdd-HHmmss");
    String screenShotPath = createScreenShotPath(suitePath);
    try {
      BufferedImage screen = new Robot().createScreenCapture(new Rectangle(0, 0, (int)d.getWidth(), (int)d.getHeight()));
      String name = screenShotPath + "/" + time + ".png";
      File file = new File(name);
      ImageIO.write(screen, "png", file);
      picPath = file.getAbsolutePath();
    }
    catch (Exception e) {
      System.out.println("截图失败！！！\n" + e.getMessage());
    }
    return picPath;
  }

  private static String createScreenShotPath(String suitePath) {
    Properties properties = FileUtil.getProperties();
    BASE_DIR = properties.getProperty("logDir", BASE_DIR) == null ? BASE_DIR : properties.getProperty("logPath", BASE_DIR);
    if (!BASE_DIR.endsWith("/")) {
      BASE_DIR += "/";
    }
    String screenShotPath = BASE_DIR;
    screenShotPath = suitePath + "screenshot";

    if (!new File(screenShotPath).isDirectory()) {
      new File(screenShotPath).mkdir();
    }
    return screenShotPath;
  }
}