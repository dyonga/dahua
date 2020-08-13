package com.bhyh;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.bhyh.config.SpringConfig;
import com.bhyh.dao.mapper.ImageDataVOMapper;
import com.bhyh.dao.mapper.VideoDataVOMapper;
import com.bhyh.model.vo.ImageDataVO;
import com.bhyh.utils.BhDateUtils;
import com.bhyh.utils.BhJsonUtils;
import com.bhyh.utils.sdk.BhKingeyeSdk;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@RunWith(SpringRunner.class)
@SpringBootTest(classes =  TokenDemoApplication.class/* SpringConfig.class */ )
class TokenDemoApplicationTests {
	
//	    @Autowired
//	    private WebApplicationContext context;
//	    @Autowired
//		private ImageDataVOMapper imageDataVOMapper;
//	    @Autowired
//	    private VideoDataVOMapper videoDataVOMapper;
	    @Autowired
		private SqlSessionTemplate sqlSessionTemplate;
	    @Test
	    public void testMe() {
	    	System.out.println("hello,world");
	    }
	    
	    @Test
		public void contextLoads() {
			System.out.println("junit test , hello world");
//			ImageDataVO vo = new ImageDataVO();
//		    vo.setImageUrl("hello,world@bhyh.com");
//		    vo.setCreateTime(BhDateUtils.getGlobalCurrentDate());
			System.out.println("byebye");
		//	sqlSessionTemplate.selectOne("select count (*) from bh_margin_token_image");
			ImageDataVO vo = sqlSessionTemplate.selectOne("com.bhyh.dao.mapper.ImageDataVOMapper.selectByPrimaryKey",1);
			
	 //       Object selectOne = sqlSessionTemplate.selectOne("select * from bh_margin_token_image");
		  
		
			System.out.println("vo==="+vo.getImageUrl());
	    } 
	    @Test    
		public void test101() {
			Date date = BhDateUtils.getGlobalCurrentDate();
			System.out.println(date.getHours());
		}    
	    
