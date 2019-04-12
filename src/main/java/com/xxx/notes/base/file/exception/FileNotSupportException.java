package com.xxx.notes.base.file.exception;

import com.xxx.notes.base.exception.GlobalException;

/**
 * @Description 文件格式不支持
 * @Author l17561
 * @Date 2019/3/25 18:00
 * @Version V1.0
 */
public class FileNotSupportException extends GlobalException {

    public FileNotSupportException() {
        this("文件格式暂不支持！");
    }

    public FileNotSupportException(String msg) {
        super(msg);
    }
}
