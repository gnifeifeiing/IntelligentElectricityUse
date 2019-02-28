package com.zpxt.zhyd.ui.report;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.HistoryAlarmDetailAdapter;
import com.zpxt.zhyd.adapter.WeeklyDateListAdapter;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.HistoryAlarmListModule;
import com.zpxt.zhyd.model.WeeklyDateListModuel;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.views.SpaceItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class WeeklyDateListActivity extends BaseActivity {

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    //父节点id和名字
    private String mPId;
    private String mPNodeName;
    /**
     * 记录当前页面的类型
     * 实时数据、历史数据、检测周报、历史告警
     */
    private String mPType;

    private WeeklyDateListAdapter mAdapter;
    private List<WeeklyDateListModuel.WeeklyDateModule> mList = new ArrayList();

    //分页参数
    private int mCurrentPage = 1;
    private int mTotalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_date_list);
        setActionBarTitle("选择日期");
        initView();
        initListener();
        initData();
    }


    private void initListener() {
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPId = bundle.getString("pId");
            mPNodeName = bundle.getString("pNodeName");
            mPType = bundle.getString("pType");
        }

        mRefreshLayout = findViewById(R.id.weeklyDate_refreshLayout);
        mRecyclerView = findViewById(R.id.weeklyDate_recyclerview);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurrentPage = 1;
                mTotalSize = 0;
                mList.clear();
                initListData();
                refreshlayout.finishRefresh();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mTotalSize % Integer.parseInt(Constants.PAGE_SIZE) == 0) {
                    mCurrentPage++;
                    initListData();
                } else {
                    SnackbarUtil.ShortSnackbar(mRecyclerView, "已加载全部数据", SnackbarUtil.INFO).show();
                }
                refreshLayout.finishLoadMore();
            }
        });


        mAdapter = new WeeklyDateListAdapter(this, mList, mPId, mPNodeName, mPType);
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
        parameter.put("treeParentIds", mPId);//节点Id
        parameter.put("orgName", mPNodeName);
        parameter.put("defaultCurrent", String.valueOf(mCurrentPage));
        parameter.put("pageSize", "20");

        Call<WeeklyDateListModuel> call = BaseAction.newInstance(false, true).queryWeelyListDate(parameter);
        call.enqueue(new MyCallBack<WeeklyDateListModuel>(this) {
            @Override
            public void onSuccess(WeeklyDateListModuel response) {
                cancelProgress();
                if (response.getResultCode() != null && response.getResultCode().equals("1")) {
                    if (response.getRows() != null && response.getRows().size() > 0) {
                        mList.addAll(response.getRows());
                        mTotalSize += mList.size();
                    } else {
                        SnackbarUtil.ShortSnackbar(mRecyclerView, "没有数据", SnackbarUtil.INFO).show();
                    }
                    mAdapter.setData(mList);
                    mAdapter.notifyDataSetChanged();
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
}
