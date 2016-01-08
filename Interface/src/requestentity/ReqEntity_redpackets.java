package requestentity;
public class ReqEntity_redpackets extends RequestEntity {
public ReqEntity_redpackets(){
initData();
}private String iUserID;
private String sToken;
public String getIUserID(){
return this.iUserID;
}
public void setIUserID(String value){
this.iUserID=value;
}
public String getSToken(){
return this.sToken;
}
public void setSToken(String value){
this.sToken=value;
}
}