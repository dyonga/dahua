package com.bhyh.utils.sdk;


public class BhNetTime {
    public int                dwYear;                   // 年
    public int                dwMonth;                  // 月
    public int                dwDay;                    // 日
    public int                dwHour;                   // 时
    public int                dwMinute;                 // 分
    public int                dwSecond;                 // 秒
    
    public BhNetTime() {
        this.dwYear = 0;
        this.dwMonth = 0;
        this.dwDay = 0;
        this.dwHour = 0;
        this.dwMinute = 0;
        this.dwSecond = 0;
    }
    
    public void setTime(int year, int month, int day, int hour, int minute, int second) {
    	this.dwYear = year;
    	this.dwMonth= month;
    	this.dwDay= day;
    	this.dwHour=hour;
    	this.dwMinute=minute;
    	this.dwSecond=second;
    }
    
    public BhNetTime(BhNetTime other) {
        this.dwYear = other.dwYear;
        this.dwMonth = other.dwMonth;
        this.dwDay = other.dwDay;
        this.dwHour = other.dwHour;
        this.dwMinute = other.dwMinute;
        this.dwSecond = other.dwSecond;
    }
    
    //用于列表中显示
    public String toStringTime() {
        return  String.format("%02d/%02d/%02d %02d:%02d:%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
    }
    
    public String toStringTimeEx() {
        return  String.format("%02d-%02d-%02d %02d:%02d:%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
    }
    
    public String toString() {
    	return String.format("%02d%02d%02d%02d%02d%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
    }
}
