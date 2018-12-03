package com.xxx.notes.entity;

import com.xxx.notes.base.annotation.Like;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserEntity
 * @Description 用户实体
 * @Author l17561
 * @Date 2018/7/20 17:30
 * @Version V1.0
 */
@Table(name = "sys_user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    private String username;

    private String password;

    @Like
    private String nickName;

    private Date createTime;

    private Date updateTime;

    private Integer delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

}
