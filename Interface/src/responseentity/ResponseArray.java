package responseentity;

import java.util.HashMap;

public class ResponseArray {
	private static ResponseArray single = null;
	private HashMap<String, HashMap<String, Response>> arraylist = new HashMap<>();
	public void setArraylist(String setName,String interName,Response response){
		HashMap<String, Response> value = new HashMap<String, Response>();
		value.put(interName, response);
		ResponseArray.getInstance().arraylist.put(setName,value);
		return ;
	}
	public Response getResponse(String setName,String interName){
		if(arraylist.containsKey(setName)){
			if(arraylist.get(setName).containsKey(interName)){
				return arraylist.get(setName).get(interName);
			}
		}
		//检测无该Response返回null
		return null;
	}
	private ResponseArray(){
		
	}
	//静态工厂方法
	public static ResponseArray getInstance(){
		if(single == null){
			single = new ResponseArray();
		}
		return single;
	}
	public static Response getInstance(String setName,String interName){
		if(single == null){
			single = new ResponseArray();
		}
		return single.getResponse(setName, interName);
	}
}
