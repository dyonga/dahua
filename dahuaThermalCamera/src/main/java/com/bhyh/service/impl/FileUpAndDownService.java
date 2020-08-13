package com.bhyh.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.bhyh.properties.MessageProperties;
import com.bhyh.service.IFileUpAndDownService;
import net.coobird.thumbnailator.Thumbnails;
//https://www.jianshu.com/p/00f57fd2654f
//https://blog.csdn.net/wanping321/article/details/83246818
//https://blog.csdn.net/null111666/article/details/88397734
/**
 * 处理图片上传下载实现类
 * @author david_lin
 *
 */
@Service
//@Transactional
public class FileUpAndDownService implements IFileUpAndDownService{
    @Autowired
	private MessageProperties messageConfig;
//    private final static String SAVE_PATH = System.getProperty("user.dir") +"\\src\\main\\resources\\static";
    private final static String SAVE_PATH = "D:\\dyonga\\java_codes\\margin_token";
    @Value("${video.video_type}")
    private String video_type;
    
    @Value("${video.video_size}")
    private int video_size;
    /**
     * 接收前端上传的图片，本地化存储
     * @param file 上传的图片文件
     * @return 
     */
	@Override
	public Map<String, Object> uploadPicture(MultipartFile file) {
	    try {
	    	Map<String ,Object> resMap = new HashMap<String ,Object>();
	    	String[] IMAGE_TYPE = messageConfig.getImageType().split(",");
	    	String physical_path = null;
	    	boolean flag = false;
	    	//判断图片类型是否在范围内
	    	for(String type : IMAGE_TYPE) {
	    		if(StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
	    			flag = true;
	    			break;
	    		}
	    	}
	    	if(flag) {//图片在范围内
	    		resMap.put("result", "返回成功");
	    		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
	    		//获取文件类型
	    		String fileType = file.getContentType();
	    		//获取文件后缀名称 jsp|png|jpeg
	    		String imageType = fileType.substring(fileType.indexOf("/")+1);
	    		//原名称
	    		String oldFileName = file.getOriginalFilename();
	    		//新名称
	    		String newFileName = uuid + "."+imageType;
	    		//年月日文件夹
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    		String basedir = sdf.format(new Date());
	    		
	    		if(file.getSize() > messageConfig.getFilesize()) {//图片大小大于阈值
	    			//进行压缩
	    			String newUUID = UUID.randomUUID().toString().replaceAll("-", "");
	    			newFileName = newUUID + "." + imageType;    			
	    			physical_path = SAVE_PATH + "\\" + basedir + "\\" + newUUID + "." + imageType;
	    			//如果目录不存在则创建
	    			File origin_file = new File(physical_path);
	    			if(!origin_file.exists()) 
	    				origin_file.mkdirs();
	    			//保存图片，流操作
	    			file.transferTo(origin_file);
	    			//压缩图片
	    			Thumbnails.of(origin_file).scale(messageConfig.getScaleRatio()).toFile(physical_path);
	    			//显示路径
	    			resMap.put("http_visit_path", "/" + basedir + "/" + newUUID + "." + imageType);
	    			resMap.put("physical_path", physical_path);
	    		 }else {//图片大小在范围内
	    			 System.out.println("SAVE_PATH>>>>>"+SAVE_PATH);
	    			 physical_path = SAVE_PATH + "\\" + basedir + "\\" + uuid + "." + imageType;	    			 
	    			//如果目录不存在则创建
		    	     File upload_file = new File(physical_path);
		    		 if(!upload_file.exists()) 
		    			 upload_file.mkdirs();
		    		 file.transferTo(upload_file);
		    		//显示路径
		    		 System.out.println("physical_path==="+physical_path);
		    		 System.out.println("http_visit_path==="+"/" + basedir + "/" + uuid + "." + imageType);
		    		 resMap.put("http_visit_path", "/" + basedir + "/" + uuid + "." + imageType);
		    		 resMap.put("physical_path", physical_path);
	    		 }
	    		 resMap.put("oldFileName", oldFileName);
	    		 resMap.put("newFileName", newFileName);
	    		 resMap.put("fileSize", file.getSize());
	    	}else {//图片格式不在范围内
	    		resMap.put("result", "图片格式不正确，只支持png|jpg|jpeg");
	    	}
	    	return resMap;
	    }catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
     * 接收前端上传的视频对象，本地化存储
     * @param file 上传的图片文件
     * @return 
     */
	@Override
	public Map<String, Object> uploadVideo(MultipartFile file) {
		try {
	    	Map<String ,Object> resMap = new HashMap<String ,Object>();
	    	String[] VIDEO_TYPE = video_type.split(",");
	    	String physical_path = null;
	    	boolean flag = false;
	    	//判断视频类型是否在范围内
	    	for(String type : VIDEO_TYPE) {
	    		if(StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
	    			flag = true;
	    			break;
	    		}
	    	}
	    	if(flag) {//视频样式在范围内		
	    		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
	    		//获取文件类型
	    		String fileType = file.getContentType();
	    		//获取文件后缀名称 mp4,m3u8,ts,avi,mkv,flv,wmv,mov,rmvb
	    		String videoType = fileType.substring(fileType.indexOf("/")+1);
	    		//原名称
	    		String oldFileName = file.getOriginalFilename();
	    		//新名称
	    		String newFileName = uuid + "."+videoType;
	    		//年月日文件夹
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    		String basedir = sdf.format(new Date());
	    		
	    		if(file.getSize() > video_size) {//视频大小大于阈值
	    			resMap.put("result", "上传视频超过规定大小，请重新选择");
	    		 }else {//视频大小在范围内
	    			 System.out.println("SAVE_PATH>>>>>"+SAVE_PATH);
	    			 physical_path = SAVE_PATH + "\\" + basedir + "\\" + uuid + "." + videoType;	    			 
	    			//如果目录不存在则创建
		    	     File upload_file = new File(physical_path);
		    		 if(!upload_file.exists()) 
		    			 upload_file.mkdirs();
		    		 //保存动作
		    		 file.transferTo(upload_file);
		    		//显示路径
		    		 System.out.println("physical_path==="+physical_path);
		    		 System.out.println("http_visit_path==="+"/" + basedir + "/" + uuid + "." + videoType);
		    		 resMap.put("http_visit_path", "/" + basedir + "/" + uuid + "." + videoType);
		    		 resMap.put("physical_path", physical_path);
		    		 resMap.put("oldFileName", oldFileName);
		    		 resMap.put("newFileName", newFileName);
		    		 resMap.put("fileSize", file.getSize());	
		    		 resMap.put("result", "返回成功");
	    		 }
	    	}else {//视频格式不在范围内
	    		resMap.put("result", "视频格式不正确，只支持mp4,m3u8,ts,avi,mkv,flv,wmv,mov,rmvb");
	    	}
	    	return resMap;
	    }catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> uploadSamples4PublishSys(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}
}//









