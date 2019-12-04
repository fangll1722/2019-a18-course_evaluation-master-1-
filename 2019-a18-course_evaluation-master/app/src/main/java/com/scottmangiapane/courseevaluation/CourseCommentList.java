package com.scottmangiapane.courseevaluation;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseCommentList extends LinearLayout implements View.OnClickListener {

    private ImageView iv_head;
    private TextView tv_username,tv_commentdate,tv_myscore,tv_mycomment;
    private Button btn_coursecollect,btn_like;
    private Context context;

    public CourseCommentList(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.course_comment_list,this);//第二个参数必须传this
        initView();
        initEvent();
        this.context=context;
    }

    private void initEvent() {
        iv_head.setImageResource(R.drawable.img_myhead);
        tv_myscore.setText("5分");
        tv_mycomment.setText("老师人很好，很少作业");
        tv_commentdate.setText("2018-12-12");
        tv_username.setText("听见下雨的声音");
        btn_coursecollect.setOnClickListener(this);
        btn_like.setOnClickListener(this);
    }

    private void initView() {
        iv_head=(ImageView)findViewById(R.id.iv_head);
        tv_username=(TextView)findViewById(R.id.tv_username);
        tv_commentdate=(TextView)findViewById(R.id.tv_commentdate);
        tv_myscore=(TextView)findViewById(R.id.tv_myscore);
        tv_mycomment=(TextView)findViewById(R.id.tv_mycomment);
        btn_coursecollect=(Button)findViewById(R.id.btn_coursecollect);
        btn_like=(Button)findViewById(R.id.btn_like);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_coursecollect:
                btn_coursecollect.setEnabled(false);
                break;
            case R.id.btn_like:
                btn_like.setEnabled(false);
                break;
        }
    }


}
