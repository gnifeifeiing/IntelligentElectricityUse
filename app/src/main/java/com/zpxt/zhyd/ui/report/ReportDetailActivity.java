package com.zpxt.zhyd.ui.report;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.ZoneReportListAdapter;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.views.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:      厂-告警详情
 * Autour：          LF   
 * Date：            2018/3/22 17:09
 */ 
public class ReportDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

//    private ReportDetailAdapter mAdapter;
    private List<Map<String,String>> mList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        hideActionBar();
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        mBackIv.setOnClickListener(this);
    }

    private void initView() {
        mBackIv=findViewById(R.id.detail_backIv);
        mRefreshLayout=findViewById(R.id.detail_refreshLayout);
        mRecyclerView=findViewById(R.id.detail_recyclerview);
    }

    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("title","A相电流");
        mList.add(map);
        map=new HashMap<>();
        map.put("title","A相温度");
        mList.add(map);
        map=new HashMap<>();
        map.put("title","剩余电量");
        mList.add(map);
        map=new HashMap<>();
        map.put("title","零线温度");
        mList.add(map);
//
//        mAdapter=new ReportDetailAdapter(this,mList);
//        mRecyclerView .setLayoutManager(new LinearLayoutManager(this));
//        //设置item间距
//        mRecyclerView.addItemDecoration(new SpaceItemDecoration(CommonUtils.dip2px(this,5)));
//        mRecyclerView.setAdapter(mAdapter);
//
//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000);
//            }
//        });
//        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishLoadMore(2000);
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.zoneReport_backIv:
                finish();
                break;
        }
    }
}
