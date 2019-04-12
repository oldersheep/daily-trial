package com.xxx.notes.base.file.exception;

import com.xxx.notes.base.exception.GlobalException;

/**
 * @Description 文件信息持久化异常，入库异常
 * @Author l17561
 * @Date 2019/3/25 17:54
 * @Version V1.0
 */
public class FileInfoPersistentException extends GlobalException {

    public FileInfoPersistentException() {
        this("文件信息入库异常！");
    }

    public FileInfoPersistentException(String msg) {
        super(msg);
    }
}
