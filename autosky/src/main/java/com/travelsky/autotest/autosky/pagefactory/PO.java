package com.travelsky.autotest.autosky.pagefactory;

import com.travelsky.autotest.autosky.browser.Browser;
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
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

public class PO
{
  public static void initPage(Object page, Browser browser)
  {
    Field[] fields = page.getClass().getDeclaredFields();
    for (Field field : fields) {
      LocatingHandle lh = new LocatingHandle(browser, field, fields);
      Object value = Proxy.newProxyInstance(page.getClass().getClassLoader(), new Class[] { IBody.class, IButton.class, ICell.class, ICheckbox.class, IDiv.class, IElement.class, IFileField.class, IForm.class, IFrame.class, IInput.class, ILink.class, IOption.class, IRadio.class, IRow.class, ISelect.class, ISpan.class, ITable.class, ITextField.class }, lh);

      if (value != null)
        try {
          field.setAccessible(true);
          field.set(page, value);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
    }
  }
}