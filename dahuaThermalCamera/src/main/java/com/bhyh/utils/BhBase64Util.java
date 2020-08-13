package com.bhyh.utils;

import org.apache.shiro.codec.Base64;
import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BhBase64Util {
	
	
	/**
	 * 对文件进行编码
	 * 
	 * @param file
	 *            需要编码的问家
	 * @return 对文件进行base64编码后的字符串
	 * @throws Exception
	 */
	public static String file2String(File file) throws Exception {
		StringBuffer sb = new StringBuffer();
		FileInputStream in = new FileInputStream(file);
		int b;
		char ch;
		while ((b = in.read()) != -1) {
			ch = (char) b;
			sb.append(ch);
		}
		in.close();
		// 将buffer转化为string
		String oldString = new String(sb);
		// 使用base64编码
		String newString = compressData(oldString);
		return newString;
	}

	/**
	 * 对文件进行解码
	 * 
	 * @param oldString
	 *            需要解码的字符串
	 * @param filePath
	 *            将字符串解码到filepath文件路径
	 * @return 返回解码后得到的文件
	 * @throws Exception
	 */
	public static File string2File(String oldString, String filePath,String filename) {
		if (!new File(filePath).exists() && !new File(filePath).isDirectory()){       
		    new File(filePath).mkdirs();    
		}
		File file = new File(filePath+filename);
		try{
			if (file.exists()) {
				System.out.println("文件已经存在，不能将base64编码转换为文件");
				return null;
			} else {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			// 对oldString进行解码
			String newString = decompressData(oldString);
			// 将问件内容转为byte[]
			char[] str = newString.toCharArray();
			for (char ch : str) {
				byte b = (byte) ch;
				out.write(b);
			}
			out.close();
		}catch(Exception e){
			log.error("", e);
		}
		
		return file;
	}

	/**
	 * 使用base64编码字符串
	 * 
	 * @param data
	 * @return 编码后的字符串
	 */
	public static String compressData(String data) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DeflaterOutputStream zos = new DeflaterOutputStream(bos);
			zos.write(data.getBytes());
			zos.close();
			return new String(getenBASE64inCodec(bos.toByteArray()));
		} catch (Exception ex) {
			ex.printStackTrace();
			return "ZIP_ERR";
		}
	}

	/**
	 * 使用base64解码字符串
	 * 
	 * @param encdata
	 * @return 解码后的字符串
	 */
	public static String decompressData(String encdata) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			InflaterOutputStream zos = new InflaterOutputStream(bos);
			zos.write(getdeBASE64inCodec(encdata));
			zos.close();
			return new String(bos.toByteArray());
		} catch (Exception ex) {
			ex.printStackTrace();
			return "UNZIP_ERR";
		}
	}

	/**
	 * 调用apache的编码方法
	 * @throws UnsupportedEncodingException 
	 */
	public static String getenBASE64inCodec(byte[] b) throws UnsupportedEncodingException {
		if (b == null)
			return null;
		return new String((new Base64()).encode(b),"utf-8");
	}

	/**
	 * 调用apache的解码方法
	 */
	public static byte[] getdeBASE64inCodec(String s) {
		if (s == null)
			return null;
		return new Base64().decode(s.getBytes());
	}
	/**
	 * 将byte 字符串转回图片
	 */
	/**
     * 将base64转换成图片
     * @param imgBase64String
     * @param dir
     * @param fileName
     * @return
     */
    public static void doSaveBase64AsImageFile(String imgBase64String, String dir, String fileName) {
        // 图像数据为空
        if (imgBase64String == null) {
            log.error("将Base64字符串保存为本地图片文件时发生异常：图片Base64字符串不能为空");
        }

        // Base64解码
        byte[] bytes = Base64.decode(new String(imgBase64String).getBytes());
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {
                // 调整异常数据
                bytes[i] += 256;
            }
        }

        OutputStream out = null;
        try {

            File directory = new File(dir);
            // 判断文件目录是否存在
            if (!directory.exists() && directory.isDirectory()) {
                // 如果不存在则创建目录
                directory.mkdirs();
            }
            // 本地图片文件保存路径
            String imageSaveToFilePath = dir + File.separator + fileName;
            out = new FileOutputStream(imageSaveToFilePath);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("请检查本地图片文件保存路径是否存在：{0}", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("图片Base64字符串保存为本地图片文件时发生异常：{0}", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("图片Base64字符串保存为本地图片文件时发生异常：{0}", e.getMessage());
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("图片Base64字符串保存为本地图片文件关闭OutputStream时发生异常：{0}", e.getMessage());
            }
        }
    }
	
	
	
	
	
	
	
	
	
}