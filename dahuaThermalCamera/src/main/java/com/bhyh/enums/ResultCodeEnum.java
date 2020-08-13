package com.bhyh.enums;
public enum ResultCodeEnum {


    USER_BAN(-2, "您的账户已被禁用"),
    TOKEN_EXPIRE(-1, "认证失效"),
    ACCESS_DENIED(-3, "连接失败"),

    ERROR(1, "fail"),
    SUCCESS(0, "返回成功"),

    FILE_UPLOAD_NULL(1002,"上传图片为空文件"),    //add by david_lin请求失败,空文件

    UNKNOW_ERROR(100000, "未知异常"),
    ILLEGAL_ARGUMENT(100001, "参数无效"),
    METHOD_NOT_ALLOW(100002, "方法不允许访问"),
    NOT_LOGIN_IN(100003, "未登录系统，请先登录"),
    PAGE_NOT_FOUND(100005, "页面找不到"),

    TOKEN_BE_USED(100006, "当前账户已在其他地方登录,请确认后重新登录"),


    USER_NOT_FOUND(200001, "该用户不存在"),
    USER_PASSWORD_NOT_MATCH(200002, "账户密码不匹配"),
    ACCOUNT_EXIST(200003, "账户已存在"),
    MOBIILE_EXIST(200004, "该手机号已注册"),
    ROLE_NOT_FOUND(200005, "该角色不存在"),
    MENU_EXIST(200006,"菜单已存在"),
    EMAIL_EXIST(200007,"该邮箱已注册"),


    DEVICE_NOT_FOUND(300001, "设备找不到");

    private int code;
    private String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
