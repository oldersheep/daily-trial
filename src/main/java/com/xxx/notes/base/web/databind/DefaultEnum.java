package com.xxx.notes.base.web.databind;

/**
 * @Description 案例--- 前端传值直接转换为枚举 @RequestParam方式
 * @Author l17561
 * @Date 2019/5/9 17:21
 * @Version V1.0
 */
public enum DefaultEnum implements ParameterizedEnum {
    YES("1", "Success"),
    NO("0", "Fail");

    private String code;
    private String description;

    DefaultEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean equal(Object o) {
        if (o instanceof String) {
            return this.getCode().equals(o);
        } else {
            return equals(o);
        }
    }

    @Override
    public boolean canResolve(String t) {

        return this.code.equals(t);
    }

    @Override
    public String getRequestParameter() {
        return this.code;
    }

}
