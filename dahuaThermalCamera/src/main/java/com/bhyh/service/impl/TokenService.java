package com.bhyh.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bhyh.controller.TokenController;
import com.bhyh.dao.mapper.ImageDataVOMapper;
import com.bhyh.dao.mapper.ImgResSummaryVOMapper;
import com.bhyh.dao.mapper.VideoDataVOMapper;
import com.bhyh.model.vo.ImageDataVO;
import com.bhyh.model.vo.ImgResSummaryVO;
import com.bhyh.model.vo.VideoDataVO;
import com.bhyh.service.ITokenService;
import com.bhyh.utils.BhBase64Util;
import com.bhyh.utils.BhDateUtils;
import com.bhyh.utils.BhStringUtils;
import com.bhyh.utils.sdk.BhKingeyeSdk;

//import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
/**
 * 这是一个实现类
 * @author dyonga
 *
 */
@Service
@Transactional
//@Slf4j
public class TokenService implements ITokenService{
    @Autowired
	private ImageDataVOMapper imageDataVOMapper;
    @Autowired
    private ImgResSummaryVOMapper imgResSummaryVOMapper;
    @Autowired
    private VideoDataVOMapper videoDataVOMapper;
    @Value("${img_res_summary.file2check}")
    private String FILE2CHECK;
    /**
     * kye: 识别类型；value： 设备号
     */
    private static final HashMap<String, String> TYPE_MAP = new HashMap<String, String>();
  //  private static final String TYPE = "hardhat smoke uniform ladder phone car_lane fire walker";
    {
    	TYPE_MAP.put("hardhat", "34020000001320000991");
    	TYPE_MAP.put("smoke", "34020000001320000992");
    	TYPE_MAP.put("uniform", "34020000001320000993");
    	TYPE_MAP.put("phone", "34020000001320000994");
    	TYPE_MAP.put("car_lane", "34020000001320000995");
    	TYPE_MAP.put("fire", "34020000001320000996");
    	TYPE_MAP.put("walker", "34020000001320000997");
    }
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
//    private String super_dir_path = null;
	@Override
	public String tokenRequest(String host, String method, String service, String action, String version,
			String access_key, String secret_key, String postData) throws Exception {
		return BhKingeyeSdk.request(host, method, service, action, version, access_key, secret_key, postData);
	}

	@Override
	public void insertImageDataVO(ImageDataVO vo) {
		System.out.println("保存 imagedatavo");
		if(vo != null) {
		   imageDataVOMapper.insert(vo);
		}else {
			throw new RuntimeException("ImageDataVO 不能为null");
		}
	}

	@Override
	public void insertVideoDataVO(VideoDataVO vo) {
		System.out.println("保存 videodatavo");
		if(vo != null) {
		   videoDataVOMapper.insert(vo);
		}else {
			throw new RuntimeException("videoDataVO 不能为null");
		}
	}
    /**
     * @throws IOException 
     * 
     */
	@Override
	public /*List<ImgResSummaryVO>*/TreeSet<ImgResSummaryVO> identificationImageUrlRequest(final File topFile) throws IOException{ //topFile顶层文件夹路径 即phone/smoke/uniform这些文件夹的父文件夹		
		System.out.println("i am identificationImageUrlRequest from service...........");		
//		List<ImgResSummaryVO> voList = new ArrayList<ImgResSummaryVO>();
		TreeSet<ImgResSummaryVO> voSet = new TreeSet<>();
		//遍历子文件夹，过滤出hardhat, smoke, uniform, ladder, phone, car_lane, fire, walker命名的文件夹
   // 	Iterator<File> secondLevelDirs = listSonDirs(topFile);
    	List<File> secondLevelDirs = listSonDirs(topFile);
    	System.out.println("secondLevelDirs.toString()====="+secondLevelDirs.toString());
 //   	System.out.println("secondLevelDirs.hasNext()==="+secondLevelDirs.hasNext());
 //   	while(secondLevelDirs.hasNext()) {//遍历第二层文件夹（hardhat, smoke, uniform这层）  
    	for(File secondLevelDir: secondLevelDirs) {	
    	//	File secondLevelDir = secondLevelDirs.next();		
    		//递归获取第三层文件夹所有后代文件夹(顾虑掉文件，只保留文件夹)  ====注意第三层文件夹内的文件不会被检测到，请勿把jpg文件放在此处=====
    		Collection<File> thirdLevelAndMoreDirs = FileUtils.listFilesAndDirs(secondLevelDir, DirectoryFileFilter.DIRECTORY, DirectoryFileFilter.INSTANCE);//DirectoryFileFilter.INSTANCE表示递归，若为null则不递归
            thirdLevelAndMoreDirs.remove(secondLevelDir);
    		//用Iterator包装第三层及往后的所有文件夹
    		Iterator<File> it = thirdLevelAndMoreDirs.iterator();
    		//遍历每个文件夹，检查每个文件夹内部是否有.jpg文件        
        	while(it.hasNext()) {
        //	while(it!=null) {	
        		 File thirdLevelAndMoreDir = it.next();//每一个文件夹
        		//判断文件夹子目录下（此处不递归）是否含jpg,并返回所有jpg文件路径
            	 File[] imgFiles = thirdLevelAndMoreDir.listFiles(new FileFilter(){
                     @Override
                     public boolean accept(File pathname) {
                         // 判断文件名是否是.jpg 结尾
                         if (pathname.getName().toLowerCase().endsWith(".jpg")) {
                             return true;
                         }
                         return false;
                     }}
            	 ); 
            	 //判断File[]是否为空
            	 if(imgFiles.length == 0 || imgFiles == null)
            		 continue;
            	 //将File[] 转成Map<图片编号，图片路径>，图片编号为8为随机整数
            	 Map<String, File> map = transfer2Map(imgFiles);
            	 
            	 //获取识别图片类型 
//            	 String temp_path = secondLevelDir.getPath();
//            	 String model = secondLevelDir.getPath().substring(temp_path.lastIndexOf(File.separator) + 1, temp_path.length());
            	 String model = secondLevelDir.getName();
//            	 String model = getModelFromFileNm(thirdLevelAndMoreDir);//获取对应识别类型
           	     String guid = TYPE_MAP.get(model);//获取对应设备号
            	 
            	 //获取文件对应的层数
            	 int level = getLevelNum(thirdLevelAndMoreDir, secondLevelDir);            	
            	 //送审识别服务器，以相同文件夹下jpg为单位，批量送审
            	 long start = BhDateUtils.getGlobalCurrentDate().getTime();            	 
            	 String[] results = send2Check(map, guid, model);           	 
            	 long end = BhDateUtils.getGlobalCurrentDate().getTime();
            	 //创建两个子文件夹checked 和 unchecked;并把检测到的放入checked文件夹，未检测到的放入unchecked文件夹
            	 long costTime = end - start;//识别一个文件夹，花费的时间
            	 ImgResSummaryVO vo = createDirsAndMoveIn(thirdLevelAndMoreDir, map,results, level, costTime, model);
//            	 voList.add(vo);
            	 voSet.add(vo);
    		}    
        	 //将voSet按顺序写入文件，让用户读取文件结果
        	 printRes2File(voSet, FILE2CHECK);
    	}
//		return voList;
		return voSet;
	}
	/**
	 * 将voSet按顺序写入文件，让用户读取文件结果
	 * @param voSet
	 * @param fILE2CHECK2
	 * @throws IOException 
	 */
	private void printRes2File(TreeSet<ImgResSummaryVO> voSet, final String path) throws IOException {
		if(!StringUtils.hasText(path))
			throw new RuntimeException("文件路径不允许为空，请重新检查");
		File file = new File(path);
		if(!file.exists()) {
			FileUtils.touch(file);//新建文件，若存在则update 修改时间
		}
		FileWriter out = new FileWriter(file);		
		try {
			
			Iterator<ImgResSummaryVO> it = voSet.iterator();
			while(it.hasNext()) {
				ImgResSummaryVO vo = it.next();
				String str2Write = "图片类别: "+vo.getType() + "花费时间: "+ vo.getCostTime() + "ms" + ";  图片路径: "+vo.getPath() + ";  识别到图片数量: " + vo.getChecked() +";   未识别到图片数量：" +vo.getUnchecked() + System.lineSeparator();
				out.write(str2Write, 0, str2Write.length());
			}
		}finally {
			if(out != null)
				out.close();			
		}
	}

	/**
	 * 获取文件路径对应层数，(hardhat, smoke, uniform为第二层，对应数字1;最顶层对应数字0)
	 * @param thirdLevelAndMoreDir
	 * @param secondLevelDir
	 * @return
	 */
	private int getLevelNum(File thirdLevelAndMoreDir, File secondLevelDir) {
		int count = 0;
		int levelNum = 1;
		while((thirdLevelAndMoreDir = thirdLevelAndMoreDir.getParentFile()) != secondLevelDir) {
			System.out.println(thirdLevelAndMoreDir.getPath());
			System.out.println(secondLevelDir.getPath());
			System.out.println("yes or not ====="+thirdLevelAndMoreDir.getPath().equals(secondLevelDir.getPath()));
			count++;
			if(thirdLevelAndMoreDir.getPath().equals(secondLevelDir.getPath()))
				break;
			
		}
		return levelNum + count;
	}

	/**
	 * 将File[] 转成Map<图片编号，图片路径>
	 * 图片编号唯一，以8位uuid代表
	 * @param eachFileList
	 * @return
	 */
	private Map<String, File> transfer2Map(File[] files) {
		if(files == null || files.length == 0)
			throw new RuntimeException("File[] 为空，请重新检查");
		Map<String, File> map = new HashMap<>();
		for (File file : files) {
			String ranNum = BhStringUtils.getRandomIntegerByLength(8);
			map.put(ranNum, file);
		}
		return map;
	}

	/**
	 * 创建两个子文件夹checked 和 unchecked;并把检测到的放入checked文件夹，未检测到的放入unchecked文件夹
	 * @param dir
	 * @param map
	 * @param results
	 * @param level
	 * @param costTime
	 * @param type
	 * @return
	 * @throws IOException 
	 */
//	@SuppressWarnings("null")
	private ImgResSummaryVO createDirsAndMoveIn(File dir,Map<String, File> map, String[] results, int level, long costTime, String type) throws IOException {
		File checkedFile = new File(dir.getPath()+ File.separator + "checked");
		checkedFile.mkdir();
		File uncheckedFile = new File(dir.getPath()+ File.separator + "unchecked");
		uncheckedFile.mkdir();
		ImgResSummaryVO vo = new ImgResSummaryVO();
		int checkedNum = 0;
		//匹配遍历，将检测到和未检测到分开存放
		for (String res : results) {
			JSONObject resJsonObj = JSONObject.fromObject(res);
			String guid = null;
			Object infoObj = resJsonObj.get("info");
			System.out.println("length====="+infoObj.toString().length());
			int guidInt = (int) resJsonObj.get("guid");	
			guid = String.valueOf(guidInt);
//			File fileFromMap = map.get(resJsonObj.get("guid"));
//			File fileFromMap3 = map.get("15296407");
//			System.out.println(fileFromMap);
//			System.out.println(fileFromMap3);
			File fileFromMap = map.get(guid);
			if(infoObj != null &&  infoObj.toString().length() > 4) {//证明检测到结果			
				FileUtils.moveFileToDirectory(map.get(guid), checkedFile, false);
					checkedNum++;				
			}else {//未检测到结果				
				FileUtils.moveFileToDirectory(map.get(guid), uncheckedFile, false);				
			}
		}
		//保存识别结果信息
		vo.setPath(dir.getPath());
		String localFileName = dir.getName();
		String parentFileName = dir.getParentFile().getName();		
		vo.setParentFileName(parentFileName);
		vo.setChecked(checkedNum);
		vo.setUnchecked(results.length - checkedNum);
		vo.setTotal(results.length);
		vo.setLevel(level);
		vo.setType(type);
		vo.setGroupId(type+ "_" + level + "_" + localFileName);
		vo.setParentGroupId(type+ "_" + level + "_" + parentFileName);	
		vo.setCostTime(costTime);
		
		Date globalDate = BhDateUtils.getGlobalCurrentDate();
		System.out.println(globalDate.getHours());
		System.out.println(globalDate.getMinutes());
	//	System.out.println();
		
		
		vo.setCreateTime(globalDate);
		//存入db		
		imgResSummaryVOMapper.insert(vo);		       		
		return vo;
	}

	/**
	 * 通过文件夹名判断，判断识别类型
	 * @param eachFile
	 * @return
	 */
//	private String getModelFromFileNm(File eachFile) {
//		for(String key:TYPE_MAP.keySet()) {
//			if(eachFile.getPath().indexOf(key) != -1) {
//				return key;
//			}				
//		}
//		return null;
//	}
	
	/**
	 * 文件夹为单位，批量发送给识别服务器
	 * @param eachFileList
	 * @param
	 * @param
	 * @return
	 */
	private String[] send2Check(Map<String, File> map, String guid, String model) {
		if(map == null || map.size() == 0)
			throw new RuntimeException("map 不允许为空或只含空");
		String[] resultsFromCheckServer = new String[map.size()];	
		int index = 0;
		for (String key : map.keySet()) {
	    	String base64Img = "";	
	    	try {
				BufferedImage bufferedImage = ImageIO.read(map.get(key));
			 	// base64图片
			    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        ImageIO.write(bufferedImage, "jpeg", outputStream);
		        base64Img = BhBase64Util.getenBASE64inCodec(outputStream.toByteArray());			
			} catch (IOException e1) {
				e1.printStackTrace();
			}	    	
	    	JSONObject params = new JSONObject();
	    	//=====由于识别服务器接口目前没有设置pkid，返回时无法判断对应图片，我这里先把guid替换成图片id去发送，以便对应返回结果
		//	params.put("guid", guid);//暂定为3
			params.put("guid", key);//key就是图片id
			params.put("model", model);
//			params.put("marginDeviceNum", "34020000001320000999");
			params.put("width", WIDTH);
			params.put("height", HEIGHT);
			params.put("imagedata", base64Img);
	    	String http_post_addr = "http://" + "192.168.1.39:9090" + "/buildingsite/base64/pre";
	        String resStrFromServer = TokenController.restTemplate.postForObject(http_post_addr, params, String.class);
	        resultsFromCheckServer[index] = resStrFromServer;
	        index++;
		}
		return resultsFromCheckServer;
	}

	/**
	 * 递归遍历获取所有含.jpg文件的文件夹的名字
	 * @param next
	 * @return
	 */
    @SuppressWarnings("unused")
	private Iterator<File> listAllDirs(File next) {
    	
		//先获取所有文件夹
    	Collection<File> listFilesAndDirs = FileUtils.listFilesAndDirs(next, DirectoryFileFilter.DIRECTORY, DirectoryFileFilter.INSTANCE);//DirectoryFileFilter.INSTANCE表示递归，若为null则不递归
        Iterator<File> it = listFilesAndDirs.iterator();
    	while(it.hasNext()) {
//        for (File file : listFilesAndDirs) {
    		 File file = it.next();
        	 if(file.listFiles(new FileFilter(){
                 @Override
                 public boolean accept(File pathname) {
                     // 判断文件名是否是.jpg 结尾
                     if (pathname.getName().toUpperCase().endsWith(".jpg")) {
                         return true;
                     }
                     return false;
                 }}).length <= 0) {//证明有文件夹内含jpg文件     		 
        	 }
        	 it.remove();
		}    
		return it;
	}

	/**
     * 过滤当前路径下，指定名称的文件夹，挑选出来，放入数组
     * @param file 上层文件夹路径
     * @return
     */
	private /*Iterator<File>*/List<File> listSonDirs(File file) {
		if(!file.isDirectory())
			throw new RuntimeException("文件夹不存在，请重新检查");
		File[] listFiles = file.listFiles();
		//
		if(listFiles == null || listFiles.length == 0) {
			throw new RuntimeException("文件夹内不允许为空，请重新检查");
		}
		List<File> list = Arrays.asList(listFiles);
		//Arrays.asList() 返回java.util.Arrays$ArrayList， 而不是ArrayList,以下转换下
		List<File> arrList = new ArrayList<File>(list);
		System.out.println("转换ava.util.Arrays$ArrayList， 到ArrayList====");
		ListIterator<File> listIterator = arrList.listIterator();		
        while(listIterator.hasNext()) {
        	File next = listIterator.next();
        	//第一层过滤，过滤掉文件	
        	if(!next.isDirectory()) { 
        		listIterator.remove();
        	}
        	//第二层过滤，过滤掉不属于识别范围的文件夹
            //hardhat, smoke, uniform, ladder, phone, car_lane, fire, walker
        	else if(!TYPE_MAP.containsKey(next.getName())){
            	listIterator.remove();
            }       	
        }		        
	//	return listIterator;
	    return arrList;	
	}
}//end









