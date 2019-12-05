package com.scottmangiapane.courseevaluation.ui.my_name;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.lifecycle.ViewModel;

public class MyNameViewModel extends ViewModel {


    private MutableLiveData<String> mText;

    public MyNameViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is myname fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
