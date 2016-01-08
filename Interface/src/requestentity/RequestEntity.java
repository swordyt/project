package requestentity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import responseentity.ResponseArray;
import junit.enums.DataType;
import junit.rule.DriverService;
import util.ExcelUtil;

public class RequestEntity implements Request {
	private String domain="http://api.yangbo.qa.anhouse.com.cn";
	private String url="/user/user/signAndCredit.html";
	private String method="post";
	private String name="test";
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDomain() {
		return domain;
	}


	public void setDomain(String domain) {
		this.domain = domain;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}

	
	
	
	@Override
	//保证url的格式为/work
		public String makeUrl() {
			if(!this.url.startsWith("/")){
				this.url = "/"+this.url;
			}
			if(this.url.endsWith("/")){
				this.url = this.url.substring(0, this.url.length()-1);
			}
			if(this.domain.toLowerCase().startsWith("http://")){
				this.domain = this.domain.substring(7, this.domain.length());
			}
			if(this.domain.endsWith("/")){
				this.domain = this.domain.substring(0,this.domain.length()-1);
			}
			return "http://"+this.domain+this.url;
		}


	@Override
	public String makeParam() throws Exception{
		//获取实体类的所有属性，返回Field数组
		Field []field = this.getClass().getDeclaredFields();
		String param = ""; 
		for(int j=0;j<field.length;j++){//遍历所有属性
				String name = field[j].getName();//获取属性名字
				String upName = name.substring(0, 1).toUpperCase()+name.substring(1);
				Method m = this.getClass().getMethod("get"+upName);
				String value = (String)m.invoke(this);
				if(name == "url" || name == "domain" || name == "method" || name == "num"){
					continue;
				}
				if(value == ""){
					continue;
				}
				if(param == ""){
					param = name+"="+value;
					continue;
				} 
				param = param+"&"+name+"="+value;
		}
		
		return param;
	}
	
	protected void initData(){

		List<Map<String,String>> data = new ExcelUtil().excelDatas(System.getProperty("user.dir")
				+"\\src\\interfacedata\\"
				+this.getClass().toString().split("ReqEntity_")[1]+".xlsx", "data");
		Map<String, String> map = null;
		if(DriverService.datatype == DataType.min){
			if(DriverService.times == 1 || DriverService.times > data.size()){
				DriverService.times = data.size();
			}
		}
		if(DriverService.datatype == DataType.max){
			if(DriverService.times == 1 || DriverService.times < data.size()){
				DriverService.times = data.size();
			}
		}
		if(data.size() <DriverService.number){
			map = data.get((DriverService.number-1)%data.size());
		}else{
			map = data.get(DriverService.number-1);
		}
		for(Map.Entry<String, String> entry:map.entrySet()){
			String key = entry.getKey().toString();
			String upKye = key.substring(0, 1).toUpperCase()+key.substring(1);
			String value = entry.getValue().toString();
			if(value.contains("#")){
				String []arr=value.split("#");
				String []arr1=arr[0].split("[.]");
				value = ResponseArray.getInstance(arr1[0], arr1[1]).getValue("resp."+arr[1]);
			}
			try {
				Method m = this.getClass().getMethod("set"+upKye, String.class);
				m.invoke(this, value);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

}
