package com.travelsky.autotest.autosky.locators;

import com.travelsky.autotest.autosky.browser.Browser;
import com.travelsky.autotest.autosky.browser.Container;
import com.travelsky.autotest.autosky.elements.Element;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementLocator
{
  public String id;
  public String name;
  public String xpath;
  public String className;
  public WebElement element;
  public WebDriver driver;
  public Container container;/*代表该元素的上一元素*/
  public HashMap<String, Object> options;
  public HashMap<String, Object> regexpOptions = new HashMap();
  public HashMap<String, Object> otherOptions = new HashMap();
  public static By by;

  public ElementLocator(Container container, HashMap<String, Object> options)
  {
    this.container = container;
    this.options = ((HashMap)options.clone());
  }

  public ElementLocator()
  {
  }

  private Boolean checkElementProperty() {
    return Boolean.valueOf(true);
  }

  public WebElement locate()
  {
    this.otherOptions = ((HashMap)getRegexpOptions(this.options));
    if (this.regexpOptions.size() > 0)
    {
      List webElementList = new ArrayList();
      int index = 1;
      /*判断是否包含index标签*/
      if (this.otherOptions.containsKey("index")) {
        index = Integer.parseInt(this.otherOptions.remove("index").toString());
      }
      if (this.otherOptions.size() == 0) {
        webElementList = allElements();
      } else if (this.otherOptions.size() == 1) {
        Iterator iter = this.otherOptions.entrySet().iterator();
        while (iter.hasNext()) {
          Map.Entry entry = (Map.Entry)iter.next();
          String key = (String)entry.getKey();
          String value = (String)entry.getValue();
          if (value.indexOf("|") > 0)
            webElementList = findAllByMultiple(this.otherOptions);
          else
            webElementList = findAllByOne(key, value);
        }
      }
      else {
        webElementList = findAllByMultiple(this.otherOptions);
      }

      this.element = matchedElement(webElementList, index);
    }
    else if (this.options.size() == 1) {
      Iterator iter = this.options.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        String key = (String)entry.getKey();
        String value = (String)entry.getValue();
        if (key.contentEquals("index"))
          this.element = ((WebElement)allElements().get(Integer.parseInt(value) - 1));
        else
          this.element = findFirstByOne(key, value);
      }
    }
    else
    {
      this.element = findFirstByMultiple(this.options);
    }

    return this.element;
  }
