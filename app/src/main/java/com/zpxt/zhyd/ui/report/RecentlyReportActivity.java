package com.zpxt.zhyd.ui.report;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.RecentlyReportAdapter;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.cache.SharedPreferenceCache;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.AlarmSearchListModule;
import com.zpxt.zhyd.model.PickerViewListModule;
import com.zpxt.zhyd.model.RecentlyReportListModule;
import com.zpxt.zhyd.model.RecentlyReportModule;
import com.zpxt.zhyd.model.RecentlyReportOrganizationListModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.views.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import retrofit2.Call;

/**
 * Description:      最近告警
 * Autour：          LF
 * Date：            2018/3/21 15:54
 */
public class RecentlyReportActivity extends BaseActivity implements View.OnClickListener {

    private EditText mSearchEt;

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RelativeLayout mOrganizationRl;
    private TextView mOrganizationTv;
    private RelativeLayout mTypeRl;
    private TextView mTypeTv;

    private RecentlyReportAdapter mAdapter;
    private List<RecentlyReportModule> mList = new ArrayList<>();

    //分页参数
    private int mCurrentPage = 1;
    private int mTotalSize = 0;

    //地区或组织筛选集合
    public List<PickerViewListModule> mOrganizationPickerList = new ArrayList<>();
    //筛选类型
    public List<PickerViewListModule> mTypePickerList = new ArrayList<>();

    private String mTypeStr = "";
    //选中组织结构ID
    private String mSelectedID;


