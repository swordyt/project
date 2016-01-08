package com.travelsky.autotest.autosky.browser;

import com.travelsky.autotest.autosky.elements.Body;
import com.travelsky.autotest.autosky.elements.Button;
import com.travelsky.autotest.autosky.elements.Cell;
import com.travelsky.autotest.autosky.elements.Checkbox;
import com.travelsky.autotest.autosky.elements.Div;
import com.travelsky.autotest.autosky.elements.Element;
import com.travelsky.autotest.autosky.elements.FileField;
import com.travelsky.autotest.autosky.elements.Form;
import com.travelsky.autotest.autosky.elements.Frame;
import com.travelsky.autotest.autosky.elements.Input;
import com.travelsky.autotest.autosky.elements.Link;
import com.travelsky.autotest.autosky.elements.Option;
import com.travelsky.autotest.autosky.elements.Radio;
import com.travelsky.autotest.autosky.elements.Row;
import com.travelsky.autotest.autosky.elements.Select;
import com.travelsky.autotest.autosky.elements.Span;
import com.travelsky.autotest.autosky.elements.Table;
import com.travelsky.autotest.autosky.elements.TextField;
import com.travelsky.autotest.autosky.junit.core.LogModule;
import com.travelsky.autotest.autosky.junit.enums.RunResult;
import com.travelsky.autotest.autosky.junit.enums.StepType;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class Container
{
  public WebDriver driver;
  public Container container;
  public String methodInfo;
  public String errorInfo;
  public String currentTime;
  public boolean isFrame = false;
  public boolean hasFrame = false;
  public WebElement current;

  public WebDriver getDriver()
  {
    return this.driver;
  }

  public void setDriver(WebDriver driver)
  {
    this.driver = driver;
  }

  protected abstract Container locate();

  public abstract Container wd();

  public Object executeJs(String script, Object[] args) {
    JavascriptExecutor js = (JavascriptExecutor)this.driver;
    return js.executeScript(script, args);
  }

  public Object executeAsyncJs(String script, Object[] args) {
    JavascriptExecutor js = (JavascriptExecutor)this.driver;
    return js.executeAsyncScript(script, args);
  }

  public Button button(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "button|input[@type='button'or @type='submit' or @type='image' or @type='reset']");
    Button button = new Button(this, selectors);
    return button;
  }

  public Element element(String options)
  {
    Element element = new Element(this, toHash(options));
    return element;
  }

  public Div div(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "div");
    Div element = new Div(this, selectors);
    return element;
  }

  public Span span(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "span");
    Span element = new Span(this, selectors);
    return element;
  }

  public TextField textfield(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "input|textarea");
    TextField element = new TextField(this, selectors);
    return element;
  }

  public Table table(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "table");
    Table element = new Table(this, selectors);
    return element;
  }

  public Cell cell(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "th|td");
    Cell element = new Cell(this, selectors);

    return element;
  }

  public Row row(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "tr");
    Row element = new Row(this, selectors);
    return element;
  }

  public Input input(String options) {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "input");
    Input element = new Input(this, selectors);
    return element;
  }

  public Checkbox checkbox(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "input");
    Checkbox element = new Checkbox(this, selectors);
    return element;
  }

  public Radio radio(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "input");
    Radio element = new Radio(this, selectors);
    return element;
  }

  public Select select(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "select");
    Select element = new Select(this, selectors);
    return element;
  }

  public Option option(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "option");
    Option element = new Option(this, selectors);
    return element;
  }

  public Form form(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "form");
    Form element = new Form(this, selectors);
    return element;
  }

  public Link link(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "a");
    Link element = new Link(this, selectors);
    return element;
  }

  public Body body(String options) {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "body");
    Body element = new Body(this, selectors);
    return element;
  }

  public FileField fileField(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("type", "file");
    selectors.put("tagName", "input");
    FileField element = new FileField(this, selectors);
    return element;
  }

  public Frame frame(String options)
  {
    HashMap selectors = toHash(options);
    selectors.put("tagName", "iframe|frame");
    Frame element = new Frame(this, selectors);
    return element;
  }
/*判断属性中是否包含xpath和正则表达式，否则切割后按一般字符串放入map*/
  public HashMap<String, Object> toHash(String options)
  {
    HashMap map = new HashMap();
    String methodInfo = java.lang.Thread.currentThread().getStackTrace()[2].getMethodName() + "(\"" + options + "\")";
    map.put("methodInfo", methodInfo);
    if (options.contains("xpath=>")) {
      map.put("xpath", options.substring(options.indexOf("=>") + 2));
      return map;
    }
    String[] optionInfos = options.split(",");
    for (String optionInfo : optionInfos) {
      if (optionInfo.indexOf("=>") < 0) {
        this.errorInfo = (options + "格式错误,请参照（属性=>属性值）,示例：ie.button(\"id=>kw\").click();");
        LogModule.logStepFail(StepType.ACTION, methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
        throw new RuntimeException(this.errorInfo);
      }

      String[] option = optionInfo.split("=>");
      if (option.length == 2) {
    	  /*以/开始，以/结束的属性为正则表达式，放入时直接放入一个正则对象。*/
        if ((option[1].startsWith("/")) && (option[1].endsWith("/"))) {
          Pattern pattern = Pattern.compile(option[1].substring(1, option[1].length() - 1), 32);
          map.put(option[0], pattern);
        } else {
          map.put(option[0], option[1]);
        }
      } else {
        this.errorInfo = (options + "格式错误,请参照（属性=>属性值）,多个属性键值匹配用逗号分隔,示例：ie.button(\"name=>aa,type=>submit,index=>1\").click();");
        LogModule.logStepFail(StepType.ACTION, methodInfo, RunResult.FAIL, this.errorInfo, this.driver);
        throw new RuntimeException(this.errorInfo);
      }
    }
    return map;
  }
}