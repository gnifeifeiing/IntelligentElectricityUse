package com.zpxt.zhyd.ui.report;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.RealTimeDealAdviceModule;
import com.zpxt.zhyd.model.RealTimeListModule;
import com.zpxt.zhyd.model.RecentlyReportListModule;
import com.zpxt.zhyd.model.RecentlyReportModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.views.DealWithAdviceDialog;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Description:      最近告警详情
 * Autour：          LF
 * Date：            2018/3/21 17:43
 */
public class RecentlyReportDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;

    private TextView mOrgNameTv;
    private TextView mSensorNameTv;
    private TextView mDataTv;
    private TextView mBoxNameTv;
    private TextView mNodeIdTv;
    private TextView mDateTimeTv;
    private EditText mAdviceEt;

    private Button mDealWithBtn;

    private RecentlyReportModule mRecentlyReportModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_report_detail);
        hideActionBar();
        initView();
        initListener();
        initData();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRecentlyReportModule = (RecentlyReportModule) bundle.getSerializable("recentlyReportModule");
        }

        mBackIv = findViewById(R.id.reportDetail_backIv);

        mOrgNameTv = findViewById(R.id.reportDetail_orgNameTv);
        mSensorNameTv = findViewById(R.id.reportDetail_sensorNameTv);
        mDataTv = findViewById(R.id.reportDetail_dataTv);
        mBoxNameTv = findViewById(R.id.reportDetail_boxNameTv);
        mNodeIdTv = findViewById(R.id.reportDetail_nodeIDTv);
        mDateTimeTv = findViewById(R.id.reportDetail_dateTimeTv);
        mAdviceEt = findViewById(R.id.reportDetail_adviceEt);

        mDealWithBtn = findViewById(R.id.reportDetail_dealWithBtn);
    }

    private void initListener() {
        mBackIv.setOnClickListener(this);
        mDealWithBtn.setOnClickListener(this);
    }

    private void initData() {
        mOrgNameTv = findViewById(R.id.reportDetail_orgNameTv);
        mSensorNameTv = findViewById(R.id.reportDetail_sensorNameTv);
        mDataTv = findViewById(R.id.reportDetail_dataTv);
        mBoxNameTv = findViewById(R.id.reportDetail_boxNameTv);
        mNodeIdTv = findViewById(R.id.reportDetail_nodeIDTv);
        mDateTimeTv = findViewById(R.id.reportDetail_dateTimeTv);
        ;
        if(mRecentlyReportModule.getOrgName()!=null){
            mOrgNameTv.setText(mRecentlyReportModule.getOrgName());
        }
        if(mRecentlyReportModule.getSensorName()!=null){
            mSensorNameTv.setText(mRecentlyReportModule.getSensorName());
        }
        if(mRecentlyReportModule.getData()!=null){
            mDataTv.setText(mRecentlyReportModule.getData());
        }
        if(mRecentlyReportModule.getDistributionBox()!=null){
            mBoxNameTv.setText(mRecentlyReportModule.getDistributionBox());
        }
        if(mRecentlyReportModule.getNodeID()!=null){
            mNodeIdTv.setText(mRecentlyReportModule.getNodeID());
        }
        if(mRecentlyReportModule.getGenerationTimedata()!=null){
            mDateTimeTv.setText(mRecentlyReportModule.getGenerationTimedata());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.reportDetail_backIv:
                finish();
                break;
            //处理
            case R.id.reportDetail_dealWithBtn:
                dealRequest();
                break;
        }
    }

    /**
     * 处理意见
     */
    private void dealRequest() {
        if (TextUtils.isEmpty(mAdviceEt.getText().toString().trim())) {
            SnackbarUtil.ShortSnackbar(mOrgNameTv, "请输入处理信息", SnackbarUtil.INFO).show();
            return;
        }
        showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("alarm", "1");
        parameter.put("deviceSn", mRecentlyReportModule.getNodeID()==null?"":mRecentlyReportModule.getNodeID());
        parameter.put("treatmentSuggestion", mAdviceEt.getText().toString().trim());
        parameter.put("pointTime", mRecentlyReportModule.getGenerationTimedata()==null?"":mRecentlyReportModule.getGenerationTimedata());
        parameter.put("pointValue", mRecentlyReportModule.getData()==null?"":mRecentlyReportModule.getData());
        parameter.put("pointSn", mRecentlyReportModule.getSensorName()==null?"":mRecentlyReportModule.getSensorName());
        parameter.put("gatewaySn", mRecentlyReportModule.getNodeID()==null?"":mRecentlyReportModule.getNodeID());
        parameter.put("treatmentState", "1");

        Call<RealTimeDealAdviceModule> call = BaseAction.newInstance(false, true).saveDealWithAdvice(parameter);
        call.enqueue(new MyCallBack<RealTimeDealAdviceModule>(this) {
            @Override
            public void onSuccess(RealTimeDealAdviceModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    Toast.makeText(RecentlyReportDetailActivity.this,"已提交",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    SnackbarUtil.ShortSnackbar(mOrgNameTv, "提交失败", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                cancelProgress();
                SnackbarUtil.ShortSnackbar(mOrgNameTv, message, SnackbarUtil.INFO).show();
            }
        });
    }
}