/*通过正则表达式确定元素*/
  private WebElement matchedElement(List<WebElement> webElementList, int index) {
    WebElement matchedElement = null;
    List matchedElementList = new ArrayList();
    for (WebElement webElement : webElementList) {
      Boolean matched = Boolean.valueOf(false);
      Iterator iter = this.regexpOptions.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        String key = (String)entry.getKey();
        Pattern value = (Pattern)entry.getValue();

        String actualValue = "";
        if (key.equals("text")) {
          actualValue = webElement.getText();
        }
        else if (key.equals("suffixText")) {
          JavascriptExecutor js = (JavascriptExecutor)this.container.driver;
          actualValue = (String)js.executeScript("var nextSibling = arguments[0].nextSibling; while(nextSibling && nextSibling.nodeType != 3){nextSibling = nextSibling.nextSibling;}return nextSibling.nodeValue;", new Object[] { webElement });
        }
        else if (key.equals("prefixText")) {
          JavascriptExecutor js = (JavascriptExecutor)this.container.driver;
          actualValue = (String)js.executeScript("var previousSibling = arguments[0].previousSibling; while(previousSibling && previousSibling.nodeType != 3){previousSibling = previousSibling.previousSibling;}return previousSibling.nodeValue;", new Object[] { webElement });
        }
        else
        {
          actualValue = webElement.getAttribute(key);
        }

        if (actualValue != null)
        {
          Matcher matcher = value.matcher(actualValue);
          if ((matcher.matches()) || (matcher.find())) {
            matched = Boolean.valueOf(true);
          } else {
            matched = Boolean.valueOf(false);
            break;
          }
        }
      }
      if (matched.booleanValue()) {
        if (matchedElementList.size() == index - 1) {
          matchedElement = webElement;
          break;
        }
        matchedElementList.add(webElement);
      }
    }
    return matchedElement;
  }

  private List<WebElement> matchedElementList(List<WebElement> webElementList) {
    List matchedElementList = new ArrayList();
    for (WebElement webElement : webElementList) {
      Boolean matched = Boolean.valueOf(false);
      Iterator iter = this.regexpOptions.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        String key = (String)entry.getKey();
        Pattern value = (Pattern)entry.getValue();

        Matcher matcher = value.matcher(webElement.getAttribute(key));
        if (matcher.matches()) {
          matched = Boolean.valueOf(true);
        } else {
          matched = Boolean.valueOf(false);
          break;
        }
      }
      if (matched.booleanValue()) {
        matchedElementList.add(webElement);
      }
    }
    return matchedElementList;
  }

  private Map<String, Object> getRegexpOptions(HashMap<String, Object> selectors)
  {
    this.regexpOptions.clear();
    this.otherOptions.clear();
    Iterator<Entry<String, Object>>  iter = selectors.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry<String, Object> entry = (Map.Entry)iter.next();
      if ((entry.getValue() instanceof Pattern)) {
        this.regexpOptions.put(entry.getKey(), entry.getValue());
      }
      else {
        this.otherOptions.put(entry.getKey(), entry.getValue());
      }
    }

    return this.otherOptions;
  }

  public String buildXpath(HashMap<String, Object> options)
  {
    HashMap selectors = (HashMap)options.clone();
    if (selectors.get("xpath") != null) {
      return selectors.get("xpath").toString();
    }
    String xpath = "";
    Object tagName = selectors.remove("tagName");
    if (tagName == null) {
      xpath = ".//*";

      if (selectors.size() >= 1) {
        xpath = xpath + "[";
        Iterator iter = selectors.entrySet().iterator();
        int n = 0;

        while (iter.hasNext())
        {
          if (n > 0) {
            xpath = xpath + " and ";
          }
          Map.Entry entry = (Map.Entry)iter.next();
          String key = (String)entry.getKey();
          Object value = entry.getValue();
          xpath = xpath + equalPair(key, value);
          n++;
        }

      }

      xpath = xpath + "]";
    }
    else {
      String[] tagNames = tagName.toString().split("\\|");
      for (String tag : tagNames) {
        if (xpath.length() > 0) {
          xpath = xpath + "|";
        }
        xpath = xpath + ".//" + tag;

        if (selectors.size() >= 1) {
          Iterator iter = selectors.entrySet().iterator();

          xpath = xpath + "[";
          while (iter.hasNext()) {
            if (!xpath.endsWith("[")) {
              xpath = xpath + " and ";
            }
            Map.Entry entry = (Map.Entry)iter.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            xpath = xpath + equalPair(key, value);
          }

          xpath = xpath + "]";
        }
      }

    }

    return xpath;
  }

  private String formate(String key) {
    String formateKey = "";
    if (key.equals("text"))
      formateKey = "normalize-space()";
    else if (key.equals("href"))
      formateKey = "normalize-space(@href)";
    else {
      formateKey = "@" + key.replace('_', '-');
    }
    return formateKey;
  }

  private String equalPair(String key, Object value) {
    String pair = "";

    if ((key.equals("className")) || (key.equals("class")))
    {
      pair = " @class='" + value.toString() + "'";
    } else if (key.equals("text"))
      pair = "normalize-space()='" + value.toString() + "'";
    else if (key.equals("suffixText"))
      pair = "contains(following-sibling::text()[1],'" + value.toString() + "')";
    else if (key.equals("prefixText")) {
      pair = "contains(preceding-sibling::text()[1],'" + value.toString() + "')";
    }
    else
    {
      pair = "@" + key + "='" + value.toString() + "'";
    }

    return pair;
  }

  public WebElement byId() {
    this.element = this.container.driver.findElement(By.id((String)this.options.get(this.id)));
    return this.element;
  }

  public WebElement findFirstByMultiple(HashMap<String, Object> options)
  {
    HashMap multiple = (HashMap)options.clone();
    Object idx = multiple.remove("index");
    String xpath = buildXpath(multiple);
    if (idx == null)
      this.element = findElement(By.xpath(xpath));
    else {
      this.element = ((WebElement)findElements(By.xpath(xpath)).get(Integer.parseInt(idx.toString()) - 1));
    }
    return this.element;
  }

  public List<WebElement> findAllByMultiple(HashMap<String, Object> options)
  {
    HashMap multiple = (HashMap)options.clone();
    List elementList = new ArrayList();
    String xpath = buildXpath(multiple);
    elementList = findElements(By.xpath(xpath));
    return elementList;
  }

  public WebElement findFirstByOne(String key, String value)
  {
    if ("class".equals(key)) {
      key = "className";
    }
    Method byMethod = null;
    try {
      byMethod = By.class.getMethod(key, new Class[] { String.class });
    }
    catch (SecurityException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchMethodException e) {
      HashMap selectors = new HashMap();
      selectors.put(key, value);
      value = buildXpath(selectors);
      try {
        byMethod = By.class.getMethod("xpath", new Class[] { String.class });
      }
      catch (SecurityException e1) {
        e1.printStackTrace();
      }
      catch (NoSuchMethodException e1) {
        e1.printStackTrace();
      }
    }
    try
    {
      findElement((By)byMethod.invoke(By.class, new Object[] { value }));
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return this.element;
  }

  public List<WebElement> findAllByOne(String key, String value)
  {
    List elementList = new ArrayList();
    if ("class".equals(key)) {
      key = "className";
    }
    Method byMethod = null;
    try {
      byMethod = By.class.getMethod(key, new Class[] { String.class });
    }
    catch (SecurityException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchMethodException e) {
      HashMap selectors = new HashMap();
      selectors.put(key, value);
      value = buildXpath(selectors);
      try {
        byMethod = By.class.getMethod("xpath", new Class[] { String.class });
      }
      catch (SecurityException e1) {
        e1.printStackTrace();
      }
      catch (NoSuchMethodException e1) {
        e1.printStackTrace();
      }
    }
    try
    {
      elementList = findElements((By)byMethod.invoke(By.class, new Object[] { value }));
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return elementList;
  }

  private List<WebElement> allElements() {
    List allElements = new ArrayList();
    allElements = findElements(By.xpath(".//*"));
    return allElements;
  }

  private List<WebElement> findElements(By by) {
    List elements = new ArrayList();
    if ((this.container instanceof Browser))
      elements = this.container.driver.findElements(by);
    if ((this.container instanceof Element)) {
      if (this.container.isFrame)
        elements = this.container.driver.findElements(by);
      else {
        elements = this.container.current.findElements(by);
      }
    }
    return elements;
  }

  private WebElement findElement(By by)
  {
    if ((this.container instanceof Browser))
      this.element = this.container.driver.findElement(by);
    if ((this.container instanceof Element)) {
      if (this.container.isFrame)
        this.element = this.container.driver.findElement(by);
      else
        this.element = this.container.current.findElement(by);
    }
    return this.element;
  }
}