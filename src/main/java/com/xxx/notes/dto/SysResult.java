package com.xxx.notes.dto;

public class SysResult {

    private Integer code;
    private String msg;
    private Object data;

    private SysResult(){}

    public SysResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private SysResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static SysResult ok () {

        return new SysResult(200,"Success");
    }

    public static SysResult ok (Object data) {

        return new SysResult(200,"Success", data);
    }

    public static SysResult build (Integer code, String msg) {

        return new SysResult(code, msg, null);
    }

    public static SysResult build (Integer code, String msg, Object data) {

        return new SysResult(code, msg, data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "[{" +
                "\"code\" : " + code +
                ", \"msg\" : " + msg +
                ", \"data\" : " + data +
                "}]";
    }
}
