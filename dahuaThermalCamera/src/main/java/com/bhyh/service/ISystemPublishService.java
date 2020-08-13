package com.bhyh.service;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bhyh.result.PageResult;
import com.bhyh.result.Pager;

import net.sf.json.JSONObject;

/**
 * 
 * @author dyonga
 *
 */
public interface ISystemPublishService {
	/**
	  * 上传素材接口
	 * @param group 组别（可选）
	 * @param company_id （公司编号，建议默认设置为1）
	 * url===>ZHSD/upload/upload.php
	 */
	public Object uploadSamples(int company_id, String group, MultipartFile file) throws Exception;
	/**
	   * 获取素材url===>ZHSD/group/get_group_source.php
	 * @param sourceType 素材类型: video(视频) music(音频) picture(图片)  字符串： $table = array($sourceType);
	 * @param company_id 公司编号
	 * @param user_name 是否为超级用户(SuperAdministrator，默认为"")
	 * @param group (可选，默认为system)分组，图片(system/picture)，视频(system/video)，音乐(system/music)......)
	 * @param is_total_file (可选)是否显示分组下所有资源文件)-(0->否，1->是，默认为0)
	 * @param get_group_tree (可选)是否获取所有分组
	 * @param include_folder (可选)true:包含文件夹，false:不包含文件夹
	 * @param findType (可选)参数(name:按文件名查询，groupName:按分组名查询)
	 * @param findValue (可选)查询的内容
	 * @param sort (可选)1，按更新时间升序排序，2，按更新时间降序排序，3，按节目名称升序排序，4，按节目名称将序排序,5，按公司编号升序排序，6按公司编号降序排序，7，按节目大小升序排序，8，按节目大小降序排序，9，按上传时间升序排序，10，按上传时间降序排序，(默认按照上传时间升序排序)
	 * @param method 
	 * @param pager 
	 * @param method (可选)total(获取资源总数)
	
	 */
	
	public Object getAllSamples(int company_id, String user_name, int is_total_file,
			int get_group_tree, boolean include_folder, int sort, String method, Pager pager);
	public Object getSamplesByFileName(String sourceType, int company_id, String user_name, String group, int is_total_file,
			int get_group_tree, boolean include_folder, String findType, String findValue, int sort);
	public Object getSamplesByDirectoryName(String sourceType, int company_id, String user_name, int is_total_file,
			int get_group_tree, boolean include_folder, String findType, String findValue, int sort, Pager pager);
	/*
	 * public PageResult getSamples(String sourceType, int company_id, String
	 * user_name, String group, int is_total_file, int get_group_tree, boolean
	 * include_folder, String findType, String findValue, int sort, String method,
	 * Pager pager);
	 */
	/**
	   *  删除素材 url===>ZHSD/delete/delete_batch.php
	 * @param type_num 要删除的资源类型数目 
	 * @param type 要删除的资源类型  $_REQUEST['type'.$i]
	 * @param ids 资源id 字符串数组且数字中每个元素使用“，”隔开  $id = explode(',', $_REQUEST['id'.$i]);
	 * @return 
	 */
	public Object deleteSamples(int company_id, String type, String id);
	/**
	 * 发布素材 url===>ZHSD/release_program/release_program.php
	 * @param method 固定为post
	 * @param tid 终端设备的id号，例如tid=1,2,3,4  字符串：$tid_id = explode(',', $data['tid']);
	 * @param type_num 发布的资源类型数
	 * @param type video(视频) music(音频)  picture(图片)..... 字符串： if (!isset($data['type'.$i]) OR $data['type'.$i] == '') {
	 * @param id 资源id以逗号为分隔符，例如：id1=12,13,14     $id = explode(',', $data['id' . $type_num]);
	 * @param links 大图,小图的关联(link=111-123|125-126)
	 * @param publish_type order(预约发布),check(审核发布)，默认为立即发布
	 * @param status online(在线),offline(离线),standby(待机),all(所有),在tid参数不存在时有用
	 * @param group 分组，在tid参数不存在时有用
	 * @param date 预约发布时间(2018-01-05 12:00:00)
	 * @param company_id 公司编号
	 * @param immediate 即时播放(0:播放一次，1，按设置时间播放)
	 * @param immediate_time 即时播放时间
	 * @param nickname 发布名称
	 * @param release_record_id 发布列表id编号
	 * @param user_name 是否为超级用户(SuperAdministrator，默认为空)
	 * @param is_cover 是否覆盖(1:覆盖,0:追加,默认为0)
	 * @param playlist 时间编排路径(需要在获取发布前保存的时间编排路径)
	 * @return 
	 */
	public Object publishSamples(/* String method, */String tid, String type, String id, String link,
			String publish_type, String status/* , String group */, Date date, int company_id, int immediate, Date immediate_time, 
			String nickname, int release_record_id/* , String user_name */, int is_cover/* , String playlist */);
	/**
	 * url===>ZHSD/terminal/terminal.php
	 * @param company_id (公司编号)
	 * @param group (组别，默认system)(可选)
	 * @param status (online:在线，offline:离线,standby:待机,buffer:正在缓冲，download:正在下载，reservation：预约发布，check：待审核，默认所有)(可选)
	 * @param sort (0:终端编号降序，1:终端编号升序，2:用户编号降序，3:用户编号升序，4:进度升序,5:进度降序，6:节目个数升序,7:文件个数降序,默认按公司编号升序排序)(可选)
	 * @param user_name (超级用户:SuperAdministrator,默认为空)(可选)
	 * @param findType (查找类型，name:终端名称,userGiveName:自定义名称,groupName:组别,programNum:节目个数,)(可选)
	 * @param findValue (查找内容)(可选)
	 * @param pager 
	 * @return
	 */
	public Object getAllTerminalDevices(int company_id/* , String group */ , String status ,
			int sort/* , String method */, String user_name, /* String findType, String findValue, */Pager pager);
	/**
	 * url===>ZHSD/terminal/terminal.php
	 * @param id (终端的id号，以逗号隔开，如id=2,3,4,5,6，7)
	 * @param company_id (公司编号)
	 * @param group (组别，默认system)(可选)
	 * @param status (online:在线，offline:离线,standby:待机，默认所有)(可选)
	 * @return
	 */
	public Object delTerminalDevices(String id, int company_id, String group, String status);
	/**
	 * url===>ZHSD/terminal/terminal.php
	 * @param id 终端id编号
	 * @param company_id (公司编号)
	 * @param userGivenName 用户自定义名称
	 * @return
	 */
	public Object updateTerminalDevices(String id, int company_id, String userGivenName);

	/**
	 * 
	 * @param setting (终端配置参数)
	 * @param id 终端id
	 */
	public void save_setting(String setting, int id);
	
	/**
	 * 
	 * @param id 终端id
	 * @return
	 */
	public String get_setting(int id);
	/**
	 * 
	 * @param company_id (公司编号)
	 * @param user_name (是否为超级用户:SuperAdministrator)(可选) 
	 * @param url 所属分组???
	 * @param pager 
	 * @return
	 */
	public Object getTerminalGroup(int company_id, String user_name, String url, Pager pager);
	/**
	 * url===>ZHSD/group_terminal/group_terminal.php
	 * @param company_id (公司编号)
	 * @param group_name (分组名)
	 * @param url 所属分组(里面用的是group内容，(分组，图片(system/picture)，视频(system/video)，音乐(system/music)......))
	 * @return
	 */
	public Object insertTerminalGroup(int company_id, String group_name, String url);
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
	public Object delTerminalGroup(int company_id, String group_name, String url, boolean is_empty_group, boolean del_curr_group,
			boolean is_keep_source);
	/**
	 * 
	 * @param company_id (公司编号)
	 * @param group_name (分组名)
	 * @param url 所属分组
	 * @param value 修改后的组名
	 * @return
	 */
	public Object updateTerminalGroup(int company_id, String group_name, String url, String value);
	/**
	 * 
	 * @param company_id (公司编号)
	 * @param user_name (是否为超级用户:SuperAdministrator)(可选)
	 * @param url 所属分组
	 * @return
	 */
	public Object getTotalTerminalGroup(int company_id, String user_name, String url);
	public List<Object> orderResByDate(String result, Date date);
	public Object getTerminalDevicesByName(int company_id, String terminalGroup, String status, int sort,
			String user_name, String findType, String findValue, Pager pager);
	public Object insertTerminalDevices(String id, int company_id, String userGivenName);
	public Object getAllTerminalStatus(int company_id, String user_name);
	public Object getTerminalStatusByName(int company_id, String group, String user_name, String findType,
			String findValue);
	public Object getSamplesByGroupAndSourceType(String sourceType, int company_id, String user_name, String group,
			int is_total_file, int get_group_tree, boolean include_folder, int sort, String findType, String findValue, Pager pager);
	
	
	
	/* public Object getAllSamplesDir(); */


}//end









