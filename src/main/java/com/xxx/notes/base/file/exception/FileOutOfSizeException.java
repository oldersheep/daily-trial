package com.xxx.notes.base.file.exception;

import com.xxx.notes.base.exception.GlobalException;

/**
 * @ClassName FileOutOfSizeException
 * @Description 文件超出规定大小
 * @Author Lilg
 * @Date 2019/4/13 0:03
 * @Version 1.0
 */
public class FileOutOfSizeException extends GlobalException {

    public FileOutOfSizeException() {
        this("文件超出规定大小！");
    }

    public FileOutOfSizeException(String message) {
        super(message);
    }
}
