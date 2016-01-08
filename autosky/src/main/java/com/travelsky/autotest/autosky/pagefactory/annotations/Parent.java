package com.travelsky.autotest.autosky.pagefactory.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface Parent
{
  public abstract String parent();

  public abstract String value();
}