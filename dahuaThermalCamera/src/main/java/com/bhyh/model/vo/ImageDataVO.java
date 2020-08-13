package com.bhyh.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 接收入参
 * @author Administrator
 *
 */
public class ImageDataVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;//主键id
	private String imageUrl;//图片存储地址
//	private String errNo ;//错误码	
//	private String errMsg ;//错误消息
	private String suggestSummary ;//处理结果
	private String suggestSummaryCode ;//处理结果码
	
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

//	public String getErrNo() {
//		return errNo;
//	}
//
//	public void setErrNo(String errNo) {
//		this.errNo = errNo;
//	}
//
//	public String getErrMsg() {
//		return errMsg;
//	}
//
//	public void setErrMsg(String errMsg) {
//		this.errMsg = errMsg;
//	}

	public String getSuggestSummary() {
		return suggestSummary;
	}

	public void setSuggestSummary(String suggestSummary) {
		this.suggestSummary = suggestSummary;
	}

	public String getSuggestSummaryCode() {
		return suggestSummaryCode;
	}

	public void setSuggestSummaryCode(String suggestSummaryCode) {
		this.suggestSummaryCode = suggestSummaryCode;
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

	public ImageDataVO() {
		super();
		// TODO Auto-generated constructor stub
	}
   
}
