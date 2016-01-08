package com.travelsky.autotest.autosky.elements;

import com.travelsky.autotest.autosky.browser.Browser;
import com.travelsky.autotest.autosky.browser.Container;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import com.travelsky.autotest.autosky.locators.ElementLocator;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IBody;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IButton;
import com.travelsky.autotest.autosky.pagefactory.interfaces.ICell;
import com.travelsky.autotest.autosky.pagefactory.interfaces.ICheckbox;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IDiv;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IElement;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IFileField;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IForm;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IFrame;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IInput;
import com.travelsky.autotest.autosky.pagefactory.interfaces.ILink;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IOption;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IRadio;
import com.travelsky.autotest.autosky.pagefactory.interfaces.IRow;
import com.travelsky.autotest.autosky.pagefactory.interfaces.ISelect;
import com.travelsky.autotest.autosky.pagefactory.interfaces.ISpan;
import com.travelsky.autotest.autosky.pagefactory.interfaces.ITable;
import com.travelsky.autotest.autosky.pagefactory.interfaces.ITextField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Element extends Container
  implements IBody, IButton, ICell, ICheckbox, IDiv, IElement, IFileField, IForm, IFrame, IInput, ILink, IOption, IRadio, IRow, ISelect, ISpan, ITable, ITextField
{
  protected ElementLocator elementLocator;
  protected HashMap<String, Object> options;
  public String border;
  public static int TIMEOUT = 60;
  public Container browser;

  public Element(Container container, HashMap<String, Object> options)
  {
    this.container = container;
    this.methodInfo = ((String)options.remove("methodInfo"));
    this.options = options;
  }

  public Element locate()
  {
    this.browser = this.container;
    /*使用Browser直接使用方法：预估*/
    while (!(this.browser instanceof Browser)) {
      this.browser = this.browser.container;
    }
/*使用element作为属性搜索元素*/
    if ((this.options.get("element") != null) && ((this.options.get("element") instanceof WebElement))) {
      this.current = ((WebElement)this.options.get("element"));
      if (this.methodInfo == null) {
        this.methodInfo = (this.container.methodInfo + ".(\"element=>" + this.current + "\")");
      }
    }
    else if (this.current == null) {
      try {
    	  /*将操作的浏览器对象和属性map复制给locator*/
        this.elementLocator = new ElementLocator(this.container.wd(), this.options);
        this.current = ((WebElement)new WebDriverWait(this.container.driver, TIMEOUT).until(new ExpectedCondition()
        {
          public Object apply(Object input) {
            return Element.this.elementLocator.locate();
          }
        }));
        this.methodInfo = (this.container.methodInfo + "." + this.methodInfo);
      } catch (Exception e) {
        this.driver = this.container.driver;
        this.methodInfo = (this.container.methodInfo + "." + this.methodInfo);
        this.current.click();
        if (this.container.errorInfo == null)
          this.errorInfo = (this.methodInfo + "定位失败！" + e.getMessage());
        else {
          this.errorInfo = this.container.errorInfo;
        }
      }

    }

    this.driver = this.container.driver;
    if ((this.current != null) && (this.current.getAttribute("tagName") != null) && ((this.current.getAttribute("tagName").toLowerCase().equals("iframe")) || (this.current.getAttribute("tagName").toLowerCase().equals("frame"))))
    {
      this.isFrame = true;
      this.browser.hasFrame = true;
      this.driver.switchTo().frame(this.current);
    }
    return this;
  }

  public Container wd()
  {
    locate();
    return this;
  }

  protected Boolean assertExists()
  {
    locate();
    return Boolean.valueOf(this.current != null);
  }

  public Boolean exists(int timeout)
  {
    int temp = TIMEOUT;
    TIMEOUT = timeout;
    Boolean exists = assertExists();
    TIMEOUT = temp;
    return exists;
  }

  public Boolean exists()
  {
    int temp = TIMEOUT;
    TIMEOUT = 1;
    Boolean exists = assertExists();
    TIMEOUT = temp;
    return exists;
  }

  public String text()
  {
    String text = null;
    this.methodInfo += ".text()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try
    {
      text = this.current.getText();
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return text;
  }

  public String tagName()
  {
    String tagName = null;
    this.methodInfo += ".tagName()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      tagName = this.current.getTagName();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return tagName;
  }

  public String className()
  {
    String className = null;
    this.methodInfo += ".className()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      className = this.current.getAttribute("class");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return className;
  }

  public String id()
  {
    String id = null;
    this.methodInfo += ".id()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      id = this.current.getAttribute("id");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return id;
  }

  public String outerHTML()
  {
    String outerHTML = null;
    this.methodInfo += ".outerHTML()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      outerHTML = (String)executeJs("return arguments[0].outerHTML;", new Object[] { this.current });
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return outerHTML;
  }

  public String getStyle(String name)
  {
    String style = null;
    this.methodInfo = (this.methodInfo + ".getStyle(\"" + name + "\")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      style = this.current.getCssValue(name);
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return style;
  }

  public String getAttribute(String name)
  {
    String attributeValue = null;
    this.methodInfo = (this.methodInfo + ".getAttribute(\"" + name + "\")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try
    {
      attributeValue = this.current.getAttribute(name);
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return attributeValue;
  }

  public Boolean enable()
  {
    Boolean enable = null;
    this.methodInfo += ".enable()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      enable = Boolean.valueOf(this.current.isEnabled());
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return enable;
  }

  public Boolean enable(int timeout)
  {
    Boolean enable = Boolean.valueOf(false);
    this.methodInfo = (this.methodInfo + ".enable(" + timeout + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      enable = (Boolean)new WebDriverWait(this.container.driver, timeout).until(new ExpectedCondition()
      {
        public Boolean apply(Object input) {
          return Boolean.valueOf(Element.this.current.isEnabled());
        }
 } );
    } catch (TimeoutException e) {
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return enable;
  }

  public Boolean visible()
  {
    Boolean visible = null;
    this.methodInfo += ".visible()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      visible = Boolean.valueOf(this.current.isDisplayed());
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return visible;
  }

  public Boolean visible(int timeout)
  {
    Boolean visible = Boolean.valueOf(false);
    this.methodInfo = (this.methodInfo + ".visible(" + timeout + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      visible = (Boolean)new WebDriverWait(this.container.driver, timeout).until(new ExpectedCondition()
      {
        public Boolean apply(Object input) {
          return Boolean.valueOf(Element.this.current.isDisplayed());
        }
		} );
    } catch (TimeoutException e) {
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return visible;
  }

  public Boolean invisible(int timeout)
  {
    Boolean invisible = Boolean.valueOf(false);
    this.methodInfo = (this.methodInfo + ".invisible(" + timeout + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      invisible = (Boolean)new WebDriverWait(this.container.driver, timeout).until(new ExpectedCondition()
      {
        public Boolean apply(Object input) {
          return Boolean.valueOf(!Element.this.current.isDisplayed());
        }
		} );
    } catch (TimeoutException e) {
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return invisible;
  }

  public Element parent(String options)
  {
    Element parent = null;
    this.methodInfo = (this.methodInfo + ".parent(\"" + options + "\")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      HashMap selectors = toHash(options);
      selectors.remove("methodInfo");
      WebElement webElement = (WebElement)executeJs("return arguments[0].parentNode;", new Object[] { this.current });
      Boolean find = Boolean.valueOf(false);
      String actual = null;
      while (!"html".equals(webElement.getTagName())) {
        Iterator itr = selectors.entrySet().iterator();
        while (itr.hasNext()) {
          Map.Entry er = (Map.Entry)itr.next();
          if ((er.getValue() instanceof String))
          {
            actual = webElement.getAttribute(er.getKey().toString());
            if (er.getKey().toString().equals("tagName")) {
              actual = actual.toLowerCase();
            }
            if (actual != null)
            {
              if (er.getValue().toString().equals(actual))
                find = Boolean.valueOf(true);
              else
                find = Boolean.valueOf(false);
            }
          } else if ((er.getValue() instanceof Pattern)) {
            Pattern value = (Pattern)er.getValue();
            actual = webElement.getAttribute(er.getKey().toString());
            if (er.getKey().toString().equals("tagName")) {
              actual = actual.toLowerCase();
            }
            if (actual != null)
            {
              Matcher matcher = value.matcher(actual);
              if ((matcher.matches()) || (matcher.find())) {
                find = Boolean.valueOf(true);
              } else {
                find = Boolean.valueOf(false);
                break;
              }
            }
          }
        }
        if (find.booleanValue()) break;
        webElement = (WebElement)executeJs("return arguments[0].parentNode;", new Object[] { webElement });
      }

      if (!find.booleanValue()) {
        throw new Exception("找不到父控件");
      }
      selectors.put("element", webElement);
      selectors.put("methodInfo", this.methodInfo);
      parent = new Element(this.container, selectors).locate();
    }
    catch (Exception e)
    {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return parent;
  }

  public Element parent()
  {
    Element parent = null;
    this.methodInfo += ".parent()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      if (!"html".equals(this.current.getTagName())) {
        WebElement webElement = (WebElement)executeJs("return arguments[0].parentNode;", new Object[] { this.current });
        HashMap selectors = new HashMap();
        selectors.put("element", webElement);
        selectors.put("methodInfo", this.methodInfo);
        parent = new Element(this.container, selectors).locate();
      }
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return parent;
  }

  public List<Element> children()
  {
    List elementList = new ArrayList();
    List<WebElement> webElementList = new ArrayList();
    this.methodInfo += ".children()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      HashMap selectors = toHash("xpath=>./*");
      webElementList = new ElementLocator(this, selectors).findAllByMultiple(selectors);
      int i;
      i = 0;
      for (WebElement webElement : webElementList) {
        HashMap elementselectors = new HashMap();
        elementselectors.put("element", webElement);
        elementselectors.put("methodInfo", this.methodInfo + ".get(" + i + ")");
        elementList.add(new Element(this.container, elementselectors).locate());
        i++;
      }
    }
    catch (Exception e)
    {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return elementList;
  }

  public Element previous()
  {
    Element previous = null;
    this.methodInfo += ".previous()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }

    try
    {
      WebElement element = (WebElement)executeJs("var previousSibling = arguments[0].previousSibling; while(previousSibling && previousSibling.nodeType != 1){previousSibling = previousSibling.previousSibling;}return previousSibling;", new Object[] { this.current });

      HashMap selectors = new HashMap();
      selectors.put("element", element);
      selectors.put("methodInfo", this.methodInfo);
      previous = new Element(this.container, selectors).locate();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return previous;
  }

  public Element next()
  {
    Element next = null;
    this.methodInfo += ".next()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try
    {
      WebElement element = (WebElement)executeJs("var nextSibling = arguments[0].nextSibling; while(nextSibling && nextSibling.nodeType != 1){nextSibling = nextSibling.nextSibling;}return nextSibling;", new Object[] { this.current });

      HashMap selectors = new HashMap();
      selectors.put("element", element);
      selectors.put("methodInfo", this.methodInfo);
      next = new Element(this.container, selectors).locate();
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return next;
  }

  public String previousTextNodeValue()
  {
    String text = "";
    this.methodInfo += ".previousTextNodeValue()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      text = (String)executeJs("var previousSibling = arguments[0].previousSibling; while(previousSibling && previousSibling.nodeType != 3){previousSibling = previousSibling.previousSibling;}return previousSibling.nodeValue;", new Object[] { this.current });
    }
    catch (Exception e)
    {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return text;
  }

  public String nextTextNodeValue()
  {
    String text = "";
    this.methodInfo += ".nextTextNodeValue()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      text = (String)executeJs("var nextSibling = arguments[0].nextSibling; while(nextSibling && nextSibling.nodeType != 3){nextSibling = nextSibling.nextSibling;}return nextSibling.nodeValue;", new Object[] { this.current });
    }
    catch (Exception e)
    {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return text;
  }

  public List<Element> getElementList(String selectors)
  {
    HashMap hashedSelectors = toHash(selectors);
    return getElementList(hashedSelectors);
  }

  protected List<Element> getElementList(HashMap<String, Object> selectors)
  {
    List elementList = new ArrayList();
    List<WebElement> webElementList = new ArrayList();
    if (selectors.get("methodInfo") == null)
      this.methodInfo = (this.methodInfo + "getElementTextList(" + selectors.toString() + ")");
    else {
      this.methodInfo = ((String)selectors.remove("methodInfo"));
    }
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      webElementList = new ElementLocator(this, selectors).findAllByMultiple(selectors);
      int i;
      i = 0;
      for (WebElement webElement : webElementList) {
        HashMap elementselectors = new HashMap();
        elementselectors.put("element", webElement);
        elementselectors.put("methodInfo", this.methodInfo + ".get(" + i + ")");
        elementList.add(new Element(this.container, elementselectors).locate());
        i++;
      }
    }
    catch (Exception e)
    {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return elementList;
  }

  public List<String> getElementTextList(String selectors)
  {
    HashMap hashedSelectors = toHash(selectors);
    return getElementTextList(hashedSelectors);
  }

  protected List<String> getElementTextList(HashMap<String, Object> selectors)
  {
    if (selectors.get("methodInfo") == null)
      this.methodInfo = (this.methodInfo + "getElementTextList(" + selectors.toString() + ")");
    else {
      this.methodInfo = ((String)selectors.remove("methodInfo"));
    }
    List elementTextList = new ArrayList();

    List<WebElement> webElementList = new ArrayList();
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      webElementList = new ElementLocator(this, selectors).findAllByMultiple(selectors);

      for (WebElement element : webElementList)
        elementTextList.add(element.getText());
    }
    catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return elementTextList;
  }

  private void setAttribute(String js)
  {
    this.methodInfo = (this.methodInfo + ".setAttribute(" + js + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      executeJs(js, new Object[] { this.current });
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public Map<String, String> getAttributes()
  {
    Map attributes = new HashMap();
    this.methodInfo += ".getAttributes()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      String attrs = (String)executeJs("var str; var attrs=arguments[0].attributes ;for(var i=0;i<attrs.length;i++){ attr=attrs[i]; str+=','+attr.name+'=>'+attr.value; };return str", new Object[] { this.current });

      attrs = attrs.replaceFirst(",", "");
      for (String attr : attrs.split(",")) {
        String[] nameValue = attr.split("=>");
        attributes.put(nameValue[0], nameValue[1]);
      }
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
    return attributes;
  }

  public void setAttribute(String property, String value)
  {
    this.methodInfo = (this.methodInfo + ".setAttribute(\"" + property + "\",\"" + value + "\")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      executeJs("arguments[0]." + property + " = arguments[1];", new Object[] { this.current, value });
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void setInnerText(String value)
  {
    this.methodInfo = (this.methodInfo + ".setInnerText(\"" + value + "\")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      executeJs("arguments[0].textContent = arguments[1];", new Object[] { this.current, value });
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void click()
  {
    this.methodInfo += ".click()";
    if (!assertExists().booleanValue()) {/*定位元素成功，current成功赋值。*/
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      if (this.current.isDisplayed()) {
        highlight(Boolean.valueOf(true));
        this.current.click();
        highlight(Boolean.valueOf(false));
      } else {
        executeAsyncJs("arguments[0].click();", new Object[] { this.current });
      }
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void clickJs()
  {
    this.methodInfo += ".clickJs()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      executeJs("arguments[0].click();", new Object[] { this.current });
      highlight(Boolean.valueOf(false));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void fireEvent(String name)
  {
    this.methodInfo = (this.methodInfo + ".fireEvent(\"" + name + "\")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      String eventName = name.replaceFirst("on", "");
      StringBuffer script = new StringBuffer("var element = arguments[0];");
      script.append("var name = arguments[1];");
      script.append("canBubble = (typeof(canBubble) == 'undefined') ? true : false;");
      script.append("var evt = document.createEvent('HTMLEvents');");
      script.append("evt.shiftKey = false;evt.metaKey = false;");
      script.append("evt.altKey = false;evt.ctrlKey = false;");
      script.append("evt.initEvent(name, canBubble, true);");
      script.append("element.dispatchEvent(evt);");
      executeJs(script.toString(), new Object[] { this.current, eventName });
      highlight(Boolean.valueOf(false));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void drag(Element element)
  {
    this.methodInfo += ".drag(element)";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      Actions builder = new Actions(this.driver);
      Action action = builder.dragAndDrop(this.current, element.current).build();
      action.perform();
      highlight(Boolean.valueOf(false));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void doubleClick()
  {
    this.methodInfo += ".doubleClick()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      Actions action = new Actions(this.driver);

      action.doubleClick(this.current);
      action.perform();
      highlight(Boolean.valueOf(false));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void scrollIntoView(Boolean top)
  {
    this.methodInfo = (this.methodInfo + ".scrollIntoView(" + top.toString() + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      executeJs("arguments[0].scrollIntoView(arguments[1]);", new Object[] { this.current, top });
      highlight(Boolean.valueOf(false));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void focus()
  {
    this.methodInfo += ".focus()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      executeJs("return arguments[0].focus();", new Object[] { this.current });
      highlight(Boolean.valueOf(false));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void mouseClick()
  {
    this.methodInfo += ".mouseClick()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      Actions builder = new Actions(this.driver);
      Action action = builder.moveToElement(this.current).click().build();
      action.perform();
      highlight(Boolean.valueOf(false));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public String readonly()
  {
    String readonly = null;
    this.methodInfo += ".readonly()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      readonly = this.current.getAttribute("readOnly");
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return readonly;
  }

  public void readonly(Boolean readonly)
  {
    this.methodInfo = (this.methodInfo + ".readonly(" + readonly.toString() + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      executeJs("arguments[0].readOnly = arguments[1];", new Object[] { this.current, readonly });
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public int maxLength()
  {
    int maxLength = 0;
    this.methodInfo += ".maxLength()";
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      maxLength = Integer.parseInt(this.current.getAttribute("maxLength"));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }

    return maxLength;
  }

  public void maxlength(int length)
  {
    this.methodInfo = (this.methodInfo + ".maxlength(" + length + ")");
    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      executeJs("arguments[0].maxLength = arguments[1];", new Object[] { this.current, String.valueOf(length) });
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  public void sendKeys(CharSequence... value)
  {
    String actualValue = "";
    for (CharSequence key : value) {
      if ((key instanceof Keys))
        actualValue = actualValue + ((Keys)key).name();
      else {
        actualValue = actualValue + key.toString();
      }
    }
    this.methodInfo = (this.methodInfo + ".sendKey(\"" + actualValue + "\")");

    if (!assertExists().booleanValue()) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
      throw new StepException(this.errorInfo);
    }
    try {
      highlight(Boolean.valueOf(true));
      this.current.sendKeys(value);
      highlight(Boolean.valueOf(false));
    } catch (Exception e) {
      LogModule.logStepFail(StepType.ACTION, this.methodInfo, RunResult.FAIL, e.getMessage(), this.driver);
      throw new StepException(this.methodInfo + "操作失败!" + e.getMessage());
    }
    LogModule.logStepPass(StepType.ACTION, this.methodInfo, RunResult.PASS, this.driver);
  }

  protected void highlight(Boolean isHighlight)
  {
  }

  public int index()
  {
    return 0;
  }

  public void check()
  {
  }

  public void uncheck()
  {
  }

  public void set(Boolean check)
  {
  }

  public void set(String path)
  {
  }

  public void submit()
  {
  }

  public List<Element> getCellList()
  {
    return null;
  }

  public Map<String, Integer> getCellTextIndexMap()
  {
    return null;
  }

  public List<String> getCellTextList()
  {
    return null;
  }

  public void setOptionByValue(String value)
  {
  }

  public void setOptionByText(String text)
  {
  }

  public void setOptionByIndex(int index)
  {
  }

  public void setOptionByRand(int start, int end)
  {
  }

  public void setOptionByRand()
  {
  }

  public List<String> getSelectedTextList()
  {
    return null;
  }

  public List<String> getSelectedValueList()
  {
    return null;
  }

  public List<Element> getOptionList()
  {
    return null;
  }

  public List<String> getOptionTextList()
  {
    return null;
  }

  public void append(String value)
  {
  }

  public void clear()
  {
  }

  public List<Element> getRowList()
  {
    return null;
  }

  public List<List<String>> getRowTextList()
  {
    return null;
  }

  public Cell getCell(int row, int cell)
  {
    return null;
  }
}