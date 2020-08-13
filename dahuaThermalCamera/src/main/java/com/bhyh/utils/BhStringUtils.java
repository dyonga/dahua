package com.bhyh.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class BhStringUtils extends StringUtils{

	/**
	 * 字符串按照指定分隔符，切割成List集合
	 * @param source 字符串
	 * @param delim 分隔符
	 * @return
	 */
	public static List<String> delimitedStringToList(String source, String delim) {
		List<String> result = new ArrayList<String>();
		if(!hasText(source))
			return result;
		String[] tempStr = source.split(delim);
		for (int i = 0; i < tempStr.length; i++) {
			result.add(tempStr[i]);
		}
		return result;
	}
	/**
	 * 判断指定List集合，是否包含该字符串
	 * @param source 字符串
	 * @param list 集合
	 * @return
	 */
	@SuppressWarnings("unused")
	public static boolean inList(String source, List<String> list) {
		if(list == null || list.size() == 0 || !hasLength(source))
			return false;
		for (int i = 0; i < list.size(); i++) {
			if(source.equals(list.get(i)));
			return true;
		}
		return false;
	}
	/**
	  *  判断指定数组，是否包含该字符串
	 * @param source
	 * @param arr
	 * @return
	 */
	public static boolean inArray(String source, String[] arr) {
		if(arr == null || arr.length == 0 || !hasLength(source))
			return false;
		for (int i = 0; i < arr.length; i++) {
			if(source.equals(arr[i]))
				return true;
		}
		return false;
	}
	/**
	 * 判断指定字符串按照指定切割夫切割后，数组中是否包含另一个字符串
	 * @param source
	 * @param str
	 * @param delim
	 * @return
	 */
	public static boolean inDelimitString(String source, String str2Split, String delim) {		
		return inArray(source, str2Split.split(delim));
	}
	
	
	public static String getRandomString(int length) {
	     String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random = new Random();
	     StringBuffer sb = new StringBuffer();
	     for (int i = 0; i < length; i++) {
	         int number = random.nextInt(str.length());
	         sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }
	 /**
     * StringUtils工具类方法
     * 获取一定长度的随机数字，范围0-9
     * @param length：指定字符串长度
     * @return 一定长度的随机字符串
     */
    public static String getRandomIntegerByLength(int length) {
        String base = "0123456789";
        String baseWithout0 = "123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int number = 0;
        for (int i = 0; i < length; i++) {
        	if(i == 0) {
        		number = random.nextInt(baseWithout0.length());
        		sb.append(baseWithout0.charAt(number));
        	}
            number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 是否是正整数
     * @param value
     */
    public static boolean isPositiveInteger(String value){
        Pattern p=null;//正则表达式
        Matcher m=null;//操作符表达式
        boolean b=false;
        p=Pattern.compile("^[1-9]\\d*$");
        m=p.matcher(value);
        b=m.matches();
        return b;
    }
}
