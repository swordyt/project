package junit.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import junit.enums.*;
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface DataSource
{
  public abstract DriverType Drive();
  public abstract DataType minmax() default DataType.min;//0代表以最小的excel行驱动，非0以最大excel行驱动
  public abstract int count() default 1;
}
