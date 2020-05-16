package com.kailang.engassit.data.repository;

import android.content.Context;
import android.icu.lang.UScript;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kailang.engassit.data.dao.StatDao;
import com.kailang.engassit.data.dao.UserDao;
import com.kailang.engassit.data.entity.User;

import java.util.List;

public class UserRep {
    private UserDao userDao;
    private Context context;
    private LiveData<List<User>> allUsersLive;
    public UserRep(Context context){
        this.context=context;
        UserDatabase userDatabase = UserDatabase.getDatabase(context);
        userDao=userDatabase.getUserDao();
        allUsersLive=userDao.getAllUser();

    }
    public void insertUser(User...user) {
        new UserRep.InsertAsyncTask(userDao).execute(user);
    }

    public LiveData<List<User>> getAllUser() {
        return allUsersLive;
    }

    public void deleteAllUser(){
        new UserRep.DeleteAllAsyncTask(userDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;
        public DeleteAllAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(Void...voids) {
            userDao.deleteAll();
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;
        public InsertAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User...user) {
            userDao.insert(user);
            return null;
        }
    }
}
