package com.kailang.engassit.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kailang.engassit.data.entity.Sentence;


import java.util.List;

@Dao
public interface SentenceDao {
    @Delete
    int delete(Sentence...sentences);

    @Query("DELETE FROM sentence")
    void deleteAll();

    @Insert
    void insert(Sentence...sentences);
    @Update
    void update(Sentence...sentences);
    @Query("SELECT * FROM Sentence")
    LiveData<List<Sentence>> getAllSentences();

    //模糊匹配
    @Query("SELECT * FROM Sentence WHERE en LIKE :pattern or cn LIKE :pattern ORDER BY sno DESC")
    LiveData<List<Sentence>> findWithPattern(String pattern);
}
