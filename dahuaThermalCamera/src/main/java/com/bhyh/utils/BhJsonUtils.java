package com.bhyh.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
/**
 * Json数据处理工具类
 * @author dyonga
 *
 */
public class BhJsonUtils {
	
	
	public static Map<String,String> map = new HashMap<String, String>();
    /**
     * 创造静态代码块，初始化map的k-v键值对
     */
    static {
    	map.put("terrorism", "暴恐");
    	map.put("politic", "政治敏感");
    	map.put("illegal", "违规违法");
    	map.put("ad", "广告");
    	map.put("ocr", "OCR");
    	map.put("sensitive", "图像敏感");
    	map.put("porn", "色情");
    }
     /**
      * 过滤JsonObject中的null值	
      * @param jsonObj
      * @return
      */
	 public static JSONObject filterNull(JSONObject jsonObj) {
	       @SuppressWarnings("unchecked")
		   Iterator<String> it = jsonObj.keys();
	       Object obj = null;
	       String key = null;
	       while (it.hasNext()) {
	           key = it.next();
	           obj = jsonObj.get(key); 
	           if (obj == null || obj instanceof JSONNull) {
	               jsonObj.put(key, "");
	           }	           
	           if (obj.equals(null)) {
	               jsonObj.put(key, "");
	           }
	           if (obj instanceof JSONObject) {
	        	   //此处很怪，json串中null值，无法判null，只能通过length来判断"{}",长度刚好为4
	        	   if(obj.toString().length() > 4) {	           	  
	            	   filterNull((JSONObject) obj);
	        	   }else {	      
	        		   jsonObj.put(key, "");
	        	   }        	  
	           }
	           if (obj instanceof JSONArray) {
	   	               JSONArray objArr = (JSONArray) obj;
	               for (int i = 0; i < objArr.size(); i++) {
	                   filterNull(objArr.getJSONObject(i));
	               }
	           }
	       }
	       return jsonObj;
	   } 
	 
	/**
	 * 通过过滤JSONObject的属性名，并根据处理建议返回过滤后的JSONObject对象
	 * 仅保留body属性中image_result属性值，并且suggest不是normal的值
	 * @param obj传入的JSONObject对象
	 * @param propertyNm 属性名
	 * @param suggest 处理建议
	 * @return JsonObject对象
	 */
	public static JSONArray filterByPropertyNmAndSug(JSONObject obj/* , String propertyNm, String suggest */) {
		 JSONArray newArr = new JSONArray();
		 JSONObject bodyObj = (JSONObject) obj.get("body");
		 if(bodyObj == null) 
			 return null;	 	 
		 JSONArray imageResultArr = (JSONArray) bodyObj.get("image_result"); //获取image_result
		 if(imageResultArr == null)
			 return null;
		 System.out.println("filterByPropertyNmAndSug中  "+imageResultArr.toString());
		 System.out.println("size===="+imageResultArr.size());
		 //过滤imageResultObj中 "suggest": "normal"的json对，仅保留不合格（疑是，非法.......）			 
		 for (int i = 0; i < imageResultArr.size(); i++) {
			 JSONObject jsonObj = (JSONObject) imageResultArr.get(i);
			 String sug_str = (String) jsonObj.get("suggest");
			 String business = (String) jsonObj.get("business");
			 if(!sug_str.equals("normal")) {
				 System.out.println("i am going to push:"+business);		
				 newArr.add(jsonObj);
			 }			
		}
		 return newArr;	 
	 }
	/**
	 * 将"business": "terrorism"中business提换成中文
	 * 向detail中每一组jsonObject，添加“business”属性名和对应值
	 */
	public static JSONArray tranfer2NewArr(JSONArray arr) {
		//首先替换business属性值为中文
		for (int i = 0; i < arr.size(); i++) {
			JSONObject jsonObj = (JSONObject) arr.get(i);
			 String business = (String) jsonObj.get("business");
			 String businessVal = "";
			 if(map.containsKey(business)) {
				 businessVal = map.get(business);
				 System.out.println(businessVal);
				 jsonObj.put("business", businessVal);
			 }
		     //取出detail中的jasonArray并添加business属性进入
			 JSONArray detailArr = (JSONArray) jsonObj.get("detail");
			 if(detailArr == null)
				 continue;
			 System.out.println("detailArr.size===="+detailArr.size());
			 for (int j = 0; j < detailArr.size(); j++) {				 
				 JSONObject obj = (JSONObject)detailArr.get(j);
				 obj.put("business", businessVal);
			}
		}
		//取出detail中的jasonArray并添加business属性进入
		System.out.println("arr"+arr);
		return arr;
		
	}
}//end