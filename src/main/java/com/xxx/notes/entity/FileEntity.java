package com.xxx.notes.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName FileEntity
 * @Description 文件实体
 * @Author l17561
 * @Date 2018/8/31 15:10
 * @Version V1.0
 */
@Table(name = "tb_files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String originalName;
    private String fileSuffix;
    private String destPath;
    private String fileOwner;
    private Date createTime;
    private Date updateTime;

    public FileEntity() {
    }

    public FileEntity(String originalName, String fileSuffix, String destPath, String fileOwner, Date createTime) {
        this.originalName = originalName;
        this.fileSuffix = fileSuffix;
        this.destPath = destPath;
        this.fileOwner = fileOwner;
        this.createTime = createTime;
        this.updateTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public String getFileOwner() {
        return fileOwner;
    }

    public void setFileOwner(String fileOwner) {
        this.fileOwner = fileOwner;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
