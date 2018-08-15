package com.xxx.notes.entity;

import java.io.Serializable;

public class MemberEntity implements Serializable {

    private static final long serialVersionUID = -2029015443286519195L;

    private String mid;
    private String name;

    public MemberEntity() {
    }

    public MemberEntity(String mid, String name) {
        this.mid = mid;
        this.name = name;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MemberEntity{" +
                "mid='" + mid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
