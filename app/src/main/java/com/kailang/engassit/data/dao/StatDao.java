package com.kailang.engassit.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kailang.engassit.data.entity.Stat;

import java.util.List;

@Dao
public interface StatDao {
    @Insert
    void insert(Stat...stats);

    @Update
    int update(Stat...stat);

    @Query("DELETE FROM stat")
    void deleteAll();

    @Query("SELECT * FROM stat")
    LiveData<List<Stat>> selectAll();

}
