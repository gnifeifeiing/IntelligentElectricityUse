package com.zpxt.zhyd.ui.report;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.AreaSecReportListAdapter;
import com.zpxt.zhyd.adapter.ZoneReportListAdapter;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.views.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Description:      厂房状态列表
 * Autour：          LF
 * Date：            2018/3/22 16:38
 */
public class ZoneReportListActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;
    private TextView mNodeNameTv;

    public RecyclerView mRecyclerView;

    private ZoneReportListAdapter mAdapter;
    private List<AlarmListModule.AlarmModule> mList = new ArrayList<>();

    //父节点id和名字
    private String mPId;
    private String mPNodeName;
    /**
     * 记录当前页面的类型
     * 实时数据、历史数据、检测周报、历史告警
     */
    private String mPType;
    private String mStartTime = "";
    private String mEndTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_report_list);
        hideActionBar();
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        mBackIv.setOnClickListener(this);
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPId = bundle.getString("pId");
            mPNodeName = bundle.getString("pNodeName");
            mPType = bundle.getString("pType");
            //检测周报日期列表
            if (mPType.equals(getResources().getStringArray(R.array.alarm_detail_type)[2])) {
                mStartTime = bundle.getString("startTime");
                mEndTime = bundle.getString("endTime");
            }
        }

        mBackIv = findViewById(R.id.zoneReport_backIv);
        mNodeNameTv = findViewById(R.id.zoneReport_nodeNameTv);
        mRecyclerView = findViewById(R.id.zoneReport_recyclerview);

        mNodeNameTv.setText(mPNodeName);

        mAdapter = new ZoneReportListAdapter(this, mList, mPType);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置item间距
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(CommonUtils.dip2px(this, 5)));
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initData() {
        initListData();
    }

    private void initListData() {
        showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("parentId", mPId);
        Call<AlarmListModule> call = BaseAction.newInstance(false, true).getNodeList(parameter);
        call.enqueue(new MyCallBack<AlarmListModule>(this) {
            @Override
            public void onSuccess(AlarmListModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    if (response.getRows() != null && response.getRows().size() > 0) {
                        mList = response.getRows();
                        mAdapter.setData(mList);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        SnackbarUtil.ShortSnackbar(mRecyclerView, "没有数据", SnackbarUtil.INFO).show();
                    }
                } else {
                    SnackbarUtil.ShortSnackbar(mRecyclerView, "获取失败", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                cancelProgress();
                SnackbarUtil.ShortSnackbar(mRecyclerView, message, SnackbarUtil.INFO).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zoneReport_backIv:
                finish();
                break;
        }
    }

    public View getActivityView() {
        return mNodeNameTv;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public String getEndTime() {
        return mEndTime;
    }
}
