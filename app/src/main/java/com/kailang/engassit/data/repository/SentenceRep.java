package com.kailang.engassit.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kailang.engassit.data.dao.SentenceDao;
import com.kailang.engassit.data.entity.Sentence;
import com.kailang.engassit.data.entity.Word;

import java.util.List;

public class SentenceRep {
    private LiveData<List<Sentence>> allWords;
    private SentenceDao sentenceDao;
    private Context context;
    public SentenceRep(Context context){
        this.context=context;
        SentenceDatabase sentenceDatabase = SentenceDatabase.getDatabase(context);
        sentenceDao=sentenceDatabase.getSentenceDao();
        allWords=sentenceDao.getAllSentences();
    }

    public LiveData<List<Sentence>>findWithPattern(String pattern){
        return sentenceDao.findWithPattern("%" + pattern + "%");
    }
    public LiveData<List<Sentence>> getAllSentence(){
        return allWords;
    }

    public void insertSentence(Sentence...sentences) {
        new InsertAsyncTask(sentenceDao).execute(sentences);
    }
    public void deleteSentence(Sentence...sentences) {
        new DeleteAsyncTask(sentenceDao).execute(sentences);
    }
    public void deleteAllSentence() {
        new DeleteAllAsyncTask(sentenceDao).execute();
    }

    public void updateWord(Sentence...sentences) {
        new UpdateAsyncTask(sentenceDao).execute(sentences);
    }

    private static class UpdateAsyncTask extends AsyncTask<Sentence, Void, Void> {
        private SentenceDao sentenceDao;

        public UpdateAsyncTask(SentenceDao sentenceDao ) {
            this.sentenceDao = sentenceDao;
        }

        @Override
        protected Void doInBackground(Sentence...sentences) {
            sentenceDao.update(sentences);
            return null;
        }
    }


    private static class InsertAsyncTask extends AsyncTask<Sentence, Void, Void> {
        private SentenceDao sentenceDao;

        public InsertAsyncTask(SentenceDao sentenceDao) {
            this.sentenceDao = sentenceDao;
        }

        @Override
        protected Void doInBackground(Sentence...sentences) {
            sentenceDao.insert(sentences);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Sentence, Void, Void> {
        private SentenceDao sentenceDao;

        public DeleteAsyncTask(SentenceDao sentenceDao) {
            this.sentenceDao = sentenceDao;
        }

        @Override
        protected Void doInBackground(Sentence...sentences) {
            sentenceDao.delete(sentences);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private SentenceDao sentenceDao;

        public DeleteAllAsyncTask(SentenceDao sentenceDao) {
            this.sentenceDao = sentenceDao;
        }

        @Override
        protected Void doInBackground(Void...voids) {
            sentenceDao.deleteAll();
            return null;
        }
    }
}