    private String wsurl = "";
    private static final String TAG = "RecentlyReportActivity";
    private WebSocketConnection mConnect = new WebSocketConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_report);
        setActionBarTitle("最近告警");
        initView();
        initListener();
        initData();
    }

    /**
     * websocket连接，接收服务器消息
     */
    private void connect() {
        if (!mConnect.isConnected()) {
            try {
                wsurl = Constants.SERVER_SOCKET_URL + "all?flag=app&type=" + mTypeStr + "&orgID=" + mSelectedID + "&token=" + SharedPreferenceCache.getInstance().getPres("AccessToken");
                mConnect.connect(wsurl, new WebSocketHandler() {
                    @Override
                    public void onOpen() {
                        Log.i(TAG, "Status:Connect to " + wsurl);
                    }

                    @Override
                    public void onTextMessage(String payload) {
                        Log.i(TAG, payload);
                        if (!payload.equals("连接成功")) {
                            RecentlyReportModule recentlyReportModule = JSON.parseObject(payload, RecentlyReportModule.class);
                            mAdapter.add(0, recentlyReportModule);
                            mRecyclerView.scrollToPosition(0);
                        }
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        Log.i(TAG, "Connection lost..");
                    }
                });
            } catch (WebSocketException e) {
                Log.i(TAG, "Connection Error:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnect.disconnect();
    }

    private void initListener() {
        mOrganizationRl.setOnClickListener(this);
        mTypeRl.setOnClickListener(this);

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mList.clear();
                    initListData();
                    return true;
                }
                return false;
            }
        });
    }

    private void initView() {
        mSearchEt = findViewById(R.id.recentlyReport_searchEt);
        mRecyclerView = findViewById(R.id.recentlyReport_recyclerview);
        mOrganizationRl = findViewById(R.id.recentlyReport_organizationRl);
        mOrganizationTv = findViewById(R.id.recentlyReport_organizationTv);
        mTypeRl = findViewById(R.id.recentlyReport_typeRl);
        mTypeTv = findViewById(R.id.recentlyReport_typeTv);

        mRefreshLayout = findViewById(R.id.recentlyReport_refreshLayout);

        mAdapter = new RecentlyReportAdapter(this, mList);
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
//                if (mTotalSize % Integer.parseInt(Constants.PAGE_SIZE_LARGE) == 0) {
//                    mCurrentPage++;
//                    initListData();
//                } else {
//                    SnackbarUtil.ShortSnackbar(mRecyclerView, "已加载全部数据", SnackbarUtil.INFO).show();
//                    mRefreshLayout.finishLoadMore();
//                }

                mCurrentPage++;
                initListData();
            }
        });
    }

    private void initData() {
        initTypeList();
        initListData();
        /*
        隐藏取消
        initOrganizationList();
        */
    }

    /**
     * 初始化类型筛选框数据
     */
    private void initTypeList() {
        //数据类型：全部类型all、电流I、温度T、剩余电流IR，四种
        String[] typeArray = getResources().getStringArray(R.array.recently_report_type);
        PickerViewListModule pickerModule = new PickerViewListModule();
        pickerModule.setId("all");
        pickerModule.setName(typeArray[0]);
        mTypePickerList.add(pickerModule);

        pickerModule = new PickerViewListModule();
        pickerModule.setId("I");
        pickerModule.setName(typeArray[1]);
        mTypePickerList.add(pickerModule);

        pickerModule = new PickerViewListModule();
        pickerModule.setId("T");
        pickerModule.setName(typeArray[2]);
        mTypePickerList.add(pickerModule);

        pickerModule = new PickerViewListModule();
        pickerModule.setId("IR");
        pickerModule.setName(typeArray[3]);
        mTypePickerList.add(pickerModule);

        //设置类型筛选默认第一个
        mTypeTv.setText(typeArray[0]);
        mTypeStr = "all";
        mSelectedID = "all";
    }

    /**
     * 获取组织列表
     */
    private void initOrganizationList() {
        showProgress(getResources().getString(R.string.loading_text));

        Call<RecentlyReportOrganizationListModule> call = BaseAction.newInstance(false, true).getRecentlyReportOrganizationList();
        call.enqueue(new MyCallBack<RecentlyReportOrganizationListModule>(this) {
            @Override
            public void onSuccess(RecentlyReportOrganizationListModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    if ((response.getRows() != null) && (response.getRows().size() > 0)) {
                        mOrganizationPickerList.clear();
                        PickerViewListModule pickerModule = null;
                        pickerModule = new PickerViewListModule();
                        pickerModule.setId("all");
                        pickerModule.setName("全部");
                        mOrganizationPickerList.add(pickerModule);
                        for (int i = 0; i < response.getRows().size(); i++) {
                            pickerModule = new PickerViewListModule();
                            pickerModule.setId(response.getRows().get(i).getOrgID());
                            pickerModule.setName(response.getRows().get(i).getOrgName());
                            mOrganizationPickerList.add(pickerModule);
                        }
                        //设置地区、组织信息默认第一个
                        mOrganizationTv.setText(mOrganizationPickerList.get(0).getName());
                        initListData();
                    } else {
                        SnackbarUtil.ShortSnackbar(mRecyclerView, "没有数据", SnackbarUtil.INFO).show();
                    }
                } else {
                    SnackbarUtil.ShortSnackbar(mRecyclerView, "获取组织信息失败", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                cancelProgress();
                SnackbarUtil.ShortSnackbar(mRecyclerView, message, SnackbarUtil.INFO).show();
            }
        });
    }

    /**
     * 获取最近告警列表
     */
    private void initListData() {
        showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("nodeName", mSearchEt.getText().toString().trim());
        parameter.put("type", mTypeStr);// 数据类型：全部类型all、电流I、温度T、剩余电流IR，四种
        parameter.put("defaultCurrent", String.valueOf(mCurrentPage));
        parameter.put("pageSize", Constants.PAGE_SIZE_LARGE);
        parameter.put("orgID", "");
        parameter.put("orgName", "全部");//地区名称

        Call<RecentlyReportListModule> call = BaseAction.newInstance(false, true).getRecentlyReportList(parameter);
        call.enqueue(new MyCallBack<RecentlyReportListModule>(this) {
            @Override
            public void onSuccess(RecentlyReportListModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    //开启长连接
                    connect();

                    if (response.getRows() != null && response.getRows().size() > 0) {
                        mList.addAll(response.getRows());
                        mTotalSize+=mList.size();
                    } else {
                        SnackbarUtil.ShortSnackbar(mRecyclerView, "没有数据", SnackbarUtil.INFO).show();
                    }
                    mAdapter.setData(mList);
                    mAdapter.notifyDataSetChanged();
//                    runLayoutAnimation(mRecyclerView);

//                    mAdapter.notifyDataSetChanged();
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
            case R.id.recentlyReport_organizationRl:
                showSearchOneDialog(mOrganizationPickerList, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        mOrganizationTv.setText(mOrganizationPickerList.get(options1).getPickerViewText());
                        mSelectedID = mOrganizationPickerList.get(options1).getId();
                        mList.clear();
                        //关闭长连接
                        mConnect.disconnect();
                        initListData();
                    }
                });
                break;
            case R.id.recentlyReport_typeRl:
                showSearchTwoDialog(mTypePickerList, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        mTypeTv.setText(mTypePickerList.get(options1).getPickerViewText());
                        mTypeStr = mTypePickerList.get(options1).getId();
                        mList.clear();
                        //关闭长连接
                        mConnect.disconnect();
                        initListData();
                    }
                });
                break;
        }
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
