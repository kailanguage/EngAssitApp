package com.kailang.engassit.data.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kailang.engassit.data.dao.StatDao;
import com.kailang.engassit.data.entity.Stat;

@Database(entities = {Stat.class},version = 1,exportSchema = false)
public abstract class StatDatabase extends RoomDatabase {
    private static StatDatabase INSTANCE;
    static synchronized StatDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StatDatabase.class, "stat").build();
        }
        return INSTANCE;
    }
    public abstract StatDao getStatDao();
}
