package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesTool {
	public static Properties readProperties(String rePath){
		Properties prop =new Properties();
		InputStream in = Object.class.getResourceAsStream(rePath);
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
}
