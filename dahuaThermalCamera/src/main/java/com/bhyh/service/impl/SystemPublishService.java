package com.bhyh.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.bhyh.common.core.service.impl.CommonService;
import com.bhyh.exception.CheckedException;
import com.bhyh.result.PageResult;
import com.bhyh.result.Pager;
import com.bhyh.service.ISystemPublishService;
import com.bhyh.utils.BhJsonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
//import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 
 * @author dyonga
 *
 */
@Service
public class SystemPublishService extends CommonService implements ISystemPublishService{
	
	private static final String HTTP_ADD = "http://192.168.1.89";
	private static final String PORT = ":8080";
	private static final  Object[] DATA_CHECK = {0,1};
	private static final  Object[] BOOLEAN_CHECK = {true,false};
	private static final  String FIND_TYPE_CHECK = "name,userGiveName,groupName,programNum";
//	private static final  String[] TYPE_CHECK = {"video","music","picture","apk","word","update_file",
//			"text","face_img","terminal_setting","weather","marquee","program_folder","program_package","split","otherfile"};
	private static final  String TYPE_CHECK = "video,music,picture,apk,word,update_file,text,face_img,terminal_setting,weather,marquee,program_folder,program_package,split,otherfile";
	private static final  Object[] SORT_CHECK = {1,2,3,4,5,6,7,8,9,10};
	private static final  String PUBLISH_TYPE_CHECK = "order,check";
	private static final  String STATUS_CHECK = "online,offline,standby,buffer,download,reservation,check,all";
	private static final  String GROUP_CHECK = "system/video,music,picture,apk,word,update_file,text,face_img,terminal_setting,weather,marquee,program_folder,program_package,split,otherfile";
	/**
	  * 上传素材接口
	 * @param group 组别（可选）
	 * @param company_id （公司编号，建议默认设置为1）
	 * url===>ZHSD/upload/upload.php
	 * @throws Exception 
	 * @throws IllegalStateException 
	 */
	@Override
	public Object uploadSamples(int company_id, String group, MultipartFile file) throws Exception {
		if (company_id > 0 /* && judgeGroup(group) */ && !file.isEmpty()) {
		   String fileName = file.getOriginalFilename();//name
	       String tempFilePath = System.getProperty("java.io.tmpdir") + fileName;
	       File tempFile = new File(tempFilePath);
	       file.transferTo(tempFile);//生成临时文件

	       // 获取文件属性（还要额外形参这里就不给出了） 一系列的存表 操作
	       String url = HTTP_ADD + PORT + "/ZHSD/upload/upload.php";
	       RestTemplate restTemplate = new RestTemplate();
	       HttpHeaders headers = new HttpHeaders();
	       headers.add("Accept",MediaType.APPLICATION_JSON.toString());
	       headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
	       MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();

	       //MultipartFile 直接转 fileSystemResource 是不行的
	       FileSystemResource resource = new FileSystemResource(tempFilePath);//把临时文件变成filesystemresource
	       
	       param.add("pic[]",resource);	       
	       param.add("company_id",company_id);      
	       if(group != null && StringUtils.hasText(group))
	    	   param.add("group",group);	    
	       HttpEntity<MultiValueMap<String,Object>> formEntity = new HttpEntity<>(param,headers);	
	       ResponseEntity<String> res = restTemplate.postForEntity(url,formEntity, String.class);
//	       ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url,formEntity, JSONObject.class);
//	       JSONObject jsonObject =responseEntity.getBody();
//	       System.out.println(jsonObject.get("id0"));
	       String bodyStr =res.getBody();
	       JSONObject bodyJson = JSONObject.fromObject(bodyStr);
	       JSONObject object = (JSONObject) bodyJson.get("id0");
	       String resultData = (String) object.get("result");
	       System.out.println(bodyJson);
	       System.out.println("resultData======="+resultData);
	       tempFile.delete();//删除临时文件文件
	       return bodyJson;
	   }
	   throw new CheckedException("参数输入格式有误，请检查"); 
	}
	/**
	 * 
	 */
	@Override
	public Object getAllSamples(/* String sourceType, */int company_id, String user_name, int is_total_file,
			int get_group_tree, boolean include_folder, /* String findType, String findValue, */int sort, String method, Pager pager) {
		if(company_id >0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("company_id",company_id);
			if(user_name != null && StringUtils.hasText(user_name)) 
				map.put("user_name", user_name);		     			
			if(ObjectUtils.containsElement(DATA_CHECK, is_total_file)) 
				map.put("is_total_file", is_total_file);  
			if(ObjectUtils.containsElement(DATA_CHECK, get_group_tree)) 
				map.put("get_group_tree", get_group_tree);		  
			if(ObjectUtils.containsElement(BOOLEAN_CHECK, include_folder)) 
				map.put("include_folder", include_folder);
		    if(ObjectUtils.containsElement(SORT_CHECK, sort)) 
		    	map.put("sort", sort);		
		    map.put("method", method);
	    	//=====加入厂商加入分页=====
	    	//obj.put("pager", pager);
		    String url = HTTP_ADD + PORT + "/ZHSD/group/get_group_source.php";
//		    String result = restTemplate.getForObject(HTTP_ADD + PORT + "/ZHSD/group/get_group_source.php", String.class, map);
			String result = restTemplate.postForObject(url, map, String.class);
			//解析返回值,假设有分页
			JSONObject jsonObj = JSONObject.fromObject(result);
			JSONObject res = new JSONObject();		
//			int total = (int) jsonObj.get("xxxxx");
//			JSONArray arr = (JSONArray) jsonObj.get("xxxxx");
//			List<Object> pageList = new ArrayList<>();
//			for (Object data : arr) {
//				pageList.add(data);
//			}
			//List<?> list = 
			//假如厂商接口调用无法分页，就需要我用下面方法分页排序
//			PageResult pageResult = getPageResult(result, pager, 10, 10);
			return jsonObj;
		}
		throw new CheckedException("参数输入格式有误，请检查"); 
	}
	/**
	 * 
	 */
	@Override
	public Object getSamplesByGroupAndSourceType(String sourceType, int company_id, String user_name, String group,
			int is_total_file, int get_group_tree, boolean include_folder, int sort,  String findType, String findValue, Pager pager) {
		if(company_id > 0 && StringUtils.hasText(sourceType) && StringUtils.hasText(group)) {	
			String url = HTTP_ADD + PORT + "/ZHSD/group/get_group_source.php";
		//	Map<String, Object> map = new HashMap<String, Object>();
			url += "?company_id=" + company_id + "&group=" + group + "&sourceType=" + sourceType;
//			map.put("company_id",company_id);
//			map.put("sourceType",sourceType);
			if(findValue != null && StringUtils.hasText(findValue))
				url += "&findValue=" + findValue;
			if(user_name != null && StringUtils.hasText(user_name))
				url += "&user_name=" + user_name;
//				map.put("user_name", user_name);      	
			if(ObjectUtils.containsElement(DATA_CHECK, is_total_file)) 
				url += "&is_total_file=" + is_total_file;
//				map.put("is_total_file", is_total_file);		 
//			if(ObjectUtils.containsElement(DATA_CHECK, get_group_tree)) 
//				url += "&get_group_tree=" + get_group_tree;
//				System.out.println("get_group_tree === do nothing");
//				map.put("get_group_tree", get_group_tree);		  
			if(ObjectUtils.containsElement(BOOLEAN_CHECK, include_folder)) 
				url += "&include_folder=" + include_folder;
//				map.put("include_folder", include_folder);		  		
		    if(ObjectUtils.containsElement(SORT_CHECK, sort)) 
		    	url += "&sort=" + sort;
//		    	map.put("sort", sort);	
		    if(pager != null) {
		    	int limit = pager.getRows();//每页显示条数
		    	int page = pager.getPage();//页码
		    	url += "&limit=" + limit + "&page=" + page;
		    }
		    	
//		    String urlPlus = HTTP_ADD + PORT + "/ZHSD/group/get_group_source.php" + "?company_id=" + company_id + "&group=" + group + "&sourceType=" + sourceType;
//		    String urlPostman = "http://192.168.1.89:8080/ZHSD/group/get_group_source.php?company_id=1&group=system/picture&sourceType=picture";
		    //		    String result = restTemplate.getForObject(HTTP_ADD + PORT + "/ZHSD/group/get_group_source.php", String.class, map);
		    String result = restTemplate.postForObject(url, null, String.class);
			JSONObject jsonObj = JSONObject.fromObject(result);
			int total = (int) jsonObj.get("total");
			//首字母大写
			sourceType = sourceType.substring(0,1).toUpperCase()+sourceType.substring(1);
			JSONArray arr = (JSONArray) ((JSONObject)jsonObj.get(sourceType)).get("list");
			List<Object> pageList = new ArrayList<>();
			for (Object data : arr) {
				pageList.add(data);
			}		
			return new PageResult<Object>(pageList, total);
		}
		throw new CheckedException("参数输入格式有误，请检查"); 
	}
	@Override
	public Object getSamplesByFileName(String sourceType, int company_id, String user_name, String group, int is_total_file,
			int get_group_tree, boolean include_folder, String findType, String findValue, int sort) {
		if(company_id > 0 && StringUtils.hasText(sourceType) && StringUtils.hasText(findValue)) {				
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("company_id",company_id);
			map.put("sourceType",sourceType);
			if(user_name != null)
				map.put("user_name", user_name);      	
			if(ObjectUtils.containsElement(DATA_CHECK, is_total_file)) 
				map.put("is_total_file", is_total_file);		 
			if(ObjectUtils.containsElement(DATA_CHECK, get_group_tree)) 
				map.put("get_group_tree", get_group_tree);		  
			if(ObjectUtils.containsElement(BOOLEAN_CHECK, include_folder)) 
				map.put("include_folder", include_folder);		  		
		    if(ObjectUtils.containsElement(SORT_CHECK, sort)) 
		    	map.put("sort", sort);	
		    if(group != null && StringUtils.hasText(group))
		    	map.put("group", group); 
//		    String result = restTemplate.getForObject(HTTP_ADD + PORT + "/ZHSD/group/get_group_source.php", String.class, map);
			String result = restTemplate.postForObject(HTTP_ADD + PORT + "/ZHSD/group/get_group_source.php", map, String.class);
			//解析返回值
			//解析返回值,假设有分页
//			JSONObject jsonObj = JSONObject.fromObject(result);
//			JSONObject obj = new JSONObject();	
//			int total = (int) jsonObj.get("xxxxx");
//			JSONArray arr = (JSONArray) jsonObj.get("xxxxx");
//			//此处不考虑分页，由于同名文件正常情况下不会有多个
//			List<Object> fileList = new ArrayList<>();
//			for (Object data : arr) {
//				fileList.add(data);
//			}			
//			return new PageResult(fileList, total);
			return result;
		}
		throw new CheckedException("参数输入格式有误，请检查"); 	
	}
	
