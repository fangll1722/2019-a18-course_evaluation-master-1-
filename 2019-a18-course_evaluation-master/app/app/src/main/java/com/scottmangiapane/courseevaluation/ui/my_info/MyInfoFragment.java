package com.scottmangiapane.courseevaluation.ui.my_info;


import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.scottmangiapane.courseevaluation.CourseDetailActivity;

import com.scottmangiapane.courseevaluation.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MyInfoFragment extends Fragment {

    private MyInfoViewModel myInfoViewModel;
    private Button btn_mymajor, btn_myname, btn_mypassword, btn_setting, btn_about;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myInfoViewModel = ViewModelProviders.of(this).get(MyInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myinfo, container, false);

        final TextView username = root.findViewById(R.id.tv_username);
        myInfoViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                username.setText(s);
            }
        });

        btn_mymajor = root.findViewById(R.id.btn_mymajor);
        btn_myname = root.findViewById(R.id.bt_myname);
        btn_mypassword = root.findViewById(R.id.bt_mypassword);
        btn_setting = root.findViewById(R.id.bt_setting);
        btn_about = root.findViewById(R.id.bt_about);


        btn_mymajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CourseDetailActivity.class);
                startActivity(intent);
            }
        });

        btn_myname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyNameActivity.class);
                Bundle bundle = new Bundle();
                String queryResultStr="听见声音";
                bundle.putString("queryresult",queryResultStr); //放入所需要传递的值
                intent.putExtras(bundle);
                getActivity().startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
                // startActivity(intent);
            }
        });

        btn_mypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyPasswordActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }

}
