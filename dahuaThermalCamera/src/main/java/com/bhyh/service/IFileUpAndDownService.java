package com.bhyh.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
/**
 * 处理图片上传下载的接口
 * @author david_lin
 *
 */
public interface IFileUpAndDownService {
	/**
     * 接收前端上传的图片，本地化存储
     * @param file 上传的图片文件
     * @return 
     */
    Map<String,Object> uploadPicture(MultipartFile file);
    /**
     * 接收前端上传的视频对象，本地化存储
     * @param file 上传的图片文件
     * @return 
     */
    Map<String,Object> uploadVideo(MultipartFile file);
    /**
     * 
     * @param file
     * @return
     */
    Map<String,Object> uploadSamples4PublishSys(MultipartFile file);
}
