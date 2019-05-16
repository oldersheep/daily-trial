package com.xxx.notes.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @ClassName UserQueryRequest
 * @Description
 * @Author aitaiyo
 * @Date 2019/4/29 22:15
 * @Version 1.0
 */
@Data
public class UserQueryRequest implements Serializable {

    @NotEmpty(message = "{login.username.validator}")
    @Length(max = 16, message = "用户名长度过长")
    private String username;

    @NotEmpty(message = "{login.password.validator}")
    @Length(min = 4, message = "密码至少四位")
    private String password;
}
