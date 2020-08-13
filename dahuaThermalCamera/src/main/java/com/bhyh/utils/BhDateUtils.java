package com.bhyh.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BhDateUtils {
	/**
     * 获取当前所在时区系统时间
     * 目前我们在东八区
     * @return
     */
	public static Date getGlobalCurrentDate(){
        return new Date();
    }


    /**
     * 获取当前时间字符串
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getLocalCurrDateStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now_date = simpleDateFormat.format(new Date());
        return now_date;
    }
    
}