	@Override
	public Object getSamplesByDirectoryName(String sourceType, int company_id, String user_name, int is_total_file,
			int get_group_tree, boolean include_folder, String findType, String findValue, int sort, Pager pager) {
		if(company_id > 0 && StringUtils.hasText(sourceType) && StringUtils.hasText(findValue)) {
			JSONObject obj = new JSONObject();		
			obj.put("company_id",company_id);
			obj.put("sourceType",sourceType);
			if(user_name != null)
				obj.put("user_name", user_name);      	
			if(ObjectUtils.containsElement(DATA_CHECK, is_total_file)) 
				obj.put("is_total_file", is_total_file);		 
			if(ObjectUtils.containsElement(DATA_CHECK, get_group_tree)) 
				obj.put("get_group_tree", get_group_tree);		  
			if(ObjectUtils.containsElement(BOOLEAN_CHECK, include_folder)) 
				obj.put("include_folder", include_folder);		  		
		    if(ObjectUtils.containsElement(SORT_CHECK, sort)) 
				obj.put("sort", sort);		 
		    //=====如果厂商可以分页，加入分页=====
	    	//obj.put("pager", pager);
			String result = restTemplate.postForObject(HTTP_ADD + PORT + "ZHSD/group/get_group_source.php", obj, String.class);
			
			JSONObject jsonObj = JSONObject.fromObject(result);
			int total = (int) jsonObj.get("xxxxx");
			JSONArray arr = (JSONArray) jsonObj.get("xxxxx");
			List<Object> pageList = new ArrayList<>();
			for (Object data : arr) {
				pageList.add(data);
			}			
			
			PageResult pageResult = getPageResult(result, pager, 10, 10);
			return pageResult;
		}
		throw new CheckedException("参数输入格式有误，请检查"); 
	}
	/**
	 * 删除素材 url===>ZHSD/delete/delete_batch.php
	 * @param type_num 要删除的资源类型数目 
	 * @param type 要删除的资源类型  $_REQUEST['type'.$i]
	 * @param id 资源id 字符串数组且数字中每个元素使用“，”隔开  $id = explode(',', $_REQUEST['id'.$i]);
	 */
	@Override
	public Object deleteSamples(int company_id, String type, String id) {		
		if(company_id > 0 && StringUtils.hasText(id) && StringUtils.hasText(type)) {
			JSONObject obj = new JSONObject();		
			int type_num = 1;
			String url = HTTP_ADD + PORT + "/ZHSD/delete/delete_batch.php";
			url += "?company_id=" + company_id + "&type1=" + type + "&id1=" + id + "&type_num=" + type_num;
			String result = restTemplate.postForObject(url, null, String.class);
			JSONObject res = JSONObject.fromObject(result);		
			return res; 
		}
		throw new CheckedException("参数输入格式有误，请检查"); 
	}
	/**
	 * 发布素材 url===>ZHSD/release_program/release_program.php
	 * @param method 固定为post
	 * @param tid 终端设备的id号，例如tid=1,2,3,4  字符串：$tid_id = explode(',', $data['tid']);
	 * @param type_num 发布的资源类型数
	 * @param type video(视频) music(音频)  picture(图片)..... 字符串： if (!isset($data['type'.$i]) OR $data['type'.$i] == '') {
	 * @param id 资源id以逗号为分隔符，例如：id1=12,13,14     $id = explode(',', $data['id' . $type_num]);
	 * @param links 大图,小图的关联(link=111-123|125-126)
	 * @param publish_type (可选)order(预约发布),check(审核发布)，默认为立即发布
	 * @param status online (可选) (在线),offline(离线),standby(待机),all(所有),在tid参数不存在时有用
	 * @param group (可选)分组，在tid参数不存在时有用
	 * @param date (可选)预约发布时间(2018-01-05 12:00:00)
	 * @param company_id 公司编号
	 * @param immediate (可选)即时播放(0:播放一次，1，按设置时间播放)
	 * @param immediate_time (可选)即时播放时间
	 * @param nickname (可选)发布名称
	 * @param release_record_id (可选)发布列表id编号
	 * @param user_name (可选)是否为超级用户(SuperAdministrator，默认为空)
	 * @param is_cover (可选)是否覆盖(1:覆盖,0:追加,默认为0)
	 * @param playlist (可选)时间编排路径(需要在获取发布前保存的时间编排路径)
	 */
	@RequestMapping("/publishSamples")
	@Override
	public Object publishSamples(String tid, /* int type_num, */String type, String id, String link,
			String publish_type, String status/* , String group */, Date date, int company_id, int immediate,
			Date immediate_time, String nickname, int release_record_id/* , String user_name */,
			int is_cover/*, String playlist*/) {
		if(company_id > 0 && StringUtils.hasText(id) && StringUtils.hasText(type)) {
			String url = HTTP_ADD + PORT + "/ZHSD/release_program/release_program.php";		
			OkHttpClient client = new OkHttpClient().newBuilder()
					  .build();
			Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
			int type_num = 1;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			builder.addFormDataPart("company_id", String.valueOf(company_id))
			  .addFormDataPart("tid", tid)
			  .addFormDataPart("id1", id)
			  .addFormDataPart("type_num", String.valueOf(type_num))
			  .addFormDataPart("type1", type);							
			//针对可选项判断
			 if(publish_type != null && PUBLISH_TYPE_CHECK.indexOf(publish_type) != -1) 
				 builder.addFormDataPart("publish_type", publish_type);			 
			 if(status != null && STATUS_CHECK.indexOf(status) != -1) 
				 builder.addFormDataPart("status", status);	 
			/*
			 * if(date != null) { String time= sdf.format(date);
			 * builder.addFormDataPart("date", time); }
			 * if(ObjectUtils.containsElement(DATA_CHECK, immediate)) {
			 * builder.addFormDataPart("immediate", String.valueOf(immediate)); if(immediate
			 * == 1) { String imm_time= sdf.format(immediate_time);
			 * builder.addFormDataPart("immediate_time", imm_time); } }
			 */
			 
			if(nickname != null && StringUtils.hasText(nickname))
				builder.addFormDataPart("nickname", nickname);	
			if(release_record_id > 0)
				builder.addFormDataPart("release_record_id", String.valueOf(release_record_id));	
			if(ObjectUtils.containsElement(DATA_CHECK, is_cover))
				builder.addFormDataPart("is_cover", String.valueOf(is_cover));	

			RequestBody body = builder.build();
			Request request = new Request.Builder()
			  .url(url)
			  .method("POST", body)
			  .addHeader("Content-Type", "application/json")
			  .build();
			try {
				Response response = client.newCall(request).execute();
				String res = response.body().string();
				System.out.println(res);
				JSONObject obj = JSONObject.fromObject(res);
				return obj; 
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}		
		throw new CheckedException("参数输入格式有误，请检查"); 
	}
	
	@Override
	public Object getAllTerminalDevices(int company_id, String status, int sort, String user_name, Pager pager) {
		// TODO Auto-generated method stub
		if(company_id <= 0)
			throw new CheckedException("公司编号不允许<= 0");
		String url = HTTP_ADD + PORT + "/ZHSD/terminal/terminal.php";
		url += "?company_id=" + company_id;
		if(status != null && STATUS_CHECK.indexOf(status) != -1) 
			 url += "&status=" + status;	
		if(ObjectUtils.containsElement(SORT_CHECK, sort)) 
			url += "&sort=" + sort;	
		if(user_name != null && StringUtils.hasText(user_name)) 
			url += "&user_name=" + user_name;
		if(pager != null) {
	    	int limit = pager.getRows();//每页显示条数
	    	int page = pager.getPage();//页码
	    	url += "&limit=" + limit + "&page=" + page;
	    }
		String result = restTemplate.postForObject(url, null, String.class);		
		//解析返回值,假设有分页
		JSONObject jsonObj = JSONObject.fromObject(result);
		//由于setting属性段，返回null属性值，下面进行处理
		JSONObject jsonAfter = BhJsonUtils.filterNull(jsonObj);
		Object obj1 = jsonAfter.get("rows");
		Object obj2 = jsonAfter.get("num");
		Object obj3 = jsonAfter.get("results");
		int total = jsonAfter.getInt("num");
		//首字母大写
		JSONArray arr = (JSONArray) jsonAfter.get("list");
		List<Object> pageList = new ArrayList<>();
		for (Object data : arr) {
			pageList.add(data);
		}		
		return new PageResult<Object>(pageList, total);
	}

	
	@Override
	public Object getTerminalDevicesByName(int company_id, String group, String status, int sort, String user_name,
			String findType, String findValue, Pager pager) {
		if(company_id > 0 && FIND_TYPE_CHECK.indexOf(findType) != -1 && StringUtils.hasText(findValue)) {
			JSONObject obj = new JSONObject();		
			obj.put("company_id",company_id);
			//判断	    
			if(group != null) 
				obj.put("group", group);	
			if(status != null && STATUS_CHECK.indexOf(status) != -1) 
				 obj.put("status",status);
			if(ObjectUtils.containsElement(SORT_CHECK, sort)) 
				obj.put("sort", sort);
			if(user_name != null) 
				obj.put("user_name", user_name);  
			//查询
			obj.put("method", "get");
			String result = restTemplate.postForObject(HTTP_ADD + PORT + "/ZHSD/terminal/terminal.php", obj, String.class);		
			//解析返回值,假设有分页
			JSONObject jsonObj = JSONObject.fromObject(result);
			int total = (int) jsonObj.get("xxxxx");
			JSONArray arr = (JSONArray) jsonObj.get("xxxxx");
			List<Object> pageList = new ArrayList<>();
			for (Object data : arr) {
				pageList.add(data);
			}
			//List<?> list = 
			//假如厂商接口调用无法分页，就需要我用下面方法分页排序
			PageResult pageResult = getPageResult(result, pager, 10, 10);
			return pageResult;
		}
		throw new CheckedException("参数输入格式有误，请检查");
		
	}
	
	/**
	 * 
	 */
	@Override
	public Object insertTerminalDevices(String id, int company_id, String userGivenName) {
		if(company_id > 0 && StringUtils.hasText(id) && StringUtils.hasText(userGivenName)) {
			JSONObject obj = new JSONObject();
			obj.put("id", id);		
			obj.put("company_id",company_id);
		    obj.put("userGivenName", userGivenName);
			obj.put("method", "put");
			String result = restTemplate.postForObject(HTTP_ADD + PORT + "/ZHSD/terminal/terminal.php", obj, String.class);					
			//获取返回值
			JSONObject jsonObj = JSONObject.fromObject(result);
			
			jsonObj.put("result", "success");
			jsonObj.put("id", "====");
			jsonObj.put("message", "====");
			return jsonObj;
		}
		throw new CheckedException("参数输入格式有误，请检查");
	}

	/**
	 * url===>ZHSD/terminal/terminal.php
	 * @param id (终端的id号，以逗号隔开，如id=2,3,4,5,6，7)
	 * @param company_id (公司编号)
	 * @param group (组别，默认system)(可选)
	 * @param status (online:在线，offline:离线,standby:待机，默认所有)(可选)
	 * @param user_name SuperAdministrator,默认为空)(可选)
	 * @return
	 */
	@Override
	public Object delTerminalDevices(String id, int company_id, /* String group, */String status, String user_name) {
		if(company_id > 0 && StringUtils.hasText(id)) {
			String url = HTTP_ADD + PORT + "/ZHSD/terminal/terminal.php";
			url += "?company_id=" + company_id + "&id=" + id;
			if(status != null && STATUS_CHECK.indexOf(status) != -1) 
				url += "?status=" + status;
			if(user_name != null && StringUtils.hasText(user_name)) 
				url += "?user_name=" + user_name;
			//删除
			url += "?method=delete";
			String result = restTemplate.postForObject(url, null, String.class);		
			
			//获取返回值
			JSONObject jsonObj = JSONObject.fromObject(result);			
//			jsonObj.put("result", "success");
//			jsonObj.put("id", "====");
//			jsonObj.put("message", "====");			
			return jsonObj;
		}
		throw new CheckedException("参数输入格式有误，请检查");
	}
	/**
	 * url===>ZHSD/terminal/terminal.php
	 * @param id 终端id编号
	 * @param company_id (公司编号)
	 * @param userGivenName 用户自定义名称
	 * @param value 
	 * @return
	 */
	@Override
	public Object updateTerminalDevices(String id, int company_id, String userGivenName) {
		if(company_id > 0 && StringUtils.hasText(id) && StringUtils.hasText(userGivenName)) {
//			JSONObject obj = new JSONObject();
			String url = HTTP_ADD + PORT + "/ZHSD/terminal/terminal.php";
			url += "?company_id=" + company_id + "&id=" + id + "&userGiveName=" + userGivenName + "&method=put";
			String result = restTemplate.postForObject(url, null, String.class);					
			//获取返回值
			JSONObject jsonObj = JSONObject.fromObject(result);
			
//			jsonObj.put("result", "success");
//			jsonObj.put("id", "====");
//			jsonObj.put("message", "====");
			return jsonObj;
		}
		throw new CheckedException("参数输入格式有误，请检查");
	}
	
	@Override
	public Object getAllTerminalStatus(int company_id, String user_name) {
		if(company_id <= 0)
			throw new CheckedException("公司编号不允许<= 0");
		String url = HTTP_ADD + PORT + "/ZHSD/terminal/terminal.php";
		url += "?company_id=" + company_id  + "&method=total";		
		if(user_name != null && StringUtils.hasText(user_name)) 
			url += "&user_name=" + user_name;  	
		String result = restTemplate.postForObject(url, null, String.class);		
		//获取返回值
		JSONObject jsonObj = JSONObject.fromObject(result);
		return jsonObj;
	}
	@Override
	public Object getTerminalStatusByName(int company_id, String group, String user_name, String findType,
			String findValue) {
		if(company_id > 1 && StringUtils.hasText(findValue) && StringUtils.hasText(findType)) {
			JSONObject obj = new JSONObject();		
			obj.put("company_id", company_id);	
			obj.put("findType", findType);
			obj.put("findValue", findValue);
			if(user_name != null && StringUtils.hasText(user_name)) 
				obj.put("user_name", user_name);  	
			//查询
			obj.put("method", "total");
			String result = restTemplate.postForObject(HTTP_ADD + PORT + "/ZHSD/terminal/terminal.php", obj, String.class);		
			//获取返回值
			JSONObject jsonObj = JSONObject.fromObject(result);

			jsonObj.put("total", "success");
			jsonObj.put("online", "====");
			jsonObj.put("offline", "====");
			jsonObj.put("standby", "success");
			jsonObj.put("all", "====");
			jsonObj.put("buffer", "====");
			jsonObj.put("download", "success");
			jsonObj.put("reservation", "====");
			jsonObj.put("check", "====");
			return jsonObj;
		}
		throw new CheckedException("参数输入格式有误，请检查");
	}

	@Override
	public void save_setting(String setting, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String get_setting(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * url===>ZHSD/group_terminal/group_terminal.php
	 * @param company_id (公司编号)
	 * @param url 所属分组???
	 * @param user_name (是否为超级用户:SuperAdministrator)(可选) 
	 * @return
	 */
	@Override
	public Object getTerminalGroup(int company_id, String user_name, String url, Pager pager) {
		if(company_id <= 0)
			throw new RuntimeException("公司编号不允许<= 0");
		JSONObject obj = new JSONObject();		
		obj.put("company_id",company_id);
		//判断	    
		if(url != null && StringUtils.hasText(url)) {
			//增加url健壮性判断
			
			obj.put("url", url);
		}
		if(user_name != null && StringUtils.hasText(user_name)) 
			obj.put("user_name", user_name);
		//查询
		obj.put("method", "get");
		//===如果可以分页带上pager
		//obj.put("pager", pager);
		String result = restTemplate.postForObject(HTTP_ADD + PORT + "ZHSD/group_terminal/group_terminal.php", obj, String.class);		
		
		JSONObject jsonObj = JSONObject.fromObject(result);
		int total = (int) jsonObj.get("xxxxx");
		JSONArray arr = (JSONArray) jsonObj.get("xxxxx");
		List<Object> pageList = new ArrayList<>();
		for (Object data : arr) {
			pageList.add(data);
		}			
		PageResult pageResult = getPageResult(result, pager, 0, 0);
		return pageResult;
	}
	/**
	 * url===>ZHSD/group_terminal/group_terminal.php
	 * @param company_id (公司编号)
	 * @param group_name (分组名)
	 * @param url 所属分组(里面用的是group内容，(分组，图片(system/picture)，视频(system/video)，音乐(system/music)......))
	 * @return
	 */
	@Override
	public Object insertTerminalGroup(int company_id, String group_name, String url) {
		if(company_id > 0 && StringUtils.hasText(url) && StringUtils.hasText(group_name)) {			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("company_id",company_id);
			//判断	    
			map.put("url", url);
			map.put("group_name", group_name);
			//查询
			map.put("method", "post");
			String result = restTemplate.postForObject(HTTP_ADD + PORT + "ZHSD/group_terminal/group_terminal.php", map, String.class);		
			JSONObject obj = new JSONObject();	
			JSONObject jsonObj = JSONObject.fromObject(result);
			String res = jsonObj.getString("result");
			String num = jsonObj.getString("num");
			String message = jsonObj.getString("message");
			obj.put("result", res);
			obj.put("num", num);
			obj.put("message", message);
			return obj;
		}
		throw new CheckedException("参数输入格式有误，请检查");		
	}
	/**
	 * 
	 * @param company_id (公司编号)
	 * @param group_name (分组名)
	 * @param url 所属分组
	 * @param is_empty_group (true:是清空该分组下所属终端，默认为false)(可选)
	 * @param del_curr_group (true:清空分组，包括删除group_name分组，false:清空分组，但不删除group_name分组，默认为false)(可选)
	 * @param is_keep_source (true:删除分组，同时分组下面的终端设置为默认分组,false:删除分组下所有终端，默认为false)(可选)
	 * @return
	 */
	@Override
	public Object delTerminalGroup(int company_id, String group_name, String url, boolean is_empty_group,
			boolean del_curr_group, boolean is_keep_source) {
		if(company_id > 0 && StringUtils.hasText(url) && StringUtils.hasText(group_name)) {					
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("company_id",company_id);			
			map.put("url", url);			
			map.put("group_name", group_name);
			if(ObjectUtils.containsElement(BOOLEAN_CHECK, is_empty_group)) 
				map.put("is_empty_group", is_empty_group);
			if(ObjectUtils.containsElement(BOOLEAN_CHECK, is_keep_source)) 
				map.put("is_keep_source", is_keep_source);
			if(ObjectUtils.containsElement(BOOLEAN_CHECK, del_curr_group)) 
				map.put("del_curr_group", del_curr_group);
			//查询
			map.put("method", "delete");
			String result = restTemplate.postForObject(HTTP_ADD + PORT + "ZHSD/group_terminal/group_terminal.php", map, String.class);		
			JSONObject obj = new JSONObject();	
			JSONObject jsonObj = JSONObject.fromObject(result);
			String res = jsonObj.getString("result");
			String num = jsonObj.getString("num");
			String message = jsonObj.getString("message");
			obj.put("result", res);
			obj.put("num", num);
			obj.put("message", message);
			return obj;
		}
		throw new CheckedException("参数输入格式有误，请检查");	
		
	}
	/**
	 * 
	 * @param company_id (公司编号)
	 * @param group_name (分组名)
	 * @param url 所属分组
	 * @param value 修改后的组名
	 * @return
	 */
	@Override
	public Object updateTerminalGroup(int company_id, String group_name, String url, String value) {
		if(company_id > 0 && StringUtils.hasText(url) && StringUtils.hasText(group_name) && StringUtils.hasText(value)) {					
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("company_id",company_id);
			map.put("url", url);			 
			map.put("group_name", group_name);
			map.put("value", value);
			//查询
			map.put("method", "put");
			String result = restTemplate.postForObject(HTTP_ADD + PORT + "ZHSD/group_terminal/group_terminal.php", map, String.class);		
			JSONObject obj = new JSONObject();	
			JSONObject jsonObj = JSONObject.fromObject(result);
			String res = jsonObj.getString("result");
			String num = jsonObj.getString("num");
			String message = jsonObj.getString("message");
			obj.put("result", res);
			obj.put("num", num);
			obj.put("message", message);
			return obj;
		}
		throw new CheckedException("参数输入格式有误，请检查");
		
	}
	/**
	 * 
	 * @param company_id (公司编号)
	 * @param user_name (是否为超级用户:SuperAdministrator)(可选)
	 * @param url 所属分组
	 * @return
	 */
	@Override
	public Object getTotalTerminalGroup(int company_id, String user_name, String url) {
		// TODO Auto-generated method stub
		if(company_id > 0 && StringUtils.hasText(url)) {
			Map<String, Object> map = new HashMap<String, Object>();		
			map.put("company_id",company_id);
			map.put("url", url);			
			if(user_name != null && StringUtils.hasText(user_name)) 
				map.put("user_name", user_name);
			//查询
			map.put("method", "total");
			String result = restTemplate.postForObject(HTTP_ADD + PORT + "ZHSD/group_terminal/group_terminal.php", map, String.class);		
			JSONObject res = JSONObject.fromObject(result);
			String total = res.getString("total");			
			return total;
		}
		throw new CheckedException("参数输入格式有误，请检查");		
	}
	
	private boolean judgeGroup(String group) {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public List<Object> orderResByDate(String result, Date date) {
		List<Object> list = new ArrayList<Object>();
		JSONObject jsonObj = JSONObject.fromObject(result);
		JSONArray jsonArr = jsonObj.getJSONArray("list");
		  for (int i = 0; i < jsonArr.size(); i++) { 
		    	 JSONObject obj = (JSONObject)jsonArr.get(i);
		    	 Date dateFromVO = (Date) obj.get("date");
		    	 boolean flag = judgeDate(date, dateFromVO);
		    	 if(flag) {
		    		 list.add(obj);
		    	 }
		     }
		return list;
	}

	private boolean judgeDate(Date date, Date dateFromVO) {
		// TODO Auto-generated method stub
		return false;
	}
	private PageResult getPageResult(String result, Pager pager, int total, int sort) {
		// TODO Auto-generated method stub
		return null;
	}


	

	

	
	
	

	
}//end
