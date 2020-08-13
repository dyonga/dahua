package com.bhyh.model.vo;

import java.util.Date;

/**
 * 接收入参
 * @author Administrator
 *
 */
public class VideoDataVO {
	
	private int id;//主键id
	private String videoId;//video_id
	private String videoUrl;//视频存储地址
	private String videoDuration;//视频时长
	private String videoSuggestion ;//处理建议
	private String suggestSummaryCode ;//处理结果码
	private String suggestSummaryMessage;//违规内容
	
	private byte[] results;//返回的结果集
	private Integer status;//状态:0 正常，1 禁用
	private Date createTime;//创建日期
	private Date updateTime;//更新日期
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getVideoDuration() {
		return videoDuration;
	}
	public void setVideoDuration(String videoDuration) {
		this.videoDuration = videoDuration;
	}
	public String getVideoSuggestion() {
		return videoSuggestion;
	}
	public void setVideoSuggestion(String videoSuggestion) {
		this.videoSuggestion = videoSuggestion;
	}
	public String getSuggestSummaryCode() {
		return suggestSummaryCode;
	}
	public void setSuggestSummaryCode(String suggestSummaryCode) {
		this.suggestSummaryCode = suggestSummaryCode;
	}
	public String getSuggestSummaryMessage() {
		return suggestSummaryMessage;
	}
	public void setSuggestSummaryMessage(String suggestSummaryMessage) {
		this.suggestSummaryMessage = suggestSummaryMessage;
	}
	public byte[] getResults() {
		return results;
	}
	public void setResults(byte[] results) {
		this.results = results;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public VideoDataVO() {
		super();
		
	}
	

}
