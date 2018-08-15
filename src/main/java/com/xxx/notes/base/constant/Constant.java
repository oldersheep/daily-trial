package com.xxx.notes.base.constant;

/**
 * @ClassName Constant
 * @Description 常量值
 * @Author l17561
 * @Date 2018/8/9 9:26
 * @Version V1.0
 */
public class Constant {

    /**
     * 设置删除标志为真
     */
    public static final Integer DEL_FLAG_TRUE = 1;

    /**
     * 设置删除标志为假
     */
    public static final Integer DEL_FLAG_FALSE = 0;

    /**
     * redis存储token设置的过期时间
     */
    public static final Integer TOKEN_EXPIRE_TIME = 60 * 30;

    /**
     * 设置可以重置token过期时间的时间界限
     */
    public static final Integer TOKEN_RESET_TIME = 60 * 3;

    /**
     * 默认一页中的条数
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 默认页码
     */
    public static final Integer DEFAULT_PAGE_NUM = 1;


}
