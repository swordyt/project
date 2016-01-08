package requestentity;
public class ReqEntity_login extends RequestEntity {
public ReqEntity_login(){
initData();
}private String sMobile;
private String sPassword;
public String getSMobile(){
return this.sMobile;
}
public void setSMobile(String value){
this.sMobile=value;
}
public String getSPassword(){
return this.sPassword;
}
public void setSPassword(String value){
this.sPassword=value;
}
}