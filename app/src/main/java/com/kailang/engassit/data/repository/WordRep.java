package com.kailang.engassit.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kailang.engassit.data.dao.WordDao;
import com.kailang.engassit.data.entity.Word;

import java.util.List;

public class WordRep  {
    private LiveData<List<Word>> allWords;
    private WordDao wordDao;
    private Context context;
    public WordRep(Context context){
        this.context=context;
        WordDatabase wordDatabase = WordDatabase.getDatabase(context);
        wordDao=wordDatabase.getWordDao();
        allWords=wordDao.getAllWords();
    }

    public LiveData<List<Word>>findwordWithPattern(String pattern){
        return wordDao.findWithPattern("%" + pattern + "%");
    }
    public LiveData<List<Word>> getAllWords(){
        return allWords;
    }

    public void insertWord(Word...words) {
        new InsertAsyncTask(wordDao).execute(words);
    }
    public void deleteWord(Word...words) {
        new DeleteAsyncTask(wordDao).execute(words);
    }

    public void deleteAllWords() {
        new DeleteAllAsyncTask(wordDao).execute();
    }

    public void updateWord(Word...words) {
        new UpdateAsyncTask(wordDao).execute(words);
    }

    private static class UpdateAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word...words) {
            wordDao.update(words);
            return null;
        }
    }


    private static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word...words) {
            wordDao.insert(words);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word...words) {
            wordDao.delete(words);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao wordDao;

        public DeleteAllAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void ...voids) {
            wordDao.deleteAll();
            return null;
        }
    }
}
