package com.bhyh.controller;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.bhyh.enums.ResultCodeEnum;
import com.bhyh.model.vo.ImageDataVO;
import com.bhyh.model.vo.ImgResSummaryVO;
import com.bhyh.model.vo.VideoDataVO;
import com.bhyh.result.RestResult;
import com.bhyh.result.ResultUtils;
import com.bhyh.service.IFileUpAndDownService;
import com.bhyh.service.ITokenService;
import com.bhyh.utils.BhBase64Util;
import com.bhyh.utils.BhDateUtils;
import com.bhyh.utils.BhJsonUtils;
import com.bhyh.utils.BhObjectUtils;
import com.bhyh.utils.BhStringUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
//import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 送审图片及送审视频 的controller
 * @author dyonga
 *
 */
@RestController
@RequestMapping("/tokenController")
@Slf4j
public class TokenController {
   
   
   @Autowired
   private	ITokenService tokenService;
   @Autowired
   private	IFileUpAndDownService fileUpAndDownService;
   @Value("${server.port}")
   private String  VISIT_PORT; 
   @Value("${server.ip}")
   private String  VISIT_IP;   
   private final String HOST = "kir.api.ksyun.com";
   private final String METHOD = "POST";
   private final String SERVICE = "kir";
   private final String IMG_ACTION = "ClassifyImageGuard";
   private final String VIDEO_CHECK_ACTION = "AnalyzeVideo";
   private final String VIDEO_RESULT_ACTION = "GetAnalyzeVideoResult";
   private final String IMAGE_VERSION = "2019-01-18";
   private final String VIDEO_VERSION = "2018-09-03";
   private final String ACCESS_KEY = "AKLTTh-j56W0TyuYyvcrtj54hQ";
   private final String SECRET_KEY = "OASIKMtolLE74pr34/16MjTNTRSeLvxJBoImIlcvHn5NYEgxIQgAtwhsUmA93zhMRQ==";
   public final static RestTemplate restTemplate = new RestTemplate();	
   /**
    * 涉黄涉恐等图片识别返回
    * @param postData 送审图片的url地址
    * @return
    */
    @RequestMapping(value="/tokenImageUrlRequest",method=RequestMethod.POST)
    @ApiOperation(value = "送审图片接口", notes = "")
	public RestResult tokenImageUrlRequest(@RequestBody Map<String,String> map) {    
	    String postData = map.get("postData");
	    System.out.println("传进来的***postdata="+postData);
	    //拼装postdata
	    postData = "{\n    \"guard_id\": \"1577327396146478400\",\n    \"image_url\": " + "\""+postData + "\""+"\n}";
		String result = null;
		try {
			System.out.println("hello controller======");
			System.out.println("postData= "+postData);
			result = tokenService.tokenRequest(HOST, METHOD, SERVICE, IMG_ACTION, IMAGE_VERSION, ACCESS_KEY, SECRET_KEY, postData);
			JSONObject obj = JSONObject.fromObject(result);		
			/**保存到db*/ 
			
			 return ResultUtils.success(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtils.error(1, "请求失败");
	}
   
    /**
     * 涉黄涉恐等图片识别返回
     * @param file 送审图片的file文件流
     * @return
     */
     @RequestMapping(value="/tokenImageFileRequest",method=RequestMethod.POST)
     @ApiOperation(value = "送审图片接口", notes = "")
 	public RestResult tokenImageFileRequest(@RequestParam(value = "file", required = false) MultipartFile file) {    
    	 try {
    	   System.out.println("tokenImageRequest");	 
    	   Map<String, Object> resultMap = uploadImage(file);//接收图片
    	   if(ResultCodeEnum.SUCCESS.getMsg().equals(resultMap.get("result"))) {//成功上传图片
    		    String http_visit_path = (String) resultMap.get("http_visit_path");//接收图片成功后，resources/static/下的图片文件夹路径
	   	 	    //拼装image_url   http://localhost:8080/20200107/dbe6b757bee845afa44e788abd31c25f.jpeg
    	//	    String image_url = "http://183.250.155.231:" + VISIT_PORT + http_visit_path;
    		    String image_url = VISIT_IP + ":" + VISIT_PORT + http_visit_path;
    		    System.out.println("接收图片成功后，本地储存的路径======="+http_visit_path);
	   	 	    //拼装postdata
	   	 	    String postData = "{\n    \"guard_id\": \"1577327396146478400\",\n    \"image_url\": " + "\""+image_url + "\""+"\n}";
	   	 		System.out.println("postData>>>>>>>>"+postData);
	 //  	 	    String result = null;
	   	 	    JSONObject jsonObj = null;
    		    try {
	//    			result = tokenService.tokenRequest(HOST, METHOD, SERVICE, IMG_ACTION, IMAGE_VERSION, ACCESS_KEY, SECRET_KEY, postData);
	//    			jsonObj = JSONObject.fromObject(result);	
	    			System.out.println(jsonObj);
    		    }catch (Exception e) {
					log.error("送审图片过程出错", e.getCause());
					e.printStackTrace();
					return ResultUtils.error(1, "送审图片过程出错");
				}
    			/**保存到db*/ 
    		    //===简单模拟测试，现在无法使用金山云，只能本地测试
    		    try {
    		    	ImageDataVO vo = new ImageDataVO();
    		    	//vo.setId("2");
        		    vo.setImageUrl("bettter, tomorrow@bhyh.com");
        		    vo.setCreateTime(BhDateUtils.getGlobalCurrentDate());
        		    tokenService.insertImageDataVO(vo);
    		    }catch (Exception e) {					
    //		    	log.error("==========持久化到db，出错=======", e.getCause());
    		    	log.error("==========持久化到db，出错=======", e);
    		    	e.printStackTrace();
				//	throw new RuntimeException("===========持久化到db，出错=======", e.getCause());
    		    	 return ResultUtils.error(2, "请求失败");
				}
    		    
//    			ImageDataVO vo = new ImageDataVO();
//    			vo.setImageUrl(postData);
//    			vo.setStatus(0);
//    			vo.setSuggestSummary(jsonObj.getString("suggest_summary"));
//    			vo.setSuggestSummaryCode(jsonObj.getString("suggest_summary_code"));
//    			vo.setCreateTime(BhDateUtils.getGlobalCurrentDate());
//    			vo.setResults(jsonObj.toString().getBytes());
//    			tokenService.insert(vo);
    			return ResultUtils.success(jsonObj);
    	   }else {//上传图片失败
    		   return ResultUtils.error(2, "请求失败");
    	   }
 		} catch (Exception e) {
 			log.error(">>>>>>>在tokenImageRequest时出现异常====", e.getCause());
 			e.printStackTrace();
 			return ResultUtils.error(2, "请求失败");	
 		}	
 	}
    /**
     * 接收上传过来的图片 
     * @param file 图片
     * @return 图片本地化存储后的详细信息
     */
    private Map<String, Object> uploadImage(MultipartFile file){
    	Map<String, Object> returnMap = new HashMap<String, Object>();
    	try {
    		if(!file.isEmpty()) {
    			Map<String, Object> picMap = fileUpAndDownService.uploadPicture(file);
    			if(ResultCodeEnum.SUCCESS.getMsg().equals(picMap.get("result"))) {//接收，存储成功
    				return picMap;
    			}else {//接收，存储失败
    			//	log.info(">>>>>>>接收，存储失败");
    				returnMap.put("result", ResultCodeEnum.ERROR.getMsg());
    				returnMap.put("msg", picMap.get("result"));
    			}
    		}else {
    		//	log.info(">>>>>>>上传图片为空文件");
    			returnMap.put("result", ResultCodeEnum.ERROR.getMsg());
    			returnMap.put("msg", ResultCodeEnum.FILE_UPLOAD_NULL.getMsg());
    		}
    	}catch (Exception e) {
    		log.error(">>>>>>>upload方法出现异常", e.getCause());
			e.printStackTrace();
			throw new RuntimeException("在uploadImage时出现异常===", e);
		}
		return returnMap; 	
    }
    
   /**
    * 涉黄涉恐等视频识别返回（字符串返回）
    * @param access_key
    * @param secret_key
    * @param postData
    * @return
    */
   
   /**
    * JsonObject对象返回
    * @param map
    * @return
    */
 //视频识别接口
   @RequestMapping(value="/tokenVideoUrlRequest",method=RequestMethod.POST)
   @ApiOperation(value = "获取签名密匙接口", notes = "根据请求参数返回签名密匙，json参数：host:主机名, method:请求方式，service:方法名，action：controller类名，"
   		+ "version：版本号， access_key：密匙， secret_key：密匙再加密后， postData: 请求参数 ")
	public RestResult tokenVideoUrlRequest(@RequestBody Map<String,String> map) {
	    System.out.println("i am tokenVideoJsonRequest ^^^^^^^^^");
		String toCheckResult = null;
		String getResult = null;
		try {
			 String postData = map.get("postData");
			 String video_id = BhStringUtils.getRandomString(13);
			 System.out.println("video_id==="+video_id);
			 postData = "{\n    \"guard_id\": \"1576588647262478651\",\n    \"video_url\": " + "\""+postData + "\""+",\n    \"video_id\": "+ "\""+video_id+"\"\n}";  
             //进行送审动作
			 toCheckResult = tokenService.tokenRequest(HOST, METHOD, SERVICE, VIDEO_CHECK_ACTION, VIDEO_VERSION, ACCESS_KEY, SECRET_KEY, postData);
			 System.out.println("toCheckResult==="+toCheckResult);	
			 //增加判断，是否送审成功==>"header":{"err_no":200,"err_msg":"success"}
			 if(toCheckResult.indexOf("{\"err_no\":200,\"err_msg\":\"success\"}") < 0) {
				 return ResultUtils.error(1, "送审失败");
			 }
			 //while循环判断送审结果是否已经成功返回，若审核中，则等待5s后不断重复请求
			 int count = 0;
			 while(!JSONObject.fromObject(getResult).containsKey("body")) {//无body时，证明还没有返回结果，结果还在审核中
				 Thread.sleep(5000);//等待5s 
				 System.out.println("count==============="+ (count++));
				//进行获取送审结果的动作
				 getResult = tokenService.tokenRequest(HOST, METHOD, SERVICE, VIDEO_RESULT_ACTION, VIDEO_VERSION, ACCESS_KEY, SECRET_KEY, postData);	 
			     System.out.println("getResult====="+getResult);
			 }
			//判断返回的结果
			 JSONObject getResultObj = JSONObject.fromObject(getResult);
  		     System.out.println("jsonObjToString=="+getResultObj.toString());
  		     //替换jsonobject中属性值为null的字段，替换成""
			 BhJsonUtils.filterNull(getResultObj);
			 System.out.println("jsonObj after=="+getResultObj.toString());	
			 //过滤结果，仅保留body属性中image_result属性值，并且suggest不是normal的值
			 JSONArray jsonArray = BhJsonUtils.filterByPropertyNmAndSug(getResultObj);
			 System.out.println("jsonArr"+jsonArray);
			 //替换business属性值为中文，且添加business属性到detail每个jsonObject对象中
			 JSONArray tranfer2NewArr = BhJsonUtils.tranfer2NewArr(jsonArray);
			 System.out.println("tranfer2NewArr==="+tranfer2NewArr);
			 
			 JSONArray jsonArr2Return = new JSONArray();
			 //提取detail的jsonObject数组重新组合成新的jsonArr
		     for (int i = 0; i < tranfer2NewArr.size(); i++) { 
		    	 JSONObject object = (JSONObject)tranfer2NewArr.get(i);
		    	 JSONArray detailObj = (JSONArray)object.get("detail"); 
		    	 if(detailObj != null) {//增加健壮性判断
		    //	 newArr_2.add(detailObj); size为添加的array个数
		    	 jsonArr2Return.addAll(detailObj);//size为几个array中的jsonObject数目总和
		    	 }
		     }
			 System.out.println("jsonArr2Return==="+jsonArr2Return);	
			 System.out.println("jsonArr2Return.size()==="+jsonArr2Return.size());			 
			 return ResultUtils.success(jsonArr2Return);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtils.error(1, "请求失败");	
	}
   
   /**
    * JsonObject对象返回
    * @param file 上传的视频流对象
    * @return
    */
 //视频识别接口
   @RequestMapping(value="/tokenVideoFileRequest",method=RequestMethod.POST)
   @ApiOperation(value = "送审视频")
	public RestResult tokenVideoFileRequest(@RequestParam(value = "file", required = false) MultipartFile file) {
	    System.out.println("i am tokenVideoFileRequest ^^^^^^^^^");
		String toCheckResult = null;
		String getResult = null;
		try {			 
	        Map<String, Object> resultMap = uploadVideo(file);
	    	if(ResultCodeEnum.SUCCESS.getMsg().equals(resultMap.get("result"))) {//成功接收视频
		    	String http_visit_path = (String) resultMap.get("http_visit_path");//接收视频成功后，resources/static/xxx
			   	//拼装image_url   http://localhost:8080/20200107/dbe6b757bee845afa44e788abd31c25f.jpeg
		        String video_url = "http://183.250.155.231:" + VISIT_PORT + http_visit_path;
		  //      System.out.println("接收视频成功后，本地储存的路径==="+http_visit_path);
				 String video_id = BhStringUtils.getRandomString(13);
				 System.out.println("video_id==="+video_id);
				 String postData = "{\n    \"guard_id\": \"1576588647262478651\",\n    \"video_url\": " + "\""+video_url + "\""+",\n    \"video_id\": "+ "\""+video_id+"\"\n}";  
	             System.out.println("postData>>>>>>>>>>"+postData);
				 //进行送审动作
				 toCheckResult = tokenService.tokenRequest(HOST, METHOD, SERVICE, VIDEO_CHECK_ACTION, VIDEO_VERSION, ACCESS_KEY, SECRET_KEY, postData);
				 System.out.println("toCheckResult==="+toCheckResult);	
				 //增加判断，是否送审成功==>"header":{"err_no":200,"err_msg":"success"}
				 if(toCheckResult.indexOf("{\"err_no\":200,\"err_msg\":\"success\"}") < 0) {
					 return ResultUtils.error(1, "送审失败");
				 }
				 //while循环判断送审结果是否已经成功返回，若审核中，则等待5s后不断重复请求
				 int count = 0;
				 while(!JSONObject.fromObject(getResult).containsKey("body")) {//无body时，证明还没有返回结果，结果还在审核中
					 Thread.sleep(5000);//等待5s 
					 System.out.println("count==============="+ (count++));
					//进行获取送审结果的动作
					 getResult = tokenService.tokenRequest(HOST, METHOD, SERVICE, VIDEO_RESULT_ACTION, VIDEO_VERSION, ACCESS_KEY, SECRET_KEY, postData);	 
				     System.out.println("getResult====="+getResult);
				 }
				//判断返回的结果
				 JSONObject getResultObj = JSONObject.fromObject(getResult);
	  		     System.out.println("jsonObjToString=="+getResultObj.toString());
	  		     //=======提前取出video_id，"video_url video_duration video_suggestion suggestSummaryCode suggestSummaryMessage
	  		     JSONObject getResultBodyObj = (JSONObject) getResultObj.get("body");
	  		     String video_duration = getResultBodyObj.getString("video_duration");
	  		     String video_suggestion = getResultBodyObj.getString("video_suggestion");
	  		     String suggestSummaryCode = getResultBodyObj.getString("suggestSummaryCode");
	  		     String suggestSummaryMessage = getResultBodyObj.getString("suggestSummaryMessage");
	  		     //=============================
	  		     //替换jsonobject中属性值为null的字段，替换成""
				 BhJsonUtils.filterNull(getResultObj);
				 System.out.println("jsonObj after=="+getResultObj.toString());	
				 //过滤结果，仅保留body属性中image_result属性值，并且suggest不是normal的值
				 JSONArray jsonArray = BhJsonUtils.filterByPropertyNmAndSug(getResultObj);
				 System.out.println("jsonArr"+jsonArray);
				 //替换business属性值为中文，且添加business属性到detail每个jsonObject对象中
				 JSONArray tranfer2NewArr = BhJsonUtils.tranfer2NewArr(jsonArray);
				 System.out.println("tranfer2NewArr==="+tranfer2NewArr);
				 
				 JSONArray jsonArr2Return = new JSONArray();
				 //提取detail的jsonObject数组重新组合成新的jsonArr
			     for (int i = 0; i < tranfer2NewArr.size(); i++) { 
			    	 JSONObject object = (JSONObject)tranfer2NewArr.get(i);
			    	 JSONArray detailObj = (JSONArray)object.get("detail"); 
			    	 if(detailObj != null) {//增加健壮性判断
			    //	 newArr_2.add(detailObj); size为添加的array个数
			    	 jsonArr2Return.addAll(detailObj);//size为几个array中的jsonObject数目总和
			    	 }
			     }
				 System.out.println("jsonArr2Return==="+jsonArr2Return);	
				 System.out.println("jsonArr2Return.size()==="+jsonArr2Return.size());		
				 
	    			/**保存到db*/ 
	    			VideoDataVO vo = new VideoDataVO();
	    			vo.setVideoId(video_id);
	    			vo.setVideoUrl(video_url);
	    			vo.setVideoDuration(video_duration);
	    			vo.setVideoSuggestion(video_suggestion);
	    			vo.setResults(jsonArr2Return.toString().getBytes());
	    			vo.setStatus(0);
	    			vo.setSuggestSummaryCode(suggestSummaryCode);
	    			vo.setSuggestSummaryMessage(suggestSummaryMessage);
	    			vo.setCreateTime(BhDateUtils.getGlobalCurrentDate());
	    			tokenService.insertVideoDataVO(vo);
				 return ResultUtils.success(jsonArr2Return);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtils.error(1, "请求失败");	
	}
   
   /**
    * 接收上传过来的图片 
    * @param file 图片
    * @return 图片本地化存储后的详细信息
    */
   private Map<String, Object> uploadVideo(MultipartFile file){
   	Map<String, Object> returnMap = new HashMap<String, Object>();
   	try {
   		if(!file.isEmpty()) {
   			Map<String, Object> picMap = fileUpAndDownService.uploadVideo(file);
   			if(ResultCodeEnum.SUCCESS.getMsg().equals(picMap.get("result"))) {//接收，存储成功
   				return picMap;
   			}else {//接收，存储失败
   			//	log.info(">>>>>>>接收，存储失败");
   				returnMap.put("result", ResultCodeEnum.ERROR.getMsg());
   				returnMap.put("msg", picMap.get("result"));
   			}
   		}else {
   		//	log.info(">>>>>>>上传视频为空文件");
   			returnMap.put("result", ResultCodeEnum.ERROR.getMsg());
   			returnMap.put("msg", ResultCodeEnum.FILE_UPLOAD_NULL.getMsg());
   		}
   	}catch (Exception e) {
   		log.error(">>>>>>>uploadVideo方法出现异常");
			e.printStackTrace();
			throw new RuntimeException("上传视频失败", e);
		}
		return returnMap; 	
   }
   
   
   /**
    * 将图片发送给识别服务器进行判断
    * @param super_dir_path 送审图片的顶层文件夹url地址
    * file://SKY-20190523PWL/picture(局域网共享图片地址)
    * @return
    */
    @RequestMapping(value="/identificationImageUrlRequest",method=RequestMethod.POST)
 //   @ApiOperation(value = "送审图片接口", notes = "")
	public RestResult identificationImageUrlRequest(@RequestBody Map<String,String> map) {    
    	System.out.println("i am identificationImageUrlRequest from controller.........");
    	if(BhObjectUtils.isEmpty(map))
    		return ResultUtils.error(1, "map集合不含元素，请重新检查");
    	String super_dir_path = map.get("super_dir_path");
    	//判断目标地址是否存在
    	File file = new File(super_dir_path);
    	if(!file.isDirectory() || !file.exists()) 
    		return ResultUtils.error(1, "非文件夹or目标地址不存在，请重新检查");
 //   	List<ImgResSummaryVO> resList = new ArrayList<>();
    	TreeSet<ImgResSummaryVO> voSet = new TreeSet<>();
    	try {
    		voSet = tokenService.identificationImageUrlRequest(file);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("识别服务器识别过程，出现异常==========");
			return ResultUtils.error(1, "请求失败");
		}
		return ResultUtils.success("恭喜发财，鼠年吉祥");
	}
   
   
  

/**
    * 将图片发送给识别服务器进行判断
    * @param postData 送审图片的url地址
    * @return
    */
    @RequestMapping(value="/identificationImageUrlRequest_bak",method=RequestMethod.POST)
    @ApiOperation(value = "送审图片接口", notes = "")
	public RestResult identificationImageUrlRequest_bak(@RequestBody Map<String,String> map) {    
    	System.out.println("i am identificationImageUrlRequest");
    	String guid = "34020000001320000999";
    	String model = "smoke";
    //	String model = "phone";
    //	String model = "uniform";
    	String width = "1920";
    	String height = "1080";
    	String image_url = System.getProperty("user.dir") + "\\image\\smoke\\1.jpg";
    	System.out.println("image_url======"+image_url);
    	 String base64Img = "";
    	//
    	try {
			BufferedImage bufferedImage = ImageIO.read(new File(image_url));
		 	// base64图片
		    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        ImageIO.write(bufferedImage, "jpeg", outputStream);
	        base64Img = BhBase64Util.getenBASE64inCodec(outputStream.toByteArray());			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	JSONObject params = new JSONObject();
		params.put("guid", guid);//暂定为3
		params.put("model", model);
//		params.put("marginDeviceNum", "34020000001320000999");
		params.put("width", width);
		params.put("height", height);
		params.put("imagedata", base64Img);
    	String http_post_addr = "http://" + "192.168.1.39:9090" + "/buildingsite/base64/pre";
        String postForObject = TokenController.restTemplate.postForObject(http_post_addr, params, String.class);  
        System.out.println("base64Img======="+base64Img);
    	System.out.println("postForObject========="+postForObject);
		return ResultUtils.error(1, "请求成功");
	}
}//end

