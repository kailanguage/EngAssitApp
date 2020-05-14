package com.kailang.engassit.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kailang.engassit.data.entity.User;

import java.util.List;
@Dao
public interface UserDao {
    //用户注册
    @Insert
    void insert(User...user);
    //修改个人信息
    @Update
    void update(User user);

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUser();

    @Query("DELETE FROM user")
    void deleteAll();

}
