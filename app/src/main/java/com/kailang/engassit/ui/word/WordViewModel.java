package com.kailang.engassit.ui.word;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kailang.engassit.data.Sync;
import com.kailang.engassit.data.entity.Word;
import com.kailang.engassit.data.repository.WordRep;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRep wordRep;
    private Sync sync;
    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRep=new WordRep(application);
        sync= new Sync(application);
    }

    public void deleteAllSync(){
        wordRep.deleteAllWords();
        sync.downAllWord();
    }

    public void deleteWord(Word word){
        sync.deletetWord(word);
        wordRep.deleteWord(word);
    }

    public LiveData<List<Word>> getAllWords() {
        return wordRep.getAllWords();
    }

    public LiveData<List<Word>> findWordWithPattern(String pattern){
        return wordRep.findwordWithPattern(pattern);
    }

    public void insertWord(Word wordToDelete) {
        wordRep.insertWord(wordToDelete);
        sync.insertWord(wordToDelete);
    }

    public void updateWord(Word word){
        wordRep.updateWord(word);
        sync.updateWord(word);
    }
}