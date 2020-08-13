package com.bhyh.service;



import com.bhyh.netsdk.lib.NetSDKLib.NET_IN_RADIOMETRY_STARTFIND;
//import com.bhyh.netsdk.lib.NetSDKLib.NET_OUT_RADIOMETRY_DOFIND;

/**
 * 热成像摄像头服务接口
 * @author dyonga
 *
 */
public interface IDhThermalCameralService {
	/**
	   *   登入动作，连接到摄像头
	 * @param ip
	 * @param port
	 * @param userNm
	 * @param pwd
	 * @return
	 */
	public Object login(String ip, int port, String userNm, String pwd);
	/**
	   *   查询温度
	 * @param type UNKNOWN 未知 FIRST_PAGE_QUERY 第一页PRE_PAGE_QUERY NEXT_PAGE_QUERY 下一页
	 * @param currentIndex  页码
	 * @param stuStartFind  
	 * @return
	 */
	public Object/* NET_OUT_RADIOMETRY_DOFIND */ queryHistoryInfo(String type, NET_IN_RADIOMETRY_STARTFIND stuStartFind );
	public void realyPlay(int channel, int stream, String window); 
	
	public Object localCapture();
	
	public Object remoteCapture(int channel);//return snapPicture(chn, 0, 0);
	
	public Object timerCapture(int channel);//snapPicture(chn, 1, 2);
	
	public NET_IN_RADIOMETRY_STARTFIND setStuStartFind(String startTime, String endTime, int meterType, int channel, int index);
	
	

}
