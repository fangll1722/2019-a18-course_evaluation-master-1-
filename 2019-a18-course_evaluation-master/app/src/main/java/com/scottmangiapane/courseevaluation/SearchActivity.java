package com.scottmangiapane.courseevaluation;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;


import androidx.appcompat.app.AppCompatActivity;

import com.scottmangiapane.courseevaluation.ui.all_courses.AllCoursesAdapter;
import com.scottmangiapane.courseevaluation.ui.all_courses.CheckUtils;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scottmangiapane.courseevaluation.CourseDetailActivity;
import com.scottmangiapane.courseevaluation.MainActivity;
import com.scottmangiapane.courseevaluation.R;
import com.scottmangiapane.courseevaluation.ui.all_courses.AllCoursesFragment;
import com.scottmangiapane.courseevaluation.AsyncUtil;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import name.quanke.app.libs.emptylayout.EmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import name.quanke.app.libs.emptylayout.EmptyLayout;



public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private String key=null;
    private String type="0";
    private Button zykBtn;
    private Button ggkBtn;
    private Button gxkBtn;
    private Button fzskBtn;

//    private ListView list_view;
//    private List<CourseModel> courseList = new ArrayList<CourseModel>();
//    private CourseListAdapter adapter;

    private ListView listView;
    private List<Map<String, Object>> datalist=new ArrayList<Map<String,Object>>();

    private EmptyLayout emptyLayout;//空页面/错误页面布局
    private Context context;
    private SmartRefreshLayout smartRefreshLayout;//刷新布局

    //通用适配器
    private AllCoursesAdapter adapter;

    //从mainactivity 获取用户的id
    private static String userid;

    MainActivity mainActivity;

    //每次加载数据的条数
    private static int count=3;
    //起始的位置
    private  static  int startIndex=0;
    //是否刷新数据
    private static boolean isRefresh;

    private static SearchActivity sfragemnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        searchView=findViewById(R.id.searchView);
        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                //获取指定条件课程
