package com.xxx.notes.base.dto;

public class BaseResponse {

    private Integer code;
    private String msg;
    private Object data;

    private BaseResponse(){}

    private BaseResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private BaseResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static BaseResponse ok () {

        return new BaseResponse(200,"Success");
    }

    public static BaseResponse ok (Object data) {

        return new BaseResponse(200,"Success", data);
    }

    public static BaseResponse build (Integer code, String msg) {

        return new BaseResponse(code, msg, null);
    }

    public static BaseResponse build (Integer code, String msg, Object data) {

        return new BaseResponse(code, msg, data);
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
