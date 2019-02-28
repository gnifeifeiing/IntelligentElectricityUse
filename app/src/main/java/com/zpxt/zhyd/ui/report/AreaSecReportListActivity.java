package com.zpxt.zhyd.ui.report;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.AreaReportListAdapter;
import com.zpxt.zhyd.adapter.AreaSecReportListAdapter;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.AlarmSearchListModule;
import com.zpxt.zhyd.model.PickerViewListModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.views.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Description:      告警搜索列表-第二页
 * Autour：          LF
 * Date：            2018/3/22 15:07
 */
public class AreaSecReportListActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackIv;
    private TextView mNodeNameTv;

    private RecyclerView mRecyclerView;
    private EditText mSearchEt;

    private AreaSecReportListAdapter mAdapter;
    private List<AlarmListModule.AlarmModule> mList = new ArrayList<>();
    private List<PickerViewListModule> mPickerList = new ArrayList<>();

    //父节点id和名字
    private String mPId;
    private String mPNodeName;
    /**
     * 记录当前页面的类型
     * 实时数据、历史数据、检测周报、历史告警
     */
    private String mPType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_sec_report_list);
        hideActionBar();
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initListener() {
        mBackIv.setOnClickListener(this);
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mPickerList.clear();
                    searchData();
                    return true;
                }
                return false;
            }
        });
    }


    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPId = bundle.getString("pId");
            mPNodeName = bundle.getString("pNodeName");
            mPType = bundle.getString("pType");
        }

        mBackIv=findViewById(R.id.areaSecReport_backIv);
        mNodeNameTv = findViewById(R.id.areaSecReport_nodeNameTv);
        mRecyclerView=findViewById(R.id.areaSecReport_recyclerview);
        mSearchEt = findViewById(R.id.areaSecReport_searchEt);

        mNodeNameTv.setText(mPNodeName);

        mAdapter=new AreaSecReportListAdapter(this,mList,mPType);
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
        switch (view.getId()){
            case R.id.areaSecReport_backIv:
                finish();
                break;
        }
    }

    /**
     * 搜索
     */
    private void searchData() {
        showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("nodeName", mSearchEt.getText().toString().trim());
        Call<AlarmSearchListModule> call = BaseAction.newInstance(false, true).getSearchList(parameter);
        call.enqueue(new MyCallBack<AlarmSearchListModule>(this) {
            @Override
            public void onSuccess(AlarmSearchListModule response) {
                cancelProgress();
                if (response.getResultCode().equals("1")) {
                    if (response.getRows() != null && response.getRows().size() > 0) {
                        PickerViewListModule pickerModule = null;
                        for (AlarmSearchListModule.AlarmSearchModule module : response.getRows()) {
                            pickerModule = new PickerViewListModule();
                            pickerModule.setName(module.getName());
                            pickerModule.setId(module.getId());
                            pickerModule.setpId(module.getParentId());
                            pickerModule.setNodeType(module.getNodeType());
                            mPickerList.add(pickerModule);
                        }
                        showSearchDialog();
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

    /**
     * 搜索结果弹框
     */
    OptionsPickerView pvOptions;

    private void showSearchDialog() {
        if (pvOptions == null) {
            pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    Bundle bundle=new Bundle();
                    bundle.putString("pId",mPickerList.get(options1).getId());
                    bundle.putString("pType",mPType);
                    bundle.putString("pNodeName",mPickerList.get(options1).getName());
                    if(mPickerList.get(options1).getNodeType().equals("BOTTOM")){
                        //检测周报日期列表
                        if(mPType.equals(getResources().getStringArray(R.array.alarm_detail_type)[2])){
                            Intent intent=new Intent(AreaSecReportListActivity.this, WeeklyDateListActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        //非检测周报
                        else{
                            Intent intent=new Intent(AreaSecReportListActivity.this, ZoneReportListActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }else{
                        Intent intent=new Intent(AreaSecReportListActivity.this, AreaSecReportListActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            })
                    .setTitleText("请选择")
                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(20)
                    .setLineSpacingMultiplier(1.8f)
                    .build();

            pvOptions.setPicker(mPickerList);
            pvOptions.show();
        } else {
            if (!pvOptions.isShowing()) {
                pvOptions.show();
            }
        }
    }
}
