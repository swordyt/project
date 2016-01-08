package responseentity;

import net.sf.json.JSONObject;

public class Response {
	public String code = "";
	public String msg = "";
	public JSONObject data = null;
	public JSONObject json = null;
	
	public Response(String str){
		this.json = JSONObject.fromObject(str);
		this.code = this.json.getString("code");
		this.msg = this.json.getString("msg");
		this.data = this.json.getJSONObject("data");
	}

	public String getCode() {
		return code;
	}


	public String getMsg() {
		return msg;
	}

	public String getData() {
		return data.toString();
	}

	public String getJson() {
		return json.toString();
	}
	/**
	 * return£ºnull ×Ö¶Î²»´æÔÚ
	 * */
	public String getValue(String keyStr){
		JSONObject obj = this.data; 
		String []keys = keyStr.trim().split("[.]");
		String value = "";
		if(keys.length == 2){
			if(keys[1].equalsIgnoreCase("msg")){
				return this.getMsg();
			}
			if(keys[1].equalsIgnoreCase("code")){
				return this.getCode();
			}
			if(keys[1].equalsIgnoreCase("data")){
				return this.getData();
			}
		}
		if(!keys[1].equalsIgnoreCase("data")){
			return null;
		}
		for(int i=2;i<keys.length;i++){
			if (keys[i].contains("[")) {
				String key = keys[i].substring(0, keys[i].indexOf("["));
				int num = Integer.parseInt(keys[i].substring(keys[i].indexOf("[")+1, keys[i].indexOf("]")));
				if(!obj.containsKey(key)){
					return null;
				}
				obj = obj.getJSONArray(key).getJSONObject(num);
				if(i == keys.length -1){
					return value = obj.getJSONArray(key).getString(num);
				}else{
					continue;
				}
			}
			if(!obj.containsKey(keys[i])){
				return null;
			}
			if(i < keys.length-1){
				obj = obj.getJSONObject(keys[i]);
				continue;
			}
			value = obj.getString(keys[i]);
		}
		return value;
	}
}
