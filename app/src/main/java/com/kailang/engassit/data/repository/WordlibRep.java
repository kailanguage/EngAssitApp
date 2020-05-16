package com.kailang.engassit.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kailang.engassit.data.dao.WordlibDao;
import com.kailang.engassit.data.entity.Wordlib;

import java.util.List;

public class WordlibRep  {
    private LiveData<List<Wordlib>> allWorlibs;
    private WordlibDao wordlibDao;
    private Context context;

    public WordlibRep(Context context){
        this.context=context;
        WordlibDatabase wordlibDatabase = WordlibDatabase.getDatabase(context);
        wordlibDao=wordlibDatabase.getWordlibDao();
    }

    public LiveData<List<Wordlib>> getAllWorlibs() {
        return wordlibDao.getAllWordlibs();
    }
    public LiveData<List<Wordlib>> getWorlibByLevel(short level) {
        return wordlibDao.getAllByLevel(level);
    }

    public void insetWordlib(Wordlib...wordlibs){
        new WordlibRep.InsertAsyncTask(wordlibDao).execute(wordlibs);
    }
    public void updateWordlib(Wordlib...wordlibs){
        new WordlibRep.UpdateAsyncTask(wordlibDao).execute(wordlibs);
    }
    public void deleteWordlib(Wordlib...wordlibs){
        new WordlibRep.DeleteAsyncTask(wordlibDao).execute(wordlibs);
    }
    public void deleteAllWordlibs(){
        new WordlibRep.DeleteAllAsyncTask(wordlibDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordlibDao wordlibDao;
        public DeleteAllAsyncTask(WordlibDao wordlibDao) {
            this.wordlibDao = wordlibDao;
        }
        @Override
        protected Void doInBackground(Void...voids) {
            wordlibDao.deleteAll();
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Wordlib, Void, Void> {
        private WordlibDao wordlibDao;
        public InsertAsyncTask(WordlibDao wordlibDao) {
            this.wordlibDao = wordlibDao;
        }
        @Override
        protected Void doInBackground(Wordlib...wordlibs) {
            wordlibDao.insert(wordlibs);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Wordlib, Void, Void> {
        private WordlibDao wordlibDao;
        public DeleteAsyncTask(WordlibDao wordlibDao) {
            this.wordlibDao = wordlibDao;
        }
        @Override
        protected Void doInBackground(Wordlib...wordlibs) {
            wordlibDao.delete(wordlibs);
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<Wordlib, Void, Void> {
        private WordlibDao wordlibDao;
        public UpdateAsyncTask(WordlibDao wordlibDao) {
            this.wordlibDao = wordlibDao;
        }
        @Override
        protected Void doInBackground(Wordlib...wordlibs) {
            wordlibDao.update(wordlibs);
            return null;
        }
    }

}
