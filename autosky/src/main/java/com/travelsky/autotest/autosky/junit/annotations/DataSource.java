package com.travelsky.autotest.autosky.junit.annotations;

import com.travelsky.autotest.autosky.junit.enums.DataSourceType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface DataSource
{
  public abstract String file();

  public abstract DataSourceType type();

  public abstract String sheetName();
}