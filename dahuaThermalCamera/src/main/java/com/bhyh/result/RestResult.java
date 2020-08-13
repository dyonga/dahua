package com.bhyh.result;
public class RestResult {

    private int code;
    private String msg;
    private Object data;

    @SuppressWarnings("unused")
	private RestResult(){}

    protected RestResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public RestResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RestResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public 	Object getData() {
        return data;
    }

    public RestResult setData(Object data) {
        this.data = data;
        return this;
    }
}
