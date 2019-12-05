package com.scottmangiapane.courseevaluation.ui.my_major;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyMajorViewModel extends ViewModel{

    private MutableLiveData<String> mText;

    public MyMajorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mymajor fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
