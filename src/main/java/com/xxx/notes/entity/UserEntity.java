package com.xxx.notes.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName UserEntity
 * @Description
 * @Author aitaiyo
 * @Date 2019/4/29 21:22
 * @Version 1.0
 */
@Data
@Table(name = "sys_user")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String username;
    private String password;
    private String nick_name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String delFlag;

}
