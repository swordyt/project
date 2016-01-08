package junit.rule;

import junit.enums.DataType;
import junit.enums.DriverType;

import org.apache.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DriverService implements TestRule {
	public static Description DESCRIPTION = null;
	public static Statement STATEMENT = null;
	public static int number = 1;
	public static int times = 1;
	public static DataType datatype;
	public static Logger logger = Logger.getLogger(DriverService.class);
	public Statement apply(final Statement base, final Description des) {
		return new Statement() {
			public void evaluate() throws Throwable {
				DriverService.DESCRIPTION = des;
				DriverService.STATEMENT = base;
				if (des.getAnnotation(DataSource.class) == null) {
					base.evaluate();
					return ;
				}
				DriverType drivetype = des.getAnnotation(DataSource.class).Drive();
				
				
				if(drivetype == DriverType.excel){
					DataType minmax = des.getAnnotation(DataSource.class).minmax();
					if(minmax == DataType.min){
						DriverService.datatype = DataType.min;
					}else{
						DriverService.datatype = DataType.max;
					}
				}else if(drivetype == DriverType.constant){
					DriverService.times = des.getAnnotation(DataSource.class).count();
				}else{
					base.evaluate();
					return ;
				}
				for(;DriverService.number <= DriverService.times;DriverService.number++){
					base.evaluate();
				}
			}
		};
	}
}