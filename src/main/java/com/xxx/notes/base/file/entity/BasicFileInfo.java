package com.xxx.notes.base.file.entity;

import java.io.Serializable;

/**
 * @ClassName BasicFileInfo
 * @Description 文件的基础信息抽象
 * @Author l17561
 * @Date 2019/3/25 17:30
 * @Version V1.0
 */
public class BasicFileInfo implements Serializable {

    private String fileId;
    private String fileName;
    private String fileType;
    private String fileUrl;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
