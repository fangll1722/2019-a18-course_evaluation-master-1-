package com.scottmangiapane.courseevaluation.ui.course_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.scottmangiapane.courseevaluation.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class CourseDetailFragment extends Fragment {

    private CourseDetailViewModel courseDetailViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        courseDetailViewModel =
                ViewModelProviders.of(this).get(CourseDetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_coursedetail, container, false);
        final EditText coursename=root.findViewById(R.id.courseTitle);

        courseDetailViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                coursename.setText(s);
            }
        });
        return root;
    }
}
