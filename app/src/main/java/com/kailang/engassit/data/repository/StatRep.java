package com.kailang.engassit.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kailang.engassit.data.dao.StatDao;
import com.kailang.engassit.data.entity.Stat;

import java.util.List;


public class StatRep{

    private StatDao statDao;
    private Context context;
    private LiveData<List<Stat>> allStat;
    public StatRep(Context context){
        this.context=context;
        StatDatabase statDatabase = StatDatabase.getDatabase(context);
        statDao =statDatabase.getStatDao();
        allStat=statDao.selectAll();
    }
    public void updateStat(Stat...stats) {
        new StatRep.UpdateAsyncTask(statDao).execute(stats);
    }
    public LiveData<List<Stat>> getAllStat(){
        return allStat;
    }
    public void insertStat(Stat...stats) {
        new StatRep.InsertAsyncTask(statDao).execute(stats);
    }

    public void deleteAllStat(){
        new StatRep.DeleteAllAsyncTask(statDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private StatDao statDao;
        public DeleteAllAsyncTask(StatDao statDao) {
            this.statDao = statDao;
        }
        @Override
        protected Void doInBackground(Void...voids) {
            statDao.deleteAll();
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Stat, Void, Void> {
        private StatDao statDao;
        public UpdateAsyncTask(StatDao statDao) {
            this.statDao = statDao;
        }
        @Override
        protected Void doInBackground(Stat...stats) {
            statDao.update(stats);
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Stat, Void, Void> {
        private StatDao statDao;
        public InsertAsyncTask(StatDao statDao) {
            this.statDao = statDao;
        }
        @Override
        protected Void doInBackground(Stat...stats) {
            statDao.insert(stats);
            return null;
        }
    }
}
