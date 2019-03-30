package com.xxx.notes.base.file.exception;

/**
 * @Description 文件格式不支持
 * @Author l17561
 * @Date 2019/3/25 18:00
 * @Version V1.0
 */
public class FileNotSupportException extends RuntimeException {

    public FileNotSupportException(String msg) {
        super(msg);
    }
}
