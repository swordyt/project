package com.travelsky.autotest.autosky.utils;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class HttpUtil
{
  private static int HTTPCLIENT_CONNECT_TIMEOUT = 10000;
  private static int HTTPCLIENT_RESPONSE_TIMEOUT = 30000;

  public static String getResult(String url, String userName, String password) throws Exception {
    HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
    HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();

    managerParams.setConnectionTimeout(HTTPCLIENT_CONNECT_TIMEOUT);

    managerParams.setSoTimeout(HTTPCLIENT_RESPONSE_TIMEOUT);

    client.getParams().setParameter("http.protocol.content-charset", "utf-8");

    setAuth(userName, password, url, client);
    HttpMethod method = new GetMethod(url);
    try
    {
      int statusCode = client.executeMethod(method);
      if ((statusCode != 200) && (statusCode != 302))
        throw new Exception(statusCode + " Error");
      byte[] resultBytes = method.getResponseBody();
      return new String(resultBytes);
    }
    catch (Exception e) {
      throw new Exception(e);
    } finally {
      method.releaseConnection();
    }
  }

  public static String postRequest(String url, String userName, String password) throws Exception {
    HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
    HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();

    managerParams.setConnectionTimeout(HTTPCLIENT_CONNECT_TIMEOUT);

    managerParams.setSoTimeout(HTTPCLIENT_RESPONSE_TIMEOUT);

    client.getParams().setParameter("http.protocol.content-charset", "utf-8");
    setAuth(userName, password, url, client);

    HttpMethod method = new PostMethod(url);
    try
    {
      int statusCode = client.executeMethod(method);
      if ((statusCode != 200) && (statusCode != 302))
        throw new Exception(statusCode + " Error");
      byte[] resultBytes = method.getResponseBody();
      return new String(resultBytes);
    }
    catch (Exception e) {
      throw new Exception(e);
    } finally {
      method.releaseConnection();
    }
  }

  public static String postRequest(String url, Map<String, String> params, String userName, String password) throws Exception {
    HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
    HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();

    managerParams.setConnectionTimeout(HTTPCLIENT_CONNECT_TIMEOUT);

    managerParams.setSoTimeout(HTTPCLIENT_RESPONSE_TIMEOUT);

    client.getParams().setParameter("http.protocol.content-charset", "utf-8");

    setAuth(userName, password, url, client);

    PostMethod method = new PostMethod(url);

    Set <Entry<String, String>> esParams = params.entrySet();
    NameValuePair[] httpParams = new NameValuePair[esParams.size()];

    int i = 0;
    for (Map.Entry esParam : esParams) {
      httpParams[i] = new NameValuePair((String)esParam.getKey(), (String)esParam.getValue());
      i++;
    }
    method.setRequestBody(httpParams);
    try
    {
      int statusCode = client.executeMethod(method);
      if ((statusCode != 200) && (statusCode != 302))
        throw new Exception(statusCode + " Error");
      byte[] resultBytes = method.getResponseBody();
      return new String(resultBytes);
    }
    catch (Exception e) {
      throw new Exception(e);
    } finally {
      method.releaseConnection();
    }
  }

  public static String postXml(String url, String value, String userName, String password) throws Exception {
    HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
    HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();

    managerParams.setConnectionTimeout(HTTPCLIENT_CONNECT_TIMEOUT);

    managerParams.setSoTimeout(HTTPCLIENT_RESPONSE_TIMEOUT);

    client.getParams().setParameter("http.protocol.content-charset", "utf-8");

    setAuth(userName, password, url, client);

    PostMethod method = new PostMethod(url);
    method.setRequestEntity(new StringRequestEntity(value, "text/xml", "UTF-8"));
    try
    {
      int statusCode = client.executeMethod(method);
      if ((statusCode != 200) && (statusCode != 302))
        throw new Exception(statusCode + " Error");
      byte[] resultBytes = method.getResponseBody();
      return new String(resultBytes);
    }
    catch (Exception e) {
      throw new Exception(e);
    } finally {
      method.releaseConnection();
    }
  }

  private static void setAuth(String username, String password, String url, HttpClient httpClient) throws Exception {
    if (username != null) {
      httpClient.getParams().setAuthenticationPreemptive(true);
      Credentials defaultcreds = new UsernamePasswordCredentials(username, password);

      URL tUrl = new URL(url);
      httpClient.getState().setCredentials(new AuthScope(tUrl.getHost(), tUrl.getPort()), defaultcreds);
    }
  }

  public static String uploadFile(File file, String url) throws Exception {
    if (!file.exists()) {
      return null;
    }
    PostMethod method = new PostMethod(url);

    FilePart fp = new FilePart("fileData", file);
    Part[] parts = { fp };

    MultipartRequestEntity mre = new MultipartRequestEntity(parts, method.getParams());
    method.setRequestEntity(mre);
    HttpClient client = new HttpClient();
    client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);
    try
    {
      int statusCode = client.executeMethod(method);
      if ((statusCode != 200) && (statusCode != 302))
        throw new Exception(statusCode + " Error");
      byte[] resultBytes = method.getResponseBody();
      return new String(resultBytes);
    }
    catch (Exception e) {
      throw new Exception(e);
    } finally {
      method.releaseConnection();
    }
  }

  public static void main(String[] args)
    throws Exception
  {
    uploadFile(new File("C:\\autosky_log\\TestJunit\\2014-04-22_16-16-33\\screenshot\\20140422-161703.png"), "http://localhost:8080/AutoTestPlat/logtest/upload");
  }
}