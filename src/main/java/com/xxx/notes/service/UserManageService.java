package com.xxx.notes.service;

import com.xxx.notes.base.annotation.Key;
import com.xxx.notes.base.annotation.SaveRedis;
import com.xxx.notes.dto.SysResult;
import com.xxx.notes.entity.UserEntity;
import com.xxx.notes.vo.PageBean;

import java.util.List;

public interface UserManageService {

    /**
     *  用户登录，根据用户名密码找用户
     * @param username 登录名
     * @param password 密码
     * @return
     */
     SysResult login(String username, String password);

    /**
     *  分页查询所有用户
     * @param pageBean
     * @return
     */
    SysResult listAllUser(PageBean pageBean);

    List<UserEntity> findUserByNickNameLike(String nickName);
}