	@Test    
	public void test100() {
		String fileName = "D:/dyonga/bh/JAVA/img_res_summary/file2check.txt"; 
		File file = new File(fileName);
		if(!file.exists()) {
			System.out.println("no no no exist");	
			try {
				FileUtils.touch(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	    
	    
	@Test
	void hello() {
//		String a = "{\"code\":100,\"data\":{\"grdbl\":100.0,\"bxl\":646,\"fwl\":0,\"mytsl\":0}}";
//		String str = "{\"code\":100,\"data\":{\"grdbl\":100.0,\"bxl\":646,\"fwl\":0,\"mytsl\":0,\"body\":{\"image_result\":\"spy\",\"sex\":\"girl\"},{\"name\":\"gk\",\"sex\":\"boy\"}}}";
		String str2 = "{"
				+ "\"code\":100,"
				+ "\"data\":"
				+ "{\"grdbl\":100.0,\"bxl\":646,\"fwl\":0,\"mytsl\":0,"
				+ "\"body\":{\"image_result\":[{\"suggest\":\"normal\",\"sex\":\"girl\"},{\"suggest\":\"gk\",\"sex\":\"boy\"},{\"suggest\":\"gk\",\"sex\":\"boy\"}],\"sex\":\"girl\"}"
				+ "}"
				+ "}";
		JSONObject fromObject = JSONObject.fromObject(str2);
		JSONArray filterByPropertyNmAndSug = BhJsonUtils.filterByPropertyNmAndSug(fromObject);
		System.out.println("filterByPropertyNmAndSug===="+filterByPropertyNmAndSug);
		System.out.println("test haha");
//		String str = "{\"a\":\"b\"}";	
	}
	 @Test
	   	public void heiehi() {
	   		System.out.println("junit test , hello world");
	   	}
	 
     String access_key = "AKLTTh-j56W0TyuYyvcrtj54hQ";
     String secret_key = "OASIKMtolLE74pr34/16MjTNTRSeLvxJBoImIlcvHn5NYEgxIQgAtwhsUmA93zhMRQ==";
     /**
      * 图片送审结果查询测试
      */
     @Test
	 public void imageTest() {
    	 String postData = "{\n    \"guard_id\": \"1577327396146478400\",\n    \"image_url\": \"http://www.ylzzd.com/uploadfile/2017/1026/20171026050340184.jpg\"\n}";
	       try {
	    	   System.out.println(BhKingeyeSdk.request("kir.api.ksyun.com", "POST", "kir", "ClassifyImageGuard", "2019-01-18", access_key, secret_key, postData));
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
     /**
      * 图片送审结果查询测试
      */
     @Test
	 public void videoTest() {
		   String postData = "{\n    \"guard_id\": \"1576588647262478651\",\n    \"video_url\": \"https://mp4.vjshi.com/2019-12-24/aaf8f9a52ed43607f1f122001f2be587.mp4\",\n    \"video_id\": \"1234567890\"\n}";
	       try {
			System.out.println(BhKingeyeSdk.request("kir.api.ksyun.com", "POST", "kir", "AnalyzeVideo", "2018-09-03", access_key, secret_key, postData));
			
			System.out.println(BhKingeyeSdk.request("kir.api.ksyun.com", "POST", "kir", "GetAnalyzeVideoResult", "2018-09-03", access_key, secret_key, postData));
	       } catch (Exception e) {
				e.printStackTrace();
			}
	 }
     
     @Test
     public void test6(){
		File file = new File("f:/job_test/");
	    File[] list = file.listFiles();
	    for (int i = 0; i < list.length; i++) {
			System.out.println(list[i].getPath());
		}
	
     }
     
     
     @Test
     public void test2(){
		Collection<File> listFiles = FileUtils.listFiles(new File("F:/FileTest"),
				FileFilterUtils.suffixFileFilter("txt"), /* null */DirectoryFileFilter.INSTANCE);
		       // FileFilterUtils.suffixFileFilter("txt"), null);
         showFiles(listFiles);
     }
     @Test
     public void test3(){
		Collection<File> listFiles = FileUtils.listFilesAndDirs(new File("F:/FileTest"),
				FileFilterUtils.suffixFileFilter("txt"), DirectoryFileFilter.INSTANCE);
		       // FileFilterUtils.suffixFileFilter("txt"), null);
         showFiles(listFiles);
     }

     private void showFiles(Collection<File> listFiles) {
         if (listFiles==null) {
             return;
         }
         for (File file : listFiles) {
             System.out.println(file.getName());
         }
     }
     
     @Test
     public void test1() throws IOException {
// 		File file = new File("F:/job_test/helloworld.txt");
// 		  try {
// 			FileOutputStream fos = new FileOutputStream(file);
// 			fos.write(100);
// 		} catch (FileNotFoundException e) {
// 			e.printStackTrace();
// 		}
 		  System.out.println("hello,world");
 	}
 	
 	public static void test002() {
 		try {
 			URL url = new URL("file:/f:/job_test/helloworld.txt");
 			if("file".equalsIgnoreCase(url.getProtocol())) {
 				System.out.println("u are file");
 				System.out.println("protocol===="+url.getProtocol());
 			}else {
 				System.out.println("haha, u are not");
 				System.out.println("protocol===="+url.getProtocol());
 			}
 		} catch (MalformedURLException e) {
 			e.printStackTrace();
 		}
 	}
     @Test
     public void test003() throws IOException {
    	 boolean flag = true;
    	 File directory1 = new File("file:/f:/job_test/helloworld/heihei");
    	 File directory2 = new File("f:/job_test/helloworld/a.txt");
    	 File directory3 = new File("f:\\job_test\\helloworld\\heihei");
    	 File directory4 = new File("f:\\job_test\\helloworld\\heihei\\love.excel");	 
    	 System.out.println("yes or no ==="+directory1.isDirectory());
    	 System.out.println("yes or no ==="+directory2.isDirectory());
    	 System.out.println("yes or no ==="+directory3.isDirectory());
    	 System.out.println("yes or no ==="+directory4.isDirectory());
 //   	 System.out.println("directory3.getCanonicalFile().getPath()==="+directory3.getCanonicalFile().getPath());
  //  	 FileUtils.forceMkdir(directory1);
//     	 FileUtils.forceMkdir(directory3);
   // 	 flag = directory4.mkdir();
    	 flag = directory4.mkdir();//父目录必须存在
    	 System.out.println("flag======"+flag);
     }
     @Test
     public void test004() throws IOException {
    	 File file = new File("G:\\Family");
   // 	 FileUtils.touch(file);
    	 Collection<File> listFilesAndDirs = FileUtils.listFilesAndDirs(file, DirectoryFileFilter.DIRECTORY, DirectoryFileFilter.INSTANCE);//DirectoryFileFilter.INSTANCE表示递归，若为null则不递归
    	 for (File file2 : listFilesAndDirs) {
			System.out.println(file2.getPath());
		}
    	System.out.println("success=============");
     }
}
