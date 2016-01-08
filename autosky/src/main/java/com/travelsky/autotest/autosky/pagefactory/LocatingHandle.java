package com.travelsky.autotest.autosky.pagefactory;

import com.travelsky.autotest.autosky.browser.Browser;
import com.travelsky.autotest.autosky.browser.Container;
import com.travelsky.autotest.autosky.exception.StepException;
import com.travelsky.autotest.autosky.pagefactory.annotations.Find;
import com.travelsky.autotest.autosky.pagefactory.annotations.Parent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LocatingHandle
  implements InvocationHandler
{
  private final Browser browser;
  private final Field field;
  private final Field[] fields;

  public LocatingHandle(Browser browser, Field field, Field[] fields)
  {
    this.browser = browser;
    this.field = field;
    this.fields = fields;
  }

  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
  {
    Container e = findElement(this.browser, this.field, this.fields);
    return method.invoke(e, args);
  }

  public Container findElement(Container container, Field field, Field[] fields)
  {
    Find find = (Find)field.getAnnotation(Find.class);
    Parent parent = (Parent)field.getAnnotation(Parent.class);

    if (find == null) {
      throw new StepException(field.getName() + "的Find注解为空，无法定位！");
    }

    if ((!(container instanceof Browser)) || (parent == null)) {
      if (field.getType().getSimpleName().equals("IBody")) {
        return container.body(find.value());
      }
      if (field.getType().getSimpleName().equals("IButton")) {
        return container.button(find.value());
      }
      if (field.getType().getSimpleName().equals("ICell")) {
        return container.cell(find.value());
      }
      if (field.getType().getSimpleName().equals("ICheckbox")) {
        return container.checkbox(find.value());
      }
      if (field.getType().getSimpleName().equals("IDiv")) {
        return container.div(find.value());
      }
      if (field.getType().getSimpleName().equals("IElement")) {
        return container.element(find.value());
      }
      if (field.getType().getSimpleName().equals("IFileField")) {
        return container.fileField(find.value());
      }
      if (field.getType().getSimpleName().equals("IForm")) {
        return container.form(find.value());
      }
      if (field.getType().getSimpleName().equals("IFrame")) {
        return container.frame(find.value());
      }
      if (field.getType().getSimpleName().equals("ICheckbox")) {
        return container.checkbox(find.value());
      }
      if (field.getType().getSimpleName().equals("IInput")) {
        return container.input(find.value());
      }
      if (field.getType().getSimpleName().equals("ILink")) {
        return container.link(find.value());
      }
      if (field.getType().getSimpleName().equals("IOption")) {
        return container.option(find.value());
      }
      if (field.getType().getSimpleName().equals("IRadio")) {
        return container.radio(find.value());
      }
      if (field.getType().getSimpleName().equals("IRow")) {
        return container.row(find.value());
      }
      if (field.getType().getSimpleName().equals("ISelect")) {
        return container.select(find.value());
      }
      if (field.getType().getSimpleName().equals("ISpan")) {
        return container.span(find.value());
      }
      if (field.getType().getSimpleName().equals("ITable")) {
        return container.table(find.value());
      }
      if (field.getType().getSimpleName().equals("ITextField"))
        return container.textfield(find.value());
    }
    else {
      for (Field f : fields) {
        if (f.getName().equals(parent.value())) {
          return findElement(findElement(container, f, fields), field, fields);
        }
      }
    }

    return null;
  }
}