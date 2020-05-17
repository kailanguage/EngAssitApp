package com.kailang.engassit.ui.sentence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kailang.engassit.data.Sync;
import com.kailang.engassit.data.entity.Sentence;
import com.kailang.engassit.data.repository.SentenceRep;

import java.util.List;

public class SentenceViewModel extends AndroidViewModel {

    private SentenceRep sentanceRep;
    private Sync sync;

    public SentenceViewModel(@NonNull Application application) {
        super(application);
        sentanceRep = new SentenceRep(application);
        sync= new Sync(application);
    }

    public LiveData<List<Sentence>> findWordWithPattern(String pattern) {
        return sentanceRep.findWithPattern(pattern);
    }

    public void deleteAllSync() {
        sentanceRep.deleteAllSentence();
        sync.downAllSentence();
    }

    public LiveData<List<Sentence>> getAllSentence() {
        return sentanceRep.getAllSentence();
    }

    public void insertSentence(Sentence sentence) {
        sentanceRep.insertSentence(sentence);
        sync.inserSentence(sentence);
    }

    public void deleteSentence(Sentence sentence) {
        sentanceRep.deleteSentence(sentence);
        sync.deleteSentence(sentence);
    }

    public void updateSentence(Sentence sentence){
        sentanceRep.updateWord(sentence);
        sync.updateSentence(sentence);
    }
}