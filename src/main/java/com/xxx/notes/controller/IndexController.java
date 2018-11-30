package com.xxx.notes.controller;

import com.xxx.notes.base.annotation.AuthToken;
import com.xxx.notes.dto.SysResult;
import com.xxx.notes.service.EmailService;
import com.xxx.notes.service.UserManageService;
import com.xxx.notes.vo.PageBean;
import com.xxx.notes.vo.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName IndexController
 * @Description 主Controller入口
 * @Author l17561
 * @Date 2018/8/13 10:00
 * @Version V1.0
 */
@RestController
public class IndexController {

    @Autowired
    private UserManageService userManageService;
    @Autowired
    private EmailService emailSerrvice;

    @ApiOperation(value = "登录方法接口", notes="登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces={"application/json"})
    public SysResult login(@RequestBody User user) {
        // 按理说应该try...catch
        return userManageService.login(user.getUsername(), user.getPassword());
    }

    @ApiOperation(value = "测试AuthToken注解", notes="测试AuthToken注解")
    @RequestMapping(value = "/testAuth", method = RequestMethod.POST, produces={"application/json"})
    @AuthToken
    public SysResult testAuth(@RequestBody PageBean pageBean){

        return userManageService.listAllUser(pageBean);
    }

    @ApiOperation(value = "测试AuthToken注解", notes="测试AuthToken注解")
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public SysResult register(){
        emailSerrvice.sendTemplateMail("3");
        return SysResult.ok();
    }

    @ApiOperation(value = "查找好友", notes="查找好友")
    @RequestMapping(value = "/friend", method = RequestMethod.GET)
    public SysResult searchFriend(@RequestParam(value = "nickName") String nickName) {

        return SysResult.ok(userManageService.findUserByNickNameLike(nickName));
    }
}
