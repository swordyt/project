package www.sword.com.test;


import com.travelsky.autotest.autosky.junit.annotations.DataSource;
import com.travelsky.autotest.autosky.junit.enums.DataSourceType;
import com.travelsky.autotest.autosky.junit.rules.DriverService;
import org.junit.Rule;
import org.junit.Test;

public class test {
	@Rule
	public DriverService driver = new DriverService();
	
	@Test
	@DataSource(type=DataSourceType.EXCEL,file="data.xls",sheetName="datatable")
	public void test1(){
		System.out.println("haha"+driver.param.get("caseName"));
	}
}
