package com.xxx.notes.mapper;

import com.xxx.notes.base.annotation.Key;
import com.xxx.notes.base.annotation.Like;
import com.xxx.notes.base.annotation.SaveRedis;
import com.xxx.notes.base.mapper.BaseMapper;
import com.xxx.notes.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     *  根据用户名密码查询用户
     * @param username 用户名，唯一
     * @param password 密码
     * @return 当前用户
     */
    @SaveRedis(prefix = "KE")
    UserEntity findByUsernameAndPassword(@Key @Param("username") String username,
                                         @Param("password") String password);


    List<UserEntity> findUserByNickNameLike(@Like @Param("nickName") String nickName);

    int updateByUserNameSelective(UserEntity userEntity);
}
