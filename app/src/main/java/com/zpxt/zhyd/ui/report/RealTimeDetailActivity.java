package com.zpxt.zhyd.ui.report;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.AreaSecReportListAdapter;
import com.zpxt.zhyd.adapter.RealTimeDetailAdapter;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.cache.SharedPreferenceCache;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.RealTimeListModule;
import com.zpxt.zhyd.model.RecentlyReportModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.views.SpaceItemDecoration;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import retrofit2.Call;

/**
 * Description:      实时数据详情页
 * Autour：          LF
 * Date：            2018/3/27 16:58
 */
public class RealTimeDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;
    private TextView mTitleBarTv;
    private TextView mTitleTv;
    private TextView mTitleStateTv;

    private RecyclerView mRecyclerView;

    private RealTimeDetailAdapter mAdapter;
    private List<RealTimeListModule.RealTimeDetailModule> mList=new ArrayList<>();

    //父节点id和名字、状态
    private String mPId;
    private String mPNodeName;
    private String mPState;

    private String wsurl = "";
    private static final String TAG = "RealTimeDetailActivity";
    private WebSocketConnection mConnect = new WebSocketConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_detail);
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
            mPState = bundle.getString("pState");
        }

        mBackIv=findViewById(R.id.realTime_backIv);
        mTitleBarTv = findViewById(R.id.realTime_titleBarTv);
        mTitleTv=findViewById(R.id.realTime_titleTv);
        mTitleStateTv = findViewById(R.id.realTime_titleStateTv);
        mRecyclerView = findViewById(R.id.realTime_recyclerview);

        mTitleBarTv.setText(mPNodeName);
        mTitleTv.setText(mPNodeName);

        /**
         * 01：告警    2：正常
         */
        if (mPState.equals("1")) {
            mTitleStateTv.setText("告警");
            mTitleStateTv.setTextColor(Color.parseColor("#ff9800"));
        } else  if(mPState.equals("2")){
            mTitleStateTv.setText("正常");
            mTitleStateTv.setTextColor(Color.parseColor("#249b24"));
        }else{
            mTitleStateTv.setText("离线");
            mTitleStateTv.setTextColor(Color.parseColor("#cccacc"));
        }

        mAdapter=new RealTimeDetailAdapter(this,mList);
        mRecyclerView .setLayoutManager(new LinearLayoutManager(this));
        //设置item间距
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(CommonUtils.dip2px(this,5)));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        initListData();
    }


    private void initListData() {
        showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("id", mPId);
        Call<RealTimeListModule> call = BaseAction.newInstance(false, true).getRealTimeInfo(parameter);
        call.enqueue(new MyCallBack<RealTimeListModule>(this) {
            @Override
            public void onSuccess(RealTimeListModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    connect();
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
        switch (view.getId()){
            case R.id.realTime_backIv:
                finish();
                break;
        }
    }

    /**
     * 返回当前视图View,以便adapter能够使用
     * @return
     */
    public View getAvicityView(){
        return mTitleTv;
    }

    /**
     * websocket连接，接收服务器消息
     */
    private void connect() {
        if (!mConnect.isConnected()) {
            try {
                wsurl = Constants.SERVER_SOCKET_URL + mPId+"?flag=appReal&token=" + SharedPreferenceCache.getInstance().getPres("AccessToken");
                mConnect.connect(wsurl, new WebSocketHandler() {
                    @Override
                    public void onOpen() {
                        Log.i(TAG, "Status:Connect to " + wsurl);
                    }


                    @Override
                    public void onTextMessage(String payload) {
                        Log.i(TAG, payload);
                        if (!payload.equals("连接成功")) {
                            RealTimeListModule.RealTimeDetailModule realTimeDetailModule = JSON.parseObject(payload, RealTimeListModule.RealTimeDetailModule.class);
                            updateItemData(realTimeDetailModule);
                        }
                    }

                    @Override
                    public void onClose(int code, String reason) {
                    }
                });
            } catch (WebSocketException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateItemData(RealTimeListModule.RealTimeDetailModule realTimeDetailModule){
        switch (realTimeDetailModule.getNAME()){
            case "A相电流":
                mAdapter.getData().remove(0);
                mAdapter.getData().add(0,realTimeDetailModule);
                break;
            case "B相电流":
                mAdapter.getData().remove(1);
                mAdapter.getData().add(1,realTimeDetailModule);
                break;
            case "C相电流":
                mAdapter.getData().remove(2);
                mAdapter.getData().add(2,realTimeDetailModule);
                break;
            case "剩余电流":
                mAdapter.getData().remove(3);
                mAdapter.getData().add(3,realTimeDetailModule);
                break;
            case "A相温度":
                mAdapter.getData().remove(4);
                mAdapter.getData().add(4,realTimeDetailModule);
                break;
            case "B相温度":
                mAdapter.getData().remove(5);
                mAdapter.getData().add(5,realTimeDetailModule);
                break;
            case "C相温度":
                mAdapter.getData().remove(6);
                mAdapter.getData().add(6,realTimeDetailModule);
                break;
            case "箱体温度":
                mAdapter.getData().remove(7);
                mAdapter.getData().add(7,realTimeDetailModule);
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnect.disconnect();
    }
}
