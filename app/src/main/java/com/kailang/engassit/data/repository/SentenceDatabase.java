package com.kailang.engassit.data.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kailang.engassit.data.dao.SentenceDao;
import com.kailang.engassit.data.entity.Sentence;

@Database(entities = {Sentence.class},version = 1,exportSchema = false)
public abstract class SentenceDatabase extends RoomDatabase {
    private static SentenceDatabase INSTANCE;
    static synchronized SentenceDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SentenceDatabase.class, "sentence").build();
        }
        return INSTANCE;
    }
    public abstract SentenceDao getSentenceDao();
}
