package com.xxx.notes.base.file.exception;

import com.xxx.notes.base.exception.GlobalException;

/**
 * @ClassName FileDownloadException
 * @Description 文件下载异常
 * @Author Lilg
 * @Date 2019/4/13 0:09
 * @Version 1.0
 */
public class FileDownloadException extends GlobalException {

    public FileDownloadException() {
        this("文件下载异常！");
    }

    public FileDownloadException(String message) {
        super(message);
    }
}
