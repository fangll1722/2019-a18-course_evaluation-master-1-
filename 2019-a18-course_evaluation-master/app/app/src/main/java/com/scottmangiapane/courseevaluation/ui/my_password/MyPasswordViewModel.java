package com.scottmangiapane.courseevaluation.ui.my_password;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.lifecycle.ViewModel;

public class MyPasswordViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyPasswordViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mypassword fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
