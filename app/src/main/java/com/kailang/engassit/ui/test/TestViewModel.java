package com.kailang.engassit.ui.test;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kailang.engassit.data.Sync;
import com.kailang.engassit.data.entity.Word;
import com.kailang.engassit.data.entity.Wordlib;
import com.kailang.engassit.data.repository.WordRep;
import com.kailang.engassit.data.repository.WordlibRep;

import java.util.List;

public class TestViewModel extends AndroidViewModel {
    WordlibRep wordlibRep;
    WordRep wordRep;
    Sync sync;
    private MutableLiveData<Integer> rightCount;
    public TestViewModel(@NonNull Application application) {
        super(application);
        wordlibRep=new WordlibRep(application);
        wordRep=new WordRep(application);
        sync=new Sync(application);
        rightCount=new MutableLiveData<>(0);
    }
    public LiveData<List<Wordlib>> getWordlibByLevel(short level){
        return wordlibRep.getWorlibByLevel(level);
    }
    public LiveData<List<Wordlib>> getAllWordlib(){
        return wordlibRep.getAllWorlibs();
    }
    public LiveData<List<Word>> getAllWords(){
        return wordRep.getAllWords();
    }

    public void downWordlibByLevel(short level){
        wordlibRep.deleteAllWordlibs();
        sync.downWordlibByLevel((short)level);
    }
    public void downAllWordlib(){
        wordlibRep.deleteAllWordlibs();
        sync.downAllWordlib();
    }

    public MutableLiveData<Integer> getRightCount() {
        return rightCount;
    }

    public void setRightCount(Integer rightCount) {
        this.rightCount.postValue(rightCount);
    }
}
