package com.kailang.engassit.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.kailang.engassit.data.entity.Word;
import java.util.List;

@Dao
public interface WordDao {
    @Delete
    void delete(Word...words);

    @Query("DELETE FROM word")
    void deleteAll();

    @Insert
    void insert(Word...words);
    @Update
    void update(Word...words);

    @Query("SELECT * FROM Word")
    LiveData<List<Word>> getAllWords();

    //模糊匹配
    @Query("SELECT * FROM Word WHERE en LIKE :pattern or cn LIKE :pattern ORDER BY wno DESC")
    LiveData<List<Word>> findWithPattern(String pattern);
}
