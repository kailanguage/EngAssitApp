package com.kailang.engassit.ui.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kailang.engassit.data.entity.Stat;
import com.kailang.engassit.data.entity.User;
import com.kailang.engassit.data.entity.Wordlib;
import com.kailang.engassit.data.repository.SentenceRep;
import com.kailang.engassit.data.repository.StatRep;
import com.kailang.engassit.data.repository.UserRep;
import com.kailang.engassit.data.repository.WordRep;
import com.kailang.engassit.data.repository.WordlibRep;

import java.util.List;

import static com.kailang.engassit.data.Sync.currentUser;

public class LoginViewModel extends AndroidViewModel {
    //private UserRep userRep;
    private StatRep statRep;
    private WordRep wordRep;
    private WordlibRep wordlibRep;
    private SentenceRep sentenceRep;
    private MutableLiveData<Boolean> isRegSuccess;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        //userRep=new UserRep(application);
        statRep=new StatRep(application);
        wordRep=new WordRep(application);
        wordlibRep=new WordlibRep(application);
        sentenceRep=new SentenceRep(application);
        isRegSuccess=new MutableLiveData<>(Boolean.FALSE);
    }
//    public  void insertUser(User user){
//        userRep.insertUser(user);
//    }
    public void initAll(){
        //userRep.deleteAllUser();
        statRep.deleteAllStat();
        wordRep.deleteAllWords();
        wordlibRep.deleteAllWordlibs();
        sentenceRep.deleteAllSentence();
    }

    public MutableLiveData<Boolean> getIsRegSuccess() {
        return isRegSuccess;
    }

    public void setIsRegSuccess(Boolean isRegSuccess) {
        this.isRegSuccess.postValue(isRegSuccess);//非主线程调用用post
    }
//    public LiveData<List<User>> getAllUser(){
//        return userRep.getAllUser();
//    }

}
