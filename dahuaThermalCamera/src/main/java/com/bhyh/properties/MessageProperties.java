package com.bhyh.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
/**
 * 图片处理格式属性配置文件
 * @author david_lin
 *
 */
@Component
@ConfigurationProperties(prefix="message")
@PropertySource("classpath:file_message.properties")
public class MessageProperties {

	private long filesize;//压缩大小
	private double scaleRatio;//压缩比例
//	private String upPath;//保存路径
	private String imageType;//图片类型
	public long getFilesize() {
		return filesize;
	}
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	public double getScaleRatio() {
		return scaleRatio;
	}
	public void setScaleRatio(double scaleRatio) {
		this.scaleRatio = scaleRatio;
	}
//	public String getUpPath() {
//		return upPath;
//	}
//	public void setUpPath(String upPath) {
//		this.upPath = upPath;
//	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
}
