package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MakeInterface {

	public static void main(String[] args) {
		Properties pro = PropertiesTool.readProperties("/configfile/config.properties");
		String []confs = pro.getProperty("interface").split(",");
		for(String conf:confs){
			File file = new File(System.getProperty("user.dir")+"\\src\\requestentity\\ReqEntity_"+conf+".java");
			if(!file.exists()){
				System.out.println("创建文件：ReqEntity_"+conf+".java");
				Properties proConf = PropertiesTool.readProperties("/configfile/config.properties");
				String []fields = proConf.getProperty(conf+".fields").split(",");
				makeRequest(conf,fields);
				System.out.println("创建文件："+conf+".xlsx");
				makeExcel(conf, fields);
			}
		}
	}
	public static void makeRequest(String clsName,String []fields){
		String content = "";
		String requestPath = System.getProperty("user.dir")+"\\src\\requestentity\\ReqEntity_"+clsName+".java";
		File request = new File(requestPath);
		content = "package requestentity;\n"
				+ "public class ReqEntity_"+clsName+" extends RequestEntity {\n"
						+ "public ReqEntity_"+clsName+"(){\n"
							+"initData();\n"
			+"}\n";
		String param = "";
		String method = "";
		for(String para:fields){
			String value = "";
			param = param + "private String "+para+";\n";
			value = para.substring(0, 1).toUpperCase()+para.substring(1);
			method = method + "public String get"+value+"(){\n"
					+ "return this."+para+";\n"
							+ "}\n"
							+ "public void set"+value+"(String value){\n"
									+ "this."+para+"="+"value;\n"
									+ "}\n";
			
		}
		content = content + param + method + "}";
		try {
			request.createNewFile();
			FileOutputStream out = new FileOutputStream(request);
			out.write(content.getBytes("GBK"));
			out.close();
		} catch (Exception e) {
			System.out.println("生成request实体类失败，"+e.getMessage());
			e.printStackTrace();
		}
	}
	public static void makeExcel(String clsName,String []fields){
		String []def = {"name","domain","url","method"};
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("data");
		//创建一行
		Row row = sheet.createRow(0);
		//循环写入列数据
		for(int i=0;i<def.length;i++){
			Cell cell = row.createCell(i);
			cell.setCellValue(def[i]);
		}
		for(int i=0;i<fields.length;i++){
			Cell cell = row.createCell(i+def.length);
			cell.setCellValue(fields[i]);
		}
		//创建文件流
		try {
			OutputStream out = new FileOutputStream(System.getProperty("user.dir")+"\\src\\interfacedata\\"+clsName+".xlsx");
			wb.write(out);
			out.close();
		} catch (Exception e) {
			System.out.println("创建xlsx文件失败，"+e.getMessage());
			e.printStackTrace();
		}
	}
}
