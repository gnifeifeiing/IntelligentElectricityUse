package com.zpxt.zhyd.ui.report;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.HistoryAlarmListModule;
import com.zpxt.zhyd.model.WeeklyDetailModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;

/**
 * Description:      检测周报详情
 * Autour：          LF
 * Date：            2018/3/28 16:58
 */
public class WeeklyDetailAvtivity extends BaseActivity{

    private TextView mJcTitleTv;
    private TextView mDateTv;
    //监测报告
    private TextView mIrNum;
    private TextView mEleIaNum;
    private TextView mEleTNum;
    private TextView mTxNum;
    //报警次数
    private TextView mIrNumCS;
    private TextView mEleIaNumCS;
    private TextView mEleTNumCS;
    private TextView mTxNumCS;
    private TextView mMaxNumCS;
    //备注
    private TextView mNoteTv;

    //父节点id和名字
    private String mPId;
    private String mPNodeName;
    private String mStartTime = "";
    private String mEndTime = "";
    private String mTreeParentIds = "";

    //获取日期格式器对象
    Calendar calendar = Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_detail_avtivity);
        setActionBarTitle("检测周报");
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
            mStartTime = bundle.getString("startTime");
            mEndTime = bundle.getString("endTime");
            mTreeParentIds = bundle.getString("treeParentIds");
        }

        mJcTitleTv = findViewById(R.id.weeklyDetail_jcTitleTv);
        mDateTv = findViewById(R.id.weeklyDetail_dateTv);

        mIrNum = findViewById(R.id.weeklyDetail_irNumTv);
        mEleIaNum = findViewById(R.id.weeklyDetail_eleIaNumTv);
        mEleTNum = findViewById(R.id.weeklyDetail_eleTNumTv);
        mTxNum = findViewById(R.id.weeklyDetail_txNumTv);

        mIrNumCS = findViewById(R.id.weeklyDetail_irNumCSTv);
        mEleIaNumCS = findViewById(R.id.weeklyDetail_eleIaNumCSTv);
        mEleTNumCS = findViewById(R.id.weeklyDetail_eleTNumCSTv);
        mTxNumCS = findViewById(R.id.weeklyDetail_txNumCSTv);
        mMaxNumCS = findViewById(R.id.weeklyDetail_maxNumCSTv);

        mNoteTv = findViewById(R.id.weeklyDetail_noteTv);

        mJcTitleTv.setText(mPNodeName);
        mDateTv.setText(mStartTime.replace("-",".")+"-"+mEndTime.replace("-","."));
    }

    private void  initData(){
        showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("orgName", mPNodeName);
        parameter.put("startTime", mStartTime);
        parameter.put("endTime", mEndTime);

        Call<WeeklyDetailModule> call = BaseAction.newInstance(false, true).queryWeelyData(parameter);
        call.enqueue(new MyCallBack<WeeklyDetailModule>(this) {
            @Override
            public void onSuccess(WeeklyDetailModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    setData(response);
                } else {
                    SnackbarUtil.ShortSnackbar(mDateTv, "获取失败", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                cancelProgress();
                SnackbarUtil.ShortSnackbar(mDateTv, message, SnackbarUtil.INFO).show();
            }
        });
    }

    private void setData(WeeklyDetailModule detailModule) {
        WeeklyDetailModule.WeeklyModule rowsModule=detailModule.getRows().get(0);
        setJCViewState(mIrNum,rowsModule.getIrNum());
        setJCViewState(mEleIaNum,rowsModule.getEleIaNum());
        setJCViewState(mEleTNum,rowsModule.getEleTNum());
        setJCViewState(mTxNum,rowsModule.getTxNum());

        WeeklyDetailModule.ObjectModule objectModule=detailModule.getObject();
        mIrNumCS.setText(objectModule.getIrNum()+"次");
        mEleIaNumCS.setText(objectModule.getEleIaNum()+"次");
        mEleTNumCS.setText(objectModule.getEleTNum()+"次");
        mTxNumCS.setText(objectModule.getTxNum()+"次");
        mMaxNumCS.setText(objectModule.getTotalAlermCount()+"次");

        mNoteTv.setText(detailModule.getResultData());
    }

    private void setJCViewState(TextView tv,String state){
        if(state.equals("0")){
            tv.setText("正常");
            tv.setTextColor(getResources().getColor(R.color.normal_text));
        }
        else if(state.equals("1")){
            tv.setText("报警");
            tv.setTextColor(getResources().getColor(R.color.warn_text));
        }
        else{
            tv.setText("离线");
            tv.setTextColor(getResources().getColor(R.color.offline_text));
        }
    }
}
