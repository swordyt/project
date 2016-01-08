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
  public abstract DataType minmax() default DataType.min;//0��������С��excel����������0�����excel������
  public abstract int count() default 1;
}
