package com.scottmangiapane.courseevaluation.ui.course_detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CourseDetailViewModel extends ViewModel {

    private MutableLiveData<String> coursename;


    public CourseDetailViewModel() {
        coursename = new MutableLiveData<>();
        coursename.setValue("操作系统");
    }

    public LiveData<String> getText() {
        return coursename;
    }
}
