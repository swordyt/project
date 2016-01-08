package requestentity;
public class ReqEntity_kv extends RequestEntity {
public ReqEntity_kv(){
initData();
}private String iCityID;
private String key;
public String getICityID(){
return this.iCityID;
}
public void setICityID(String value){
this.iCityID=value;
}
public String getKey(){
return this.key;
}
public void setKey(String value){
this.key=value;
}
}