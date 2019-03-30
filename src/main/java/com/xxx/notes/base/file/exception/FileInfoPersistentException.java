package com.xxx.notes.base.file.exception;

/**
 * @Description 文件信息持久化异常，入库异常
 * @Author l17561
 * @Date 2019/3/25 17:54
 * @Version V1.0
 */
public class FileInfoPersistentException extends RuntimeException {

    public FileInfoPersistentException(String msg) {
        super(msg);
    }
}
