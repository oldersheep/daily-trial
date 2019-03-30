package com.xxx.notes.mapper;

import com.xxx.notes.base.annotation.Like;
import com.xxx.notes.base.annotation.SaveRedis;
import com.xxx.notes.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {//extends BaseMapper<UserEntity> {

    /**
     *  根据用户名密码查询用户
     * @param username 用户名，唯一
     * @param password 密码
     * @return 当前用户
     */
    /*@SaveRedis(prefix = "KE", key = "#username")
    UserEntity findByUsernameAndPassword(@Param("username") String username,
                                         @Param("password") String password);


    List<UserEntity> findUserByNickNameLike(@Like String nickName);

    List<UserEntity> findUserByNickNameLike(@Param("userEntity") UserEntity userEntity);

    int updateByUserNameSelective(UserEntity userEntity);

    List<UserEntity> findUserByDelFlagLike(@Like int delFlag);*/
}
