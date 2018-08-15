package com.xxx.notes.vo;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description 用户登录
 * @Author l17561
 * @Date 2018/8/13 14:03
 * @Version V1.0
 */
public class User implements Serializable {

    private String username;
    private String password;

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
}
