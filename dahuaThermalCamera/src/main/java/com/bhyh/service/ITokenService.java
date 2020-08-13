package com.bhyh.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

import com.bhyh.model.vo.ImageDataVO;
import com.bhyh.model.vo.ImgResSummaryVO;
import com.bhyh.model.vo.VideoDataVO;

/**
 * 这是一个Token实现类接口
 * @author dyonga
 *
 */
public interface ITokenService {
   /**
          *   返回签名密匙token
    * @param host 服务主机地址
    * @param method 请求方式: post get 
    * @param service controller中的方法名
    * @param action  controller类名
    * @param version 版本号("2019-01-18")
    * @param access_key 密匙
    * @param secret_key  密匙再加密后
    * @param postData  请求的参数
    * @return 返回签名密匙
 * @throws Exception 
    */
	public String tokenRequest( 
			String host, String method, String service, String action, 
			String version, String access_key, String secret_key, String postData) throws Exception;

    public void insertImageDataVO(ImageDataVO vo);

    public void insertVideoDataVO(VideoDataVO vo);
    
    public TreeSet<ImgResSummaryVO> identificationImageUrlRequest(File file) throws FileNotFoundException, IOException;
   
  //  public 
}
