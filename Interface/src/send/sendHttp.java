package send;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import junit.rule.DriverService;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;

import com.mysql.jdbc.StringUtils;

import requestentity.Request;
import responseentity.Response;
import responseentity.ResponseArray;

public class sendHttp {
	private HttpClient client = new HttpClient();
	private String charset = "GBK";
	public Logger logger = DriverService.logger;

	public void send(Request request) {
		if (request.getMethod().equalsIgnoreCase("get")) {
			ResponseArray.getInstance().setArraylist(getSetName(),
					request.getName(), this.Get(request));
			return;
		}
		if (request.getMethod().equalsIgnoreCase("post")) {
			ResponseArray.getInstance().setArraylist(getSetName(),
					request.getName(), this.Post(request));
			return;
		}
		logger.error("使用的请求方式" + request.getMethod() + "无法识别，请修改为get/post。");
	}

	public Response Get(Request request) {
		try {
			return new Response(this.sendGet(request.makeUrl(),
					request.makeParam()));
		} catch (Exception e) {
			logger.fatal("获取接口数据出错：" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public Response Post(Request request) {
		try {
			return new Response(this.sendPost(request.makeUrl(),
					request.makeParam()));
		} catch (Exception e) {
			logger.fatal("获取接口数据出错:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String sendGet(String url, String param) {
		HttpMethod get = new GetMethod(url);
		StringBuffer response = new StringBuffer();
		if (!StringUtils.isNullOrEmpty(param)) {
			try {
				get.setQueryString(URIUtil.encodeQuery(param));
			} catch (URIException e) {
				logger.error(param + "编码异常：" + e.getMessage());
			}
		}
		try {
			client.executeMethod(get);
			if (get.getStatusCode() < 400) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(get.getResponseBodyAsStream(),
								this.charset));
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
			}
		} catch (Exception e) {
			logger.error(url + "请求异常：" + e.getMessage());
			return null;
		}
		logger.info("Response--->" + response.toString());
		return response.toString();
	}

	private String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		logger.info(url + "  " + "POST");
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输出流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.fatal("发送 POST 请求出现异常！" + e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("Response--->" + result);
		return result;
	}

	private static String getSetName() {
		// String[] className = Thread.currentThread().getStackTrace()[3]
		// .getClassName().split("[.]");
		// String name = className[className.length - 1];
		String name = Thread.currentThread().getStackTrace()[3].getMethodName();
		return name;
	}
}
