package com.xxx.notes.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.notes.base.constant.Constant;
import com.xxx.notes.base.service.RedisService;
import com.xxx.notes.base.util.TokenGeneratorUtils;
import com.xxx.notes.dto.SysResult;
import com.xxx.notes.entity.UserEntity;
import com.xxx.notes.mapper.UserMapper;
import com.xxx.notes.service.UserManageService;
import com.xxx.notes.vo.PageBean;
import com.xxx.notes.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserManageServiceImpl
 * @Description TODO
 * @Author l17561
 * @Date 2018/7/20 17:40
 * @Version V1.0
 */
@Service
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    public SysResult login(String username, String password) {

        UserEntity user = userMapper.findByUsernameAndPassword(username, password);
        if (user != null) {
            String token = TokenGeneratorUtils.md5Generate(username, password);
            redisService.set(username, token, Constant.TOKEN_EXPIRE_TIME);
            redisService.set(token, username, Constant.TOKEN_EXPIRE_TIME);
            redisService.set(token + username, System.currentTimeMillis() + "");

            return SysResult.ok(token);
        } else {
            return SysResult.build(404,"登录失败，查无此人！");
        }
    }

    @Override
    public SysResult listAllUser(PageBean pageBean) {
        int pageNum = pageBean.getPageNum() == null ? Constant.DEFAULT_PAGE_NUM : pageBean.getPageNum();
        int pageSize = pageBean.getPageSize() == null ? Constant.DEFAULT_PAGE_SIZE : pageBean.getPageSize();

        PageHelper.startPage(pageNum, pageSize, pageBean.getOrderBy());
        List<UserEntity> users = userMapper.selectAll();
        PageInfo<UserEntity> pageInfo = new PageInfo<>(users);
        return SysResult.ok(pageInfo);
    }

    @Override
    public List<UserEntity> findUserByNickNameLike(String nickName) {
        UserEntity userEntity = new UserEntity();
        userEntity.setNickName(nickName);
        List<UserEntity> userEntities = userMapper.findUserByNickNameLike(nickName);
        return userEntities;
    }

    @Override
    public void updateUserByUserName(UserEntity userEntity) {
        userEntity.setUpdateTime(new Date());
        userMapper.updateByUserNameSelective(userEntity);
    }

    @Override
    public void insertList() {

        UserEntity user1 = new UserEntity("11");
        UserEntity user2 = new UserEntity("22");
        UserEntity user3 = new UserEntity("33");
        UserEntity user4 = new UserEntity("44");
        UserEntity user5 = new UserEntity("55");
        List<UserEntity> list = Arrays.asList(user1, user2, user3, user4, user5);

        userMapper.insertList(list);
    }
}
