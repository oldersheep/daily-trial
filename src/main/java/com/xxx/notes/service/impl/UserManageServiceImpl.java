package com.xxx.notes.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.notes.base.constant.Constant;
import com.xxx.notes.base.service.RedisService;
import com.xxx.notes.base.utils.TokenGeneratorUtil;
import com.xxx.notes.dto.SysResult;
import com.xxx.notes.entity.UserEntity;
import com.xxx.notes.mapper.UserMapper;
import com.xxx.notes.service.UserManageService;
import com.xxx.notes.vo.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            String token = TokenGeneratorUtil.md5Generate(username, password);
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
}
