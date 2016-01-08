package com.travelsky.autotest.autosky.pagefactory.interfaces;

import com.travelsky.autotest.autosky.elements.Element;
import java.util.List;
import java.util.Map;

public abstract interface IRadio
{
  public abstract Boolean exists(int paramInt);

  public abstract Boolean exists();

  public abstract String text();

  public abstract String tagName();

  public abstract String className();

  public abstract String id();

  public abstract String outerHTML();

  public abstract String getStyle(String paramString);

  public abstract String getAttribute(String paramString);

  public abstract Boolean enable();

  public abstract Boolean enable(int paramInt);

  public abstract Boolean visible();

  public abstract Boolean visible(int paramInt);

  public abstract Boolean invisible(int paramInt);

  public abstract Element parent(String paramString);

  public abstract Element parent();

  public abstract List<Element> children();

  public abstract Element previous();

  public abstract Element next();

  public abstract String previousTextNodeValue();

  public abstract String nextTextNodeValue();

  public abstract List<Element> getElementList(String paramString);

  public abstract List<String> getElementTextList(String paramString);

  public abstract Map<String, String> getAttributes();

  public abstract void setAttribute(String paramString1, String paramString2);

  public abstract void setInnerText(String paramString);

  public abstract void click();

  public abstract void clickJs();

  public abstract void fireEvent(String paramString);

  public abstract void drag(Element paramElement);

  public abstract void doubleClick();

  public abstract void scrollIntoView(Boolean paramBoolean);

  public abstract void focus();

  public abstract void mouseClick();

  public abstract String readonly();

  public abstract void readonly(Boolean paramBoolean);

  public abstract int maxLength();

  public abstract void maxlength(int paramInt);

  public abstract void sendKeys(CharSequence[] paramArrayOfCharSequence);

  public abstract void set(Boolean paramBoolean);
}

/* Location:           E:\MyEclipse\maven3\MVNrepo\com\travelsky\autotest\autosky\3.0.4\autosky-3.0.4.jar
 * Qualified Name:     com.travelsky.autotest.autosky.pagefactory.interfaces.IRadio
 * JD-Core Version:    0.6.1
 */