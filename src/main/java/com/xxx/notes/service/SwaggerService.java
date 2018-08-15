package com.xxx.notes.service;

import com.xxx.notes.entity.MemberEntity;

import java.util.List;

public interface SwaggerService {

    /**
     * 查询所有的员工信息
     * @return
     */
    List<MemberEntity> listAll();

    /**
     * 根据员工ID查询员工信息
     * @param mid
     * @return
     */
    MemberEntity queryById(String mid);

    Integer saveMember(MemberEntity memberEntity);

    Integer deleteMemberById(String mid);

}
