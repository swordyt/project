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
		logger.error("ʹ�õ�����ʽ" + request.getMethod() + "�޷�ʶ�����޸�Ϊget/post��");
	}

	public Response Get(Request request) {
		try {
			return new Response(this.sendGet(request.makeUrl(),
					request.makeParam()));
		} catch (Exception e) {
			logger.fatal("��ȡ�ӿ����ݳ���" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public Response Post(Request request) {
		try {
			return new Response(this.sendPost(request.makeUrl(),
					request.makeParam()));
		} catch (Exception e) {
			logger.fatal("��ȡ�ӿ����ݳ���:" + e.getMessage());
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
				logger.error(param + "�����쳣��" + e.getMessage());
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
			logger.error(url + "�����쳣��" + e.getMessage());
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
			// �򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new PrintWriter(conn.getOutputStream());
			// �����������
			out.print(param);
			// flush������Ļ���
			out.flush();
			// ����BufferedReader���������ȡURL����Ӧ
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.fatal("���� POST ��������쳣��" + e);
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
