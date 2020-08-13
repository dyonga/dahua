package com.bhyh.result;

import java.util.HashMap;

import com.bhyh.enums.ResultCodeEnum;

public class RestResultBuilder<T> {

    private int code = 0;

    private String msg = "返回成功";

    private T data;

    public RestResultBuilder<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public RestResultBuilder<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public RestResultBuilder<T> setData(T data) {
        this.data = data;
        return this;
    }

    public RestResult build() {
        return new RestResult(code, msg, data != null ? data : new HashMap<String,Object>());
    }

    public RestResult error(String msg) {
        return new RestResult(ResultCodeEnum.ERROR.getCode(), msg, new HashMap<String,Object>());
    }

    public RestResult success() {
        return new RestResult(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), new HashMap<String,Object>());
    }

    public RestResult success(String msg) {
        return new RestResult(ResultCodeEnum.SUCCESS.getCode(), msg, new HashMap<String,Object>());
    }

    public RestResult success(String msg, T data) {
        return new RestResult(ResultCodeEnum.SUCCESS.getCode(), msg, data != null ? data : new HashMap<String,Object>());
    }


}