//                if(TextUtils.isEmpty(key)){
//                    CommonHttpUtil.requestNet(handler, URLProtocol.searchcourse,"type="+type);
//                } else {
//                    CommonHttpUtil.requestNet(handler, URLProtocol.searchcourse,"type="+type,"keyword="+key);
//                }
                return true;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                int color0 = ((ColorDrawable)zykBtn.getBackground()).getColor();
                int color1 = ((ColorDrawable)ggkBtn.getBackground()).getColor();
                int color2 = ((ColorDrawable)gxkBtn.getBackground()).getColor();
                int color3 = ((ColorDrawable)fzskBtn.getBackground()).getColor();

                //找出绿色按钮
                if(color0==color1){
                    if(color2==color0){
                        type="3";
                    } else {
                        type="2";
                    }
                } else {
                    if(color0==color2){
                        type="1";
                    } else {
                        type="0";
                    }
                }

                //获取指定条件课程
                key=searchView.getQuery().toString();
                if(TextUtils.isEmpty(key)){
//                    CommonHttpUtil.requestNet(handler, URLProtocol.searchcourse,"type="+type);
                    getSearchCourse(type,null);
                } else {
//                    CommonHttpUtil.requestNet(handler, URLProtocol.searchcourse,"type="+type,"keyword="+key);
                    getSearchCourse(type,key);
                }
                return true;
            }
        });

        //专业课-按钮
        zykBtn=findViewById(R.id.zykBtn);zykBtn.setTag("0");
        zykBtn.setOnClickListener(selectBtnListener);
        zykBtn.setBackgroundColor(getResources().getColor(R.color.green));
        //公共课-按钮
        ggkBtn=findViewById(R.id.ggkBtn);ggkBtn.setTag("1");
        ggkBtn.setOnClickListener(selectBtnListener);
        //公选课-按钮
        gxkBtn=findViewById(R.id.gxkBtn);gxkBtn.setTag("2");
        gxkBtn.setOnClickListener(selectBtnListener);
        //非正式课-按钮
        fzskBtn=findViewById(R.id.fzskBtn);fzskBtn.setTag("3");
        fzskBtn.setOnClickListener(selectBtnListener);

        zykBtn.setBackgroundColor(getResources().getColor(R.color.green));
        ggkBtn.setBackgroundColor(getResources().getColor(R.color.grey_transparent));
        gxkBtn.setBackgroundColor(getResources().getColor(R.color.grey_transparent));
        fzskBtn.setBackgroundColor(getResources().getColor(R.color.grey_transparent));

        //获取全部课程
        //CommonHttpUtil.requestNet(handler, URLProtocol.allcourses);

        listView=findViewById(R.id.list_view);

        emptyLayout = (EmptyLayout) findViewById(R.id.emptyLayout);//错误或者空布局

        context=getBaseContext();//获取当前布局环境

        //刷新的Layout设置
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        //设置 Footer 为 球脉冲 样式
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale));

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //刷新请求新数据
                isRefresh=true;
                count=3;
                getSearchCourse(type,key);
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });

        //listview 加载更多数据
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                isRefresh=false;
                getSearchCourse(type,key);
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            }
        });

        //开始请求数据,初始加载数据为3条
        isRefresh=true;
        count=3;
        getSearchCourse(type,key);


    }


    public void getSearchCourse(String type,String keyword)
    {

        //获取用户登陆信息
        //userid=((FootprintFragment)(StarFragment.this.getParentFragment())).getUserid();
        // String accountString=userid;



            adapter = new AllCoursesAdapter(getBaseContext(), datalist);


            if(isRefresh==true){
                datalist.clear();
                startIndex=0;
                System.out.println("*************刷新数据*****************");
            }
            //刷新数据的时候先清空列表
            //if(isRefresh==true&&datalist.size()!=0) {
            //    datalist.clear();
            //  startIndex=0;
            // System.out.println("*************刷新数据*****************");
            // }
            //加载数据不用清空列表
            else{
                System.out.println("*************加载更多数据*****************");
            }


            //设置适配器adpter
            listView.setAdapter(adapter);

            RequestParams requestParams = new RequestParams();
//            requestParams.add("userID", userid);
            if(!CheckUtils.isEmpty(type)){
                requestParams.add("type", type);
            }
             if(!CheckUtils.isEmpty(keyword)){
                  requestParams.add("keyword", keyword);
             }

             AsyncUtil.post("/searchcourse", requestParams, new AsyncHttpResponseHandler() {

                int list_len=count;

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String jsonData = new String(responseBody, StandardCharsets.UTF_8);
                    System.out.println("开始获取符合条件的课程数据");

                    try {

                        JSONArray jsonArray = new JSONArray(jsonData);

                        //判断数据列表的长度,加载指定的数量
                        if(isRefresh&&jsonArray.length()>count)
                        {list_len=count;

                        }
                        else list_len=jsonArray.length();

                        System.out.println("当前count="+count);
                        System.out.println("当前startindex="+startIndex);
                        System.out.println("获取的符合条件的课程列表长度"+list_len);

                        for (int i = startIndex; i < list_len; i++) {

                            final JSONObject jsonObject = jsonArray.getJSONObject(i);//传给对应的activity


                            String comment_num = jsonObject.getString("com_num");
                            String detail = jsonObject.getString("detail");
                            String name = jsonObject.getString("name");
                            String teacher = jsonObject.getString("teacher");
                            String score = jsonObject.getString("score");
                            String academy =jsonObject.getString("academy");

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("image", R.drawable.ic_announcement);
                            map.put("title", name);
                            map.put("academy", academy);
                            map.put("teacher", teacher);
                            map.put("description", detail);
                            map.put("detail_btn", R.id.courseBtn1);
                            map.put("ratingnumber",score);
                            map.put("comment_num",comment_num);

                            //传入json的值
                            map.put("jsondata",jsonObject);


                            datalist.add(map);

                            Log.d("collect json item", "jsondata is " + jsonObject.toString());


                        }

                        //该用户没有数据
                        if(isRefresh&&datalist.size()==0){
                            //  emptyLayout.setEmptyMessage("您还没有数据喔(￣ε(#￣)" );
                            //emptyLayout.showEmpty();//设置空页面
                            adapter.notifyDataSetChanged();//调整刷新的数据
                        }
                        else {
                            adapter.notifyDataSetChanged();//调整刷新的数据
                        }
                        //每次刷新固定显示3条，每次加载增加1条
                        if(isRefresh)startIndex+=3;
                        else{
                            startIndex+=1;
                        }

                        System.out.println("成功获取符合条件的课程数据！");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println("获取符合条件的课程数据失败！");
                    emptyLayout.setErrorMessage("(｡ŏ_ŏ)检查下你的网络啦～");
                    emptyLayout.setErrorDrawable(R.drawable.network_error);
                    emptyLayout.showError();
                    emptyLayout.setErrorButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //尝试重新获取数据
                        }
                    });
                    //显示错误提示页面 --没有网络链接，错误等
                }
            });

    }

    View.OnClickListener selectBtnListener =new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            zykBtn.setBackgroundColor(getResources().getColor(R.color.grey_transparent));
            ggkBtn.setBackgroundColor(getResources().getColor(R.color.grey_transparent));
            gxkBtn.setBackgroundColor(getResources().getColor(R.color.grey_transparent));
            fzskBtn.setBackgroundColor(getResources().getColor(R.color.grey_transparent));
            v.setBackgroundColor(getResources().getColor(R.color.green));
            String tag=v.getTag().toString();
            type=tag;
            key=searchView.getQuery().toString();
            //获取指定条件课程
            if(TextUtils.isEmpty(key)){
//                CommonHttpUtil.requestNet(handler, URLProtocol.searchcourse,"type="+tag);
                getSearchCourse(tag,null);
            } else {
//                CommonHttpUtil.requestNet(handler, URLProtocol.searchcourse,"type="+tag,"keyword="+key);
                getSearchCourse(tag,key);
            }
        }
    };
}
