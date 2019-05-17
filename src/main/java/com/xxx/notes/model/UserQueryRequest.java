package com.xxx.notes.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
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

    @Email
    @NotEmpty(message = "邮箱不能为空")
    private String email;

    @Pattern(regexp = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(16[6])|(18[0-9])|(19[8|9]))\\d{8}$", message = "手机号格式不正确")
    @NotEmpty(message = "手机号不能为空")
    private String phone;
}
