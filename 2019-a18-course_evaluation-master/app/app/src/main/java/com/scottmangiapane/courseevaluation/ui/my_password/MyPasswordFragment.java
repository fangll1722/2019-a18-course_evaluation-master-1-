package com.scottmangiapane.courseevaluation.ui.my_password;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.scottmangiapane.courseevaluation.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MyPasswordFragment extends Fragment {

    private MyPasswordViewModel myPasswordViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myPasswordViewModel = ViewModelProviders.of(this).get(MyPasswordViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myinfo, container, false);

        final TextView textView = root.findViewById(R.id.text_myinfo);
        myPasswordViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) { textView.setText(s);
            }
        });
        return root;
    }
}
