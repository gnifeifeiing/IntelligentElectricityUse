package com.zpxt.zhyd.ui.report;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.HistoryAlarmDetailAdapter;
import com.zpxt.zhyd.adapter.RecentlyReportAdapter;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.HistoryAlarmListModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.views.SpaceItemDecoration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.SimpleFormatter;

import retrofit2.Call;

/**
 * Description:      历史告警详情
 * Autour：          LF
 * Date：            2018/3/29 9:18
 */
public class HistoryAlarmDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;
    private TextView mTitleBarTv;
    private TextView mTitleTv;
    private LinearLayout mTitleListLl;
    private TextView mQueryTv;
    private LinearLayout mStartDateLl;
    private TextView mStartDateTv;
    private LinearLayout mEndDateLl;
    private TextView mEndDateTv;

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    //处理状态
    private String[] mItems;
    //获取日期格式器对象
    Calendar calendar = Calendar.getInstance(Locale.CHINA);
    //父节点id和名字
    private String mPId;
    private String mPNodeName;

    private HistoryAlarmDetailAdapter mAdapter;
    private List<HistoryAlarmListModule.HistoryAlarmModule> mList = new ArrayList();

    //分页参数
    private int mCurrentPage=1;
    private int mTotalSize=0;

    private String mStartDate="";
    private String mEndDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_alarm_detail);
        hideActionBar();
        initView();
        initListener();
    }


    private void initListener() {
        mBackIv.setOnClickListener(this);
        mTitleListLl.setOnClickListener(this);
        mStartDateLl.setOnClickListener(this);
        mEndDateLl.setOnClickListener(this);
        mQueryTv.setOnClickListener(this);
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPId = bundle.getString("pId");
            mPNodeName = bundle.getString("pNodeName");
        }

        mBackIv = findViewById(R.id.historyAlarm_backIv);
        mTitleBarTv = findViewById(R.id.historyAlarm_titleBarTv);
        mTitleTv = findViewById(R.id.historyAlarm_titleTv);
        mTitleListLl = findViewById(R.id.historyAlarm_titleListLl);
        mQueryTv = findViewById(R.id.historyAlarm_queryTv);

        mTitleBarTv.setText(mPNodeName);

        mStartDateLl = findViewById(R.id.historyAlarm_startDateLl);
        mStartDateTv = findViewById(R.id.historyAlarm_startDateTv);
        mEndDateLl = findViewById(R.id.historyAlarm_endDateLl);
        mEndDateTv = findViewById(R.id.historyAlarm_endDateTv);

        mRefreshLayout = findViewById(R.id.historyAlarm_refreshLayout);
        mRecyclerView = findViewById(R.id.historyAlarm_recyclerview);

        mItems = getResources().getStringArray(R.array.history_alarm_type);
        mTitleTv.setText(mItems[0]);

        mAdapter = new HistoryAlarmDetailAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置item间距
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(CommonUtils.dip2px(this, 5)));
        mRecyclerView.setAdapter(mAdapter);

        //设置起止日期为：前一天至现在时刻
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String endTime=format.format(calendar.getTime());

        calendar.add(Calendar.DATE, -60); //向前走七天
        Date date = calendar.getTime();
        String startTime=format.format(date);

        mStartDateTv.setText("请选择时间");
        mEndDateTv.setText("请选择时间");

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurrentPage=1;
                mTotalSize=0;
                mList.clear();
                initListData();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(mTotalSize%Integer.parseInt(Constants.PAGE_SIZE)==0){
                    mCurrentPage++;
                    initListData();
                }else{
                    SnackbarUtil.ShortSnackbar(mRecyclerView, "已加载全部数据", SnackbarUtil.INFO).show();
                    mRefreshLayout.finishLoadMore();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentPage=1;
        mTotalSize=0;
        mList.clear();
        initData();
    }

    private void initData() {
        initListData();
    }

    private void initListData() {
        if(!mStartDateTv.getText().toString().equals("请选择时间")&&!mEndDateTv.getText().toString().equals("请选择时间")){
            if(!compareTwoDateString(mStartDateTv.getText().toString().trim(),mEndDateTv.getText().toString().trim())){
                SnackbarUtil.ShortSnackbar(mRecyclerView, "开始日期不能大于结束日期", SnackbarUtil.INFO).show();
                return;
            }else{
                mStartDate=mStartDateTv.getText().toString().trim();
                mEndDate=mStartDateTv.getText().toString().trim();
            }
        }else{
            mStartDate="";
            mEndDate="";
        }


        showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("nodeId", mPId);//节点Id
        parameter.put("state", mTitleTv.getText().toString());//数据状态值：已处理、未处理、全部状态
        parameter.put("startTime",mStartDate);
        parameter.put("endTime", mEndDate);
        parameter.put("defaultCurrent", String.valueOf(mCurrentPage));
        parameter.put("pageSize", Constants.PAGE_SIZE);

        Call<HistoryAlarmListModule> call = BaseAction.newInstance(false, true).queryAlarmList(parameter);
        call.enqueue(new MyCallBack<HistoryAlarmListModule>(this) {
            @Override
            public void onSuccess(HistoryAlarmListModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    if (response.getRows() != null && response.getRows().size() > 0) {
                        mList.addAll(response.getRows());
                        mTotalSize+=mList.size();
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
            case R.id.historyAlarm_backIv:
                finish();
                break;
            //类型选择
            case R.id.historyAlarm_titleListLl:
                dialogList();
                break;
            //查找
            case R.id.historyAlarm_queryTv:
                mCurrentPage=1;
                mTotalSize=0;
                mList.clear();
                initListData();
                break;
            //开始日期选择
            case R.id.historyAlarm_startDateLl:
                showDate(0);
                break;
            //结束日期选择
            case R.id.historyAlarm_endDateLl:
                showDate(1);
                break;
        }
    }


    /***************************************** 弹框区域 ***************************************************/
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


    /**
     * 显示日期对话框
     *
     * @param type 0：开始时间   1：结束时间
     */
    private void showDate(final int type) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //修改日历控件的年，月，日
                //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                showTime(type);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * 显示时间对话框
     *
     * @param type 0：开始时间   1：结束时间
     */
    private void showTime(final int type) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //同DatePickerDialog控件
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (type == 0) {
                    mStartDateTv.setText(format.format(calendar.getTime()));
                } else {
                    mEndDateTv.setText(format.format(calendar.getTime()));
                }

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    public View getActivityView(){
        return mStartDateTv;
    }
}
