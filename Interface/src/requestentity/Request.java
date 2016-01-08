package requestentity;


public interface Request {
	public String getName();
	public void setName(String name);
	public String getDomain() ;
	public void setDomain(String domain) ;
	public String getUrl() ;
	public void setUrl(String url) ;
	public String getMethod();
	public void setMethod(String method) ;
	public String makeUrl();
	public String makeParam() throws Exception;
}
