package com.kailang.engassit.ui.sentence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class TranslateViewModel extends AndroidViewModel {
    private MutableLiveData<String> result;

    public TranslateViewModel(@NonNull Application application) {
        super(application);
        result=new MutableLiveData<>();
    }

    public MutableLiveData<String> getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result.postValue(result);
    }
}
