package com.scottmangiapane.courseevaluation.ui.all_courses;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scottmangiapane.courseevaluation.MainActivity;
import com.scottmangiapane.courseevaluation.R;
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


public class AllCoursesFragment extends Fragment {
    private ListView listView;
    private List<Map<String, Object>> datalist=new ArrayList<Map<String,Object>>();
//    private List<CourseModel> courseList = new ArrayList<CourseModel>();

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

    private static AllCoursesFragment acfragemnt;

    //    private AppCompatButton courseDetailBtn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_allcourses, container, false);

        listView=root.findViewById(R.id.list_view);

        emptyLayout = (EmptyLayout) root.findViewById(R.id.emptyLayout);//错误或者空布局

        context=this.getContext();//获取当前布局环境

        //刷新的Layout设置
        smartRefreshLayout = (SmartRefreshLayout) root.findViewById(R.id.refreshLayout);
        //设置 Footer 为 球脉冲 样式
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale));

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //刷新请求新数据
                isRefresh=true;
                count=3;
                getAllCourse();
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });

        //listview 加载更多数据
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                isRefresh=false;
                getAllCourse();
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            }
        });


        //开始请求数据,初始加载数据为3条
        isRefresh=true;
        count=3;
        getAllCourse();

        //获取全部课程
     //   CommonHttpUtil.requestNet(handler, URLProtocol.allcourses);

        return root;
    }

    public void getAllCourse()
    {

        //获取用户登陆信息
        //userid=((FootprintFragment)(StarFragment.this.getParentFragment())).getUserid();
        // String accountString=userid;
//        mainActivity = (MainActivity) getActivity();
//        userid = mainActivity.getUserid();
//
//        System.out.println("用户id为:"+userid);
//
//        //如果没有登录 直接显示空的页面
//        if(userid==null) {
//            emptyLayout.setEmptyMessage("您还没有登录(つ´ω`)つ喔");
//            emptyLayout.showEmpty();
//            System.out.println("用户id为:"+userid);
//        }
//
//        else {

            RequestParams requestParams = new RequestParams();
//            requestParams.add("userID", userid);

            adapter = new AllCoursesAdapter(getActivity(), datalist);


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

            AsyncUtil.post("/allcourses", requestParams, new AsyncHttpResponseHandler() {

                int list_len=count;


                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String jsonData = new String(responseBody, StandardCharsets.UTF_8);
                    System.out.println("开始获取所有的课程数据");

                    try {

                        JSONArray jsonArray = new JSONArray(jsonData);

                        //判断数据列表的长度,加载指定的数量
                        if(isRefresh&&jsonArray.length()>count)
                        {list_len=count;

                        }
                        else list_len=jsonArray.length();

                        System.out.println("当前count="+count);
                        System.out.println("当前startindex="+startIndex);
                        System.out.println("获取的课程列表长度"+list_len);

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

                        System.out.println("成功获取全部课程数据！");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println("获取全部课程数据失败！");
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

//        }

    }



}