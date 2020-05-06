package com.kailang.engassit.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public class Wordlib {

    @PrimaryKey
    private Integer wno;

    private short wlevel;

    private String en;

    private String cn;

    public Integer getWno() {
        return wno;
    }

    public void setWno(Integer wno) {
        this.wno = wno;
    }

    public short getWlevel() {
        return wlevel;
    }

    public void setWlevel(short wlevel) {
        this.wlevel = wlevel;
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