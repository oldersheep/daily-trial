package com.xxx.notes.base.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName FileUtils
 * @Description 文件的工具类
 * @Author l17561
 * @Date 2018/8/31 14:40
 * @Version V1.0
 */
public class FileUtils {

    private static final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue(){
            return new SimpleDateFormat("yyyy/MM/dd/HH/");
        }
    };

    /**
     * 获取文件转存路径
     * @return
     */
    public static String fileDestUrl() {
        return dateFormat.get().format(new Date());
    }

}
