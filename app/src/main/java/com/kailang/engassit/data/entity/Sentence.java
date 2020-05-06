package com.kailang.engassit.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Sentence {

    private Integer userno;

    @PrimaryKey
    private Integer sno;

    private String en;

    private String cn;

    public Integer getUserno() {
        return userno;
    }

    public void setUserno(Integer userno) {
        this.userno = userno;
    }

    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en == null ? null : en.trim();
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn == null ? null : cn.trim();
    }
}