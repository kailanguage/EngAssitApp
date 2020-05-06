package com.kailang.engassit.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Stat {
    @PrimaryKey
    private Integer userno;

    private Short today;

    private Integer total;

    private Integer actday;

    public Integer getUserno() {
        return userno;
    }

    public void setUserno(Integer userno) {
        this.userno = userno;
    }

    public Short getToday() {
        return today;
    }

    public void setToday(Short today) {
        this.today = today;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getActday() {
        return actday;
    }

    public void setActday(Integer actday) {
        this.actday = actday;
    }
}