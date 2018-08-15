package com.xxx.notes.service.impl;

import com.xxx.notes.entity.MemberEntity;
import com.xxx.notes.service.SwaggerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SwaggerServiceImpl implements SwaggerService {

    private static List<MemberEntity> list = new ArrayList<>();
    static {
        MemberEntity member = new MemberEntity("m1","First");
        list.add(member);
        member = new MemberEntity("m2","Second");
        list.add(member);
        member = new MemberEntity("m3","Third");
        list.add(member);
        member = new MemberEntity("m4","Fourth");
        list.add(member);
        member = new MemberEntity("m5","中文测啊啊啊试");
        list.add(member);
        member = new MemberEntity("m6","Sixth");
        list.add(member);
    }


    @Override
    public List<MemberEntity> listAll() {
        System.out.println("试sss试");
        return list;
    }

    @Override
    public MemberEntity queryById(String mid) {
        for (MemberEntity memberEntity : list) {
            if (mid.equals(memberEntity.getMid())){
                return memberEntity;
            }
        }
        return null;
    }

    @Override
    public Integer saveMember(MemberEntity memberEntity) {
        list.add(memberEntity);
        return 1;
    }

    @Override
    public Integer deleteMemberById(String mid) {
        Iterator<MemberEntity> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (mid.equals(iterator.next().getMid())){
                iterator.remove();
                return 1;
            }
        }
        return 0;
    }
}
