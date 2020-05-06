package com.kailang.engassit.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word {

    private Integer userno;
    @PrimaryKey
    private Integer wno;

    private String en;

    private String cn;

    private Short wlevel;

    public Integer getUserno() {
        return userno;
    }

    public void setUserno(Integer userno) {
        this.userno = userno;
    }

    public Integer getWno() {
        return wno;
    }

    public void setWno(Integer wno) {
        this.wno = wno;
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

    public Short getWlevel() {
        return wlevel;
    }

    public void setWlevel(Short wlevel) {
        this.wlevel = wlevel;
    }
}