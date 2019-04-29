package com.xxx.notes.controller;

import com.xxx.notes.base.dto.BaseResponse;
import com.xxx.notes.entity.UserEntity;
import com.xxx.notes.mapper.UserMapper;
import com.xxx.notes.model.UserQueryRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DemoController
 * @Description
 * @Author aitaiyo
 * @Date 2019/4/29 21:34
 * @Version 1.0
 */
@Validated
@RestController
public class DemoController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/query")
    public UserEntity query(@NotEmpty(message = "参数不能为空") @RequestParam("id") String id) {

        System.out.println("请求参数为：" + id);
        return null;
    }

    @PostMapping("/login")
    public BaseResponse login(@Validated @RequestBody UserQueryRequest request) {
        UserEntity record = new UserEntity();
        BeanUtils.copyProperties(request, record);
        record = userMapper.selectOne(record);
        if (record == null) {
            return BaseResponse.build(10001, "用户名或者密码错误");
        }
        return BaseResponse.build(200, "登录成功", record);
    }

    @GetMapping("/insert")
    public void insert() {
        List<UserEntity> list = new ArrayList<>();
        UserEntity user = new UserEntity();
        user.setId("a");
        user.setUsername("1");
        user.setUpdateTime(LocalDateTime.now());
        list.add(user);
        user = new UserEntity();
        user.setId("b");
        user.setUsername("2");
        list.add(user);
        user = new UserEntity();
        user.setId("c");
        user.setUsername("3");
        list.add(user);

        userMapper.insertListExt(list);
    }

}
