package com.kailang.engassit.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kailang.engassit.data.entity.Wordlib;

import java.util.List;

@Dao
public interface WordlibDao {
    @Delete
    void delete(Wordlib...wordlibs);
    @Insert
    void insert(Wordlib...wordlibs);

    @Query("DELETE FROM wordlib")
    void deleteAll();

    @Update
    void update(Wordlib...wordlibs);

    @Query("SELECT * FROM Wordlib")
    LiveData<List<Wordlib>> getAllWordlibs();

    @Query("SELECT * FROM wordlib WHERE wlevel=:wlevel")
    LiveData<List<Wordlib>> getAllByLevel(short wlevel);

    //模糊匹配
    @Query("SELECT * FROM wordlib WHERE en LIKE :pattern or cn LIKE :pattern ORDER BY wno DESC")
    LiveData<List<Wordlib>> findWithPattern(String pattern);
}
