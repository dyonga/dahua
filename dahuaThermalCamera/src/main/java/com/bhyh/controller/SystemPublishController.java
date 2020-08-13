package com.bhyh.controller;


import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.bhyh.common.core.controller.CommonController;
import com.bhyh.result.Pager;
import com.bhyh.result.RestResult;
import com.bhyh.result.ResultUtils;
import com.bhyh.service.IFileUpAndDownService;
import com.bhyh.service.ISystemPublishService;
import com.bhyh.utils.BhStringUtils;

/**
   * 发布系统的controller层接口调用类
 * @author dyonga
 *
 */
@RestController
@RequestMapping("/SystemPublishController")
public class SystemPublishController extends CommonController{
    @Autowired
	ISystemPublishService systemPublishService;
    @Autowired
    IFileUpAndDownService fileUpAndDownService;
    /**
	  * 上传素材接口，一次仅能上传一份
	  * group可选，应该是根据上传文件后缀名进行分组
	 * @param file 必须带
	 * @param group 组别(必选，前端通过调用我的接口（）获取固定设置让用户选择；目前先手动输入)
	 * @param company_id （必填，前端可通过session信息保存，公司编号，建议默认设置为1）
	 * url===>ZHSD/upload/upload.php
	 */
	@RequestMapping(value="/uploadSamples")
	public RestResult uploadSamples(@RequestParam(value = "company_id", required = true) int company_id,
			/* @RequestParam(value = "group", required = true) */String group, @RequestParam(value = "file", required = true) MultipartFile file) {    
		try {	       
		    Object result = systemPublishService.uploadSamples(company_id, group, file);		
	        return ResultUtils.success("素材上传成功");	   
	        
		}catch(Exception e) {
			log.info("素材上传失败", e); 
			return ResultUtils.error(1, e.getMessage());
		}		
 	}
	/**
	   *  页面初始化时显示的内容，默认全部素材全部显示获取素材url===>ZHSD/group/get_group_source.php
	 * @param company_id 公司编号
	 * @param user_name 是否为超级用户(SuperAdministrator，默认为"")
	 * @param is_total_file (默认是 1)是否显示分组下所有资源文件)-(0->否，1->是，默认为0)
	 * @param get_group_tree (默认是 1，获取所有)是否获取所有分组
	 * @param include_folder (直接默认false 不包含，后续若需要再修改)true:包含文件夹，false:不包含文件夹
	 * @param sort (直接默认一个值 9，按节目名称升序排序)1，按更新时间升序排序，2，按更新时间降序排序，3，按节目名称升序排序，4，按节目名称将序排序,5，按公司编号升序排序，6按公司编号降序排序，7，按节目大小升序排序，8，按节目大小降序排序，9，按上传时间升序排序，10，按上传时间降序排序，(默认按照上传时间升序排序)
	 * @param method (直接默认total，因为需要分页)total(获取资源总数)
	 * @param pager （可选）分页对象默认从第0页开始，每页显示15条
	 */
	@RequestMapping(value="/getAllSamples")
	public Object getAllSamples(@RequestParam(value = "company_id", required = true) int company_id, String user_name, Pager pager) {		 
		try{
			int is_total_file = 1;//显示分组下所有资源文件
			int get_group_tree = 1;//获取所有分组
			boolean include_folder = false;//false:不包含文件夹
			int sort = 9;//9，按上传时间升序排序
			String method = "total";
			Object result = systemPublishService.getAllSamples(company_id, user_name, is_total_file, get_group_tree, include_folder, sort, method, pager);			
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("获取素材失败", e); 
			return ResultUtils.error(1, e.getMessage());
		}
		
	}
	@RequestMapping(value="/getSamplesByGroupAndSourceType")
	public Object getSamplesByGroupAndSourceType(@RequestParam(value = "sourceType", required = true)  String sourceType,
			@RequestParam(value = "company_id", required = true) int company_id,
			String user_name, @RequestParam(value = "group", required = true) String group, String findValue, Pager pager) {		 
		try {
			System.out.println("=======================");
			int is_total_file = 0;//否,不显示分组下所有资源文件
			int get_group_tree = 0;//否,获取所有分组
			boolean include_folder = true;//不包含文件夹 
			int sort = 9;//按上传时间
			String findType = "name";
			Object result = systemPublishService.getSamplesByGroupAndSourceType(sourceType, company_id, user_name, group, is_total_file, get_group_tree, include_folder, sort, findType, findValue, pager);
			return ResultUtils.success(result);		
		}catch(Exception e) {
			log.info("通过文件名获取素材失败", e); 
			return ResultUtils.error(1, e.getMessage());
		}
	}
	/**
	 * 通过文件名获取指定素材   url===>ZHSD/group/get_group_source.php
	 * @param sourceType 素材类型: video(视频) music(音频) picture(图片)  字符串： $table = array($sourceType);
	 * @param company_id 公司编号
	 * @param user_name 是否为超级用户(SuperAdministrator，默认为"")
	 * @param group group 组别(这里可选，因为已经有文件名和类型如果需要还是一样，前端通过调用我的接口（）获取固定设置让用户选择；目前先手动输入)
	 * @param is_total_file 直接默认否，不显示分组下所有资源文件，因为是搜索指定文件，这个选项没有在此
	 * @param get_group_tree 直接默认，获取所有分组 （不同分组可能有同名文件）
	 * @param include_folder (直接默认不包含，后续若需要再修改)
	 * @param findType 直接默认(name:按文件名查询)
	 * @param findValue (这里必选)查询的内容
	 * @param sort (直接默认9)1，按更新时间升序排序，2，按更新时间降序排序，3，按节目名称升序排序，4，按节目名称将序排序,5，按公司编号升序排序，6按公司编号降序排序，7，按节目大小升序排序，8，按节目大小降序排序，9，按上传时间升序排序，10，按上传时间降序排序，(默认按照上传时间升序排序)
	 */
	@RequestMapping(value="/getSamplesByFileName")
	public Object getSamplesByFileName( @RequestParam(value = "sourceType", required = true)  String sourceType,
			@RequestParam(value = "company_id", required = true) int company_id,
			String user_name, @RequestParam(value = "group", required = true) String group/* , String findType */,
			/* @RequestParam(value = "findValue", required = true) */ String findValue) {		 
		try {
			System.out.println("=======================");
			int is_total_file = 0;//否
			int get_group_tree = 1;//是
			boolean include_folder = false;//不包含文件夹
			String findType = "name"; 
			int sort = 9;//按上传时间
			Object result = systemPublishService.getSamplesByFileName(sourceType, company_id, user_name, group, is_total_file, get_group_tree, include_folder, findType, findValue, sort);
			return ResultUtils.success(result);		
		}catch(Exception e) {
			log.info("通过文件名获取素材失败", e); 
			return ResultUtils.error(1, e.getMessage());
		}
	}
	/**
	 * 通过文件夹名获取素材url===>ZHSD/group/get_group_source.php
	 * @param sourceType （必填，因为可能出现不同类型底下有相同名字文件夹）素材类型: video(视频) music(音频) picture(图片)  字符串： $table = array($sourceType);
	 * @param company_id 必填 公司编号
	 * @param user_name 是否为超级用户(SuperAdministrator，默认为"")
	 * @param group 组别(这里可选，因为已经有文件夹名和类型如果需要还是一样，前端通过调用我的接口（）获取固定设置让用户选择；目前先手动输入)
	 * @param is_total_file (直接默认显示分组下所有资源文件)
	 * @param get_group_tree (直接默认获取所有分组)
	 * @param include_folder (直接默认包含文件夹)
	 * @param findType (直接默认 groupName:按分组名查询)
	 * @param findValue (必填)查询的内容
	 * @param sort (直接默认9)1，按更新时间升序排序，2，按更新时间降序排序，3，按节目名称升序排序，4，按节目名称将序排序,5，按公司编号升序排序，6按公司编号降序排序，7，按节目大小升序排序，8，按节目大小降序排序，9，按上传时间升序排序，10，按上传时间降序排序，(默认按照上传时间升序排序)
	 * @param pager （可选）分页对象默认从第0页开始，每页显示15条
	 */
	@RequestMapping(value="/getSamplesByDirectoryName")
	public Object getSamplesByDirectoryName(@RequestParam(value = "sourceType", required = true) String sourceType,
			@RequestParam(value = "company_id", required = true) int company_id,
			String user_name, String findValue, Pager pager) {		 
		try{
			int is_total_file = 0;//否
			int get_group_tree = 1;//true获取所有分组
			boolean include_folder = true;//默认不自动显示分组下所有资源文件，但显示文件夹
			String findType = "groupName"; 
			int sort = 9;//按上传时间
			Object result = systemPublishService.getSamplesByDirectoryName(sourceType, company_id, user_name, is_total_file, get_group_tree, include_folder, findType, findValue, sort, pager);
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("通过文件夹名获取素材失败", e); 
			return ResultUtils.error(1, e.getMessage());
		}
	}
	
	
	/**
	 * 根据文件id号，删除素材（一次可以删除多个ids以'，'隔开） url===>ZHSD/delete/delete_batch.php
	 * @param type_num 要删除的资源类型数目 
	 * @param type 要删除的资源类型  $_REQUEST['type'.$i]
	 * @param ids 资源id 字符串数组且数字中每个元素使用“，”隔开  $id = explode(',', $_REQUEST['id'.$i]);
	 */
	@RequestMapping(value="/deleteSamples")
	public Object deleteSamples(@RequestParam(value = "company_id", required = true) int company_id, @RequestParam(value = "type", required = true) String type, @RequestParam(value = "id", required = true) String id) {
		try {
			//此处id有很大疑问，看接口文档，貌似id是每种类型的序号，都是从1开始递增，并不是唯一。因为每一类型都是从1开始
//			List<String> typeList = BhStringUtils.delimitedStringToList(type, ",");	
//			if(typeList.size() != ids.length) {
//				return ResultUtils.error(1, "请求失败:资源类型数目与ids数组大小不一致");
//			}
			Object result = systemPublishService.deleteSamples(company_id, type, id);
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("删除失败", e); 
			return ResultUtils.error(1, "删除失败");
		}	
	}
	/**
	 * 发布素材，支持一次发布多个素材   url===>ZHSD/release_program/release_program.php
	 * @param method 直接默认为post
	 * @param tid 必填 终端设备的id号，例如tid=1,2,3,4  字符串：$tid_id = explode(',', $data['tid']);
	 * @param type 必填  video(视频) music(音频)  picture(图片)..... 字符串： if (!isset($data['type'.$i]) OR $data['type'.$i] == '') {
	 * @param id 必填  资源id以逗号为分隔符，例如：id1=12,13,14     $id = explode(',', $data['id' . $type_num]);
	 * @param links 大图,小图的关联(link=111-123|125-126)
	 * @param publish_type (可选)order(预约发布),check(审核发布)，默认为立即发布
	 * @param status online (可选) (在线),offline(离线),standby(待机),all(所有) 默认所有
	 * @param group (弃用)已经有tid情况下，没有效果。这里的group应该指的是终端分组
	 * @param date (可选)预约发布时间(2018-01-05 12:00:00) 
	 * @param company_id 必填公司编号 
	 * @param immediate (可选)即时播放(0:播放一次，1，按设置时间播放)
	 * @param immediate_time (可选)即时播放时间
	 * @param nickname (可选)发布名称
	 * @param release_record_id (可选)发布列表id编号
	 * @param user_name (这里弃用)是否为超级用户(SuperAdministrator，默认为空)
	 * @param is_cover (可选)是否覆盖(1:覆盖,0:追加,默认为0)
	 * @param playlist (可选)时间编排路径(需要在获取发布前保存的时间编排路径)
	 */
	@RequestMapping(value="/publishSamples")
	public RestResult publishSamples(@RequestParam(value = "tid", required = true) String tid, @RequestParam(value = "type", required = true)  String type, @RequestParam(value = "id", required = true) String id, String link,
			String publish_type, String status/* , String group */, Date date, @RequestParam(value = "company_id", required = true) int company_id, Integer immediate, Date immediate_time, 
			String nickname, Integer release_record_id/* , String user_name */, Integer is_cover/* , String playlist */) {
		try {
	//		List<String> typeList = BhStringUtils.delimitedStringToList(types, ",");
			link = "";
			if(publish_type == null)
				publish_type = "  ";
			if(status == null)
				status = "all";
			if(date == null)
				date = new Date();
			if(immediate == null)
				immediate = 1;
			if(immediate_time == null)
				immediate_time = new Date();
			if(nickname == null)
				nickname = "";
			if(release_record_id == null)
				release_record_id = -1;
			if(is_cover == null)
				is_cover = 0;
			Object result = systemPublishService.publishSamples(tid, type, id, link, publish_type, status/* , group */, date,
					company_id, immediate, immediate_time, nickname, release_record_id/* , user_name */,
					is_cover/* , playlist */);
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("发布失败", e);
			return ResultUtils.error(1,"发布失败");
		}		
	}
	/**
	 * ==============此接口findType和findvalue没有作用，status有用============
	   *  获取所有终端设备 url===>ZHSD/terminal/terminal.php   
	 * @param company_id (公司编号)
	 * @param group 由于选择所有，这里不入参，由于页面初始化时，显示所有终端内容。 
	 * @param status 由于选择所有，这里不入参，直接选择所有
	 * @param sort 选择1，终端编号升序 (0:终端编号降序，1:终端编号升序，2:用户编号降序，3:用户编号升序，4:进度升序,5:进度降序，6:节目个数升序,7:文件个数降序,默认按公司编号升序排序)(可选)
	 * @param user_name (超级用户:SuperAdministrator,默认为空)(可选)
	 * @param findType 由于选择所有，这里不入参 (查找类型，name:终端名称,userGiveName:自定义名称,groupName:组别,programNum:节目个数,)(可选)
	 * @param findValue 由于选择所有，这里不入参 (查找内容)(可选)
	 * @param pager （可选）分页对象默认从第0页开始，每页显示15条
	 * @return
	 */
	@RequestMapping(value="/getAllTerminalDevices")
	public Object getAllTerminalDevices(@RequestParam(value = "company_id", required = true) int company_id/* , String terminalGroup */ , String status ,
			/* int sort, */String user_name/* , String findType, String findValue */, Pager pager) {
		try {
			int sort = 1;
			Object result = systemPublishService.getAllTerminalDevices(company_id, status, sort, user_name, pager);			
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("获取终端设备失败", e);
			return ResultUtils.error(1,"获取终端设备失败");
		}
	}
	/**
	   * 根据分组名/终端名/自定义名称等 获取终端设备  url===>ZHSD/terminal/terminal.php
	 * @param company_id (公司编号)
	 * @param terminalGroup 必填 (组别，默认system)
	 * @param status 可选  (online:在线，offline:离线,standby:待机,buffer:正在缓冲，download:正在下载，reservation：预约发布，check：待审核，默认所有)(可选)
	 * @param sort 可选 (0:终端编号降序，1:终端编号升序，2:用户编号降序，3:用户编号升序，4:进度升序,5:进度降序，6:节目个数升序,7:文件个数降序,默认按公司编号升序排序)(可选)
	 * @param user_name 可选 (超级用户:SuperAdministrator,默认为空)(可选)
	 * @param findType 直接默认(查找类型，name:终端名称,userGiveName:自定义名称,groupName:组别,programNum:节目个数,)(可选)
	 * @param findValue (查找内容)(可选)
	 * @param pager （可选）分页对象默认从第0页开始，每页显示15条 
	 * @return
	 */
	@RequestMapping(value="/getTerminalDevicesByName")
	public Object getTerminalDevicesByName(@RequestParam(value = "company_id", required = true) int company_id, String terminalGroup, String status,
			 int sort, String user_name , @RequestParam(value = "findType", required = true) String findType, @RequestParam(value = "findValue", required = true) String findValue , Pager pager) {
		try {
			Object result = systemPublishService.getTerminalDevicesByName(company_id, terminalGroup, status, sort, user_name, findType, findValue, pager);			
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("获取终端设备失败", e);
			return ResultUtils.error(1,"获取终端设备失败");
		}
	}
	/**
	  * 根据终端id，删除终端设备，一次可以删除多个    url===>ZHSD/terminal/terminal.php
	 * @param id (终端的id号，以逗号隔开，如id=2,3,4,5,6，7)
	 * @param company_id (公司编号)
	 * @param group 意义不大，直接通过行属性中id删除。此处不用
	 * @param status (online:在线，offline:离线,standby:待机，默认所有)(可选)
	 * @param user_name SuperAdministrator,默认为空)(可选)
	 * @return
	 */
	@RequestMapping(value="/delTerminalDevices")
	public RestResult delTerminalDevices(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "company_id", required = true) int company_id, /* String group, */ String status,
			String user_name) {
		try {
			Object result = systemPublishService.delTerminalDevices(id, company_id, /* group, */status, user_name);
			return ResultUtils.success(result);
			}catch(Exception e) {
				log.info("删除终端设备失败", e);
				return ResultUtils.error(1,"删除失败");
			}		
	}
	/**
	 * 更新终端设备     url===>ZHSD/terminal/terminal.php
	 * @param id 终端id编号
	 * @param company_id (公司编号)
	 * @param userGivenName 用户自定义名称
	 * @param newVal 更新后名称
	 * @return
	 */
	@RequestMapping(value="/updateTerminalDevices")
	public RestResult updateTerminalDevices(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "company_id", required = true) int company_id, @RequestParam(value = "userGivenName", required = true) String userGivenName) {
		try {
			Object result = systemPublishService.updateTerminalDevices(id, company_id, userGivenName);			
			return ResultUtils.success(result);		
		}catch(Exception e) {
			log.info("更新终端设备失败", e);
			return ResultUtils.error(1,"更新终端设备失败");
		}		
	}
	/**
	   *   获取所有终端设备状态信息
	 * @param company_id (公司编号)
	 * @param user_name  
	 * @return
	 */
	@RequestMapping(value="/getAllTerminalStatus")
	public RestResult getAllTerminalStatus(@RequestParam(value = "company_id", required = true) int company_id/* , String group, String status */,
			String user_name/* , String findType, String findValue */) {
		try {
			Object result = systemPublishService.getAllTerminalStatus(company_id, user_name);
			return ResultUtils.success(result);			
		}catch(Exception e) {
			log.info("获取所有终端设备状态信息败", e);
			return ResultUtils.error(1,"获取所有终端设备状态信息");
		}		
	}
	/**
	   *   根据组名获取指定终端设备状态信息
	 * @param company_id (公司编号)
	 * @param group (组别，默认system)(可选)
	 * @param user_name  (可选)
	 * @param findType 直接默认groupName:组别
	 * @param findValue 必填 (查找内容)
	 * @return
	 */
	@RequestMapping(value="/getTerminalStatusByName")
	public RestResult getTerminalStatusByName(@RequestParam(value = "company_id", required = true) int company_id, String group,
			String user_name, @RequestParam(value = "findValue", required = true) String findValue) {
		try {
			String findType = "groupName";
			Object result = systemPublishService.getTerminalStatusByName(company_id, group, user_name, findType, findValue);
			return ResultUtils.success(result);			
		}catch(Exception e) {
			log.info("根据组名获取指定终端设备状态信息失败", e);
			return ResultUtils.error(1,"根据组名获取指定终端设备状态信息失败");
		}		
	}
	/**
	   *   获取终端所有组别信息（不含文件内容）
	 * @param company_id (公司编号)
	 * @param user_name (是否为超级用户:SuperAdministrator)(可选) 
	 * @param url 
	 * @param pager （可选）分页对象默认从第0页开始，每页显示15条 
	 * @return
	 */
	public Object getTerminalGroup(@RequestParam(value = "company_id", required = true) int company_id, String user_name, String url, Pager pager) {
		try {
			Object result = systemPublishService.getTerminalGroup(company_id, user_name, url, pager);
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("获取终端所有组别信息失败", e);
			return ResultUtils.error(1,"获取终端所有组别信息失败");
		}
	}
	/**
	   *   新增终端分组     url===>ZHSD/group_terminal/group_terminal.php
	 * @param company_id (公司编号)
	 * @param group_name (分组名)
	 * @param url 所属分组()
	 * @return
	 */
	public Object insertTerminalGroup(@RequestParam(value = "company_id", required = true) int company_id, @RequestParam(value = "group_name", required = true) String group_name, @RequestParam(value = "url", required = true) String url) {
		try {
			Object result = systemPublishService.insertTerminalGroup(company_id, group_name, url);
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("新增终端分组失败", e);
			return ResultUtils.error(1,"新增终端分组失败");
		}
			
	}
	/**
	 * 删除终端组
	 * @param 必填 company_id (公司编号)
	 * @param 必填 group_name (分组名)  
	 * @param 必填 url 所属分组
	 * @param is_empty_group (可选)(true:是清空该分组下所属终端，默认为false)
	 * @param del_curr_group (可选)(true:清空分组，包括删除group_name分组，false:清空分组，但不删除group_name分组，默认为false)
	 * @param is_keep_source (true:删除分组，同时分组下面的终端设置为默认分组,false:删除分组下所有终端，默认为false)(可选)
	 * @return
	 */
	public RestResult delTerminalGroup(@RequestParam(value = "company_id", required = true) int company_id, @RequestParam(value = "group_name", required = true) String group_name, @RequestParam(value = "url", required = true) String url, Boolean is_empty_group, Boolean del_curr_group,
			Boolean is_keep_source) {
		try {
			if(del_curr_group == null)
				del_curr_group = true;//是否删除group_name分组文件夹
			if(is_empty_group == null)
				is_empty_group = false;//删除group_name分组文件夹下所有终端						 
			if(is_keep_source == null)
				is_keep_source = true;//删除分组，但分组下终端设置为默认分组
			Object result = systemPublishService.delTerminalGroup(company_id, group_name, url, is_empty_group, del_curr_group, is_keep_source);		
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("删除终端组失败", e);
			return ResultUtils.error(1,"删除终端组失败");
		}			
	}
	/**
	 * 更新终端设备分组
	 * @param company_id (公司编号)
	 * @param group_name (分组名)
	 * @param url 所属分组
	 * @param value 修改后的组名
	 * @return
	 */
	public RestResult updateTerminalGroup(@RequestParam(value = "company_id", required = true) int company_id, @RequestParam(value = "group_name", required = true) String group_name, @RequestParam(value = "url", required = true) String url, @RequestParam(value = "value", required = true) String value) {
		try {
			Object result = systemPublishService.updateTerminalGroup(company_id, group_name, url, value);
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("更新终端组失败", e);
			return ResultUtils.error(1,"更新终端组失败");
		}		
	}
	/**
	 * 获取终端分组总数
	 * @param company_id (公司编号)
	 * @param user_name (是否为超级用户:SuperAdministrator)(可选)
	 * @param url 所属分组
	 * @return
	 */
	public RestResult getTotalTerminalGroup(@RequestParam(value = "company_id", required = true) int company_id, String user_name, @RequestParam(value = "url", required = true) String url) {
		try {
			Object result = systemPublishService.getTotalTerminalGroup(company_id, user_name, url);
			//获取返回值		
			return ResultUtils.success(result);
		}catch(Exception e) {
			log.info("获取终端分组总数失败", e);
			return ResultUtils.error(1,"获取终端分组总数失败");
		}			
	}
	
	/**
	 * 根据所有类型所有分组
	 * @param company_id
	 * @param sourceType
	 * @return
	 */
	@RequestMapping("/getAllSampleGroups")
	public Object getAllSampleGroups(int company_id, String sourceType) {
		return null;
	}
	/**
	 * 根据类型获取所有分组
	 * @param company_id
	 * @param sourceType
	 * @return
	 */
	@RequestMapping("/getSampleGroups")
	public Object getSampleGroups(int company_id, String sourceType) {
		return null;
	}
	
	@RequestMapping("/getTerminals")
	public Object getTerminals(int company_id, String user_name) {
		return null;
	}
}//end
