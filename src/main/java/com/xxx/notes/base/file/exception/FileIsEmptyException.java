package com.xxx.notes.base.file.exception;

import com.xxx.notes.base.exception.GlobalException;

/**
 * 当文件为空时抛出此异常
 */
public class FileIsEmptyException extends GlobalException {

    public FileIsEmptyException() {
        this("当前上传文件不能为空！");
    }

    public FileIsEmptyException(String message) {
        super(message);
    }
}
