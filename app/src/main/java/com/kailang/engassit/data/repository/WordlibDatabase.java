package com.kailang.engassit.data.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kailang.engassit.data.dao.WordlibDao;
import com.kailang.engassit.data.entity.Wordlib;

@Database(entities = {Wordlib.class},version = 1,exportSchema = false)
public abstract class WordlibDatabase extends RoomDatabase {
    private static WordlibDatabase INSTANCE;
    static synchronized WordlibDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordlibDatabase.class, "wordlib").build();
        }
        return INSTANCE;
    }
    public abstract WordlibDao getWordlibDao();
}
