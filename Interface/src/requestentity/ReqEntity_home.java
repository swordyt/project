package requestentity;
public class ReqEntity_home extends RequestEntity {
public ReqEntity_home(){
initData();
}private String iCityID;
public String getICityID(){
return this.iCityID;
}
public void setICityID(String value){
this.iCityID=value;
}
}