package com.zpxt.zhyd.ui.report;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.HistoryAlarmDetailAdapter;
import com.zpxt.zhyd.adapter.HistoryDetailAdapter;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.HistoryAlarmListModule;
import com.zpxt.zhyd.model.HistoryListModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.views.SpaceItemDecoration;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Description:      历史数据详情
 * Autour：          LF
 * Date：            2018/3/28 17:15
 */
public class HistoryDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;
    private TextView mTitleBarTv;
    private TextView mTitleTv;
    private LinearLayout mTitleListLl;
    private TextView mQueryTv;
    private LinearLayout mDlqsbhLl;
    private LinearLayout mWdqsbhLl;

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    //处理状态
    private String[] mItems;
    //父节点id和名字
    private String mPId;
    private String mPNodeName;

    //分页参数
    private int mCurrentPage = 1;
    private int mTotalSize = 0;

    private HistoryDetailAdapter mAdapter;
    private List<HistoryListModule.HistoryModule> mList = new ArrayList();
    //折线图数据
    private HistoryListModule.ObjectModule mObjectModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        hideActionBar();
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        mBackIv.setOnClickListener(this);
        mTitleListLl.setOnClickListener(this);
        mQueryTv.setOnClickListener(this);
        mDlqsbhLl.setOnClickListener(this);
        mWdqsbhLl.setOnClickListener(this);
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPId = bundle.getString("pId");
            mPNodeName = bundle.getString("pNodeName");
        }

        mBackIv = findViewById(R.id.history_backIv);
        mTitleBarTv = findViewById(R.id.history_titleBarTv);
        mTitleTv = findViewById(R.id.history_titleTv);
        mTitleListLl = findViewById(R.id.history_titleListLl);
        mQueryTv = findViewById(R.id.history_queryTv);
        mDlqsbhLl = findViewById(R.id.history_dlqsbhLl);
        mWdqsbhLl = findViewById(R.id.history_wdqsbhLl);

        mRefreshLayout = findViewById(R.id.history_refreshLayout);
        mRecyclerView = findViewById(R.id.history_recyclerview);

        mItems = getResources().getStringArray(R.array.history_data_type);
        mTitleBarTv.setText(mPNodeName);

        mAdapter = new HistoryDetailAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置item间距
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(CommonUtils.dip2px(this, 5)));
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurrentPage = 1;
                mTotalSize = 0;
                mList.clear();
                initListData();
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
            }
        });
    }

    private void initData() {
        initListData();
    }

    private void initListData() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String endTime = format.format(calendar.getTime());

        calendar.add(Calendar.DATE, -1); //向前走一天
        Date date = calendar.getTime();
        String startTime = format.format(date);

        showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("deviceSn", mPId);//节点Id或设备编号
        parameter.put("dropState", "列表");
        parameter.put("dropType", mTitleTv.getText().toString());//查询类型  （A相电流、B相电流、C相电流、剩余电流、A相温度、B相温度、C相温度、箱体温度、全部类型）
        parameter.put("startDate", startTime);
        parameter.put("endDate", endTime);
        parameter.put("defaultCurrent", String.valueOf(mCurrentPage));
        parameter.put("pageSize", Constants.PAGE_SIZE);

        Call<HistoryListModule> call = BaseAction.newInstance(false, true).queryEleData(parameter);
        call.enqueue(new MyCallBack<HistoryListModule>(this) {
            @Override
            public void onSuccess(HistoryListModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    mObjectModule = response.getObject();
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
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }

            @Override
            public void onFail(String message) {
                cancelProgress();
                SnackbarUtil.ShortSnackbar(mRecyclerView, message, SnackbarUtil.INFO).show();
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_backIv:
                finish();
                break;
            //选择查找的类型
            case R.id.history_titleListLl:
                dialogList();
                break;
            //确定查找
            case R.id.history_queryTv:
                mCurrentPage = 1;
                mTotalSize = 0;
                mList.clear();
                initListData();
                break;
            //电流趋势
            case R.id.history_dlqsbhLl:
                Intent intent=new Intent(this, MPChartActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("objectModule",mObjectModule);
                bundle.putString("type","0");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //温度趋势
            case R.id.history_wdqsbhLl:
                Intent intent1=new Intent(this, MPChartActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putSerializable("objectModule",mObjectModule);
                bundle1.putString("type","1");
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 类型筛选框
     */
    private void dialogList() {
        showDialogList(this, mItems, "处理状态", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTitleTv.setText(mItems[i]);
                dialogInterface.dismiss();
            }
        });
    }
}
