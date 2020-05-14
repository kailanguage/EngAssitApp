package com.kailang.engassit.ui.person;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kailang.engassit.data.Sync;
import com.kailang.engassit.data.entity.Stat;
import com.kailang.engassit.data.entity.User;
import com.kailang.engassit.data.repository.StatRep;
import com.kailang.engassit.data.repository.UserRep;

import java.util.List;

public class PersonViewModel extends AndroidViewModel {

    private StatRep statRep;
    private Sync sync;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        statRep=new StatRep(application);
        sync=new Sync(application);
    }

    public void check(Stat s){
        statRep.updateStat(s);
        sync.updateStat(s);

    }
    public LiveData<List<Stat>> getStat(){
        return statRep.getAllStat();
    }


    public void deleteAllStat(){
        statRep.deleteAllStat();
        sync.getStat();
    }

    public void insertStat(Stat s ){
        statRep.deleteAllStat();
        statRep.insertStat(s);
        //sync.insertStat();
    }
}