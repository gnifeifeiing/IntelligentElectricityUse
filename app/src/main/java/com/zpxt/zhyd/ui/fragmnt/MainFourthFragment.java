package com.zpxt.zhyd.ui.fragmnt;


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.MainFourthAdapter;
import com.zpxt.zhyd.adapter.MainSecondAdapter;
import com.zpxt.zhyd.common.base.BaseFragment;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.Constants;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.MainFourthAlarmListMoudle;
import com.zpxt.zhyd.model.PickerViewListModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.ui.main.MainActivity;
import com.zpxt.zhyd.views.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Description:      历史告警
 * Autour：          LF
 * Date：            2018/1/4 16:56
 */

public class MainFourthFragment extends BaseFragment implements View.OnClickListener {

    private EditText mSearchEt;

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RelativeLayout mTypeRl;
    private TextView mTypeTv;

    private MainFourthAdapter mAdapter;
    private List<MainFourthAlarmListMoudle.MainFourthAlarmModule> mList=new ArrayList<>();

    private MainActivity mMainActivity;

    //分页参数
    private int mCurrentPage=1;
    private int mTotalSize=0;

    //筛选类型
    public List<PickerViewListModule> mTypePickerList = new ArrayList<>();

    private String mTypeStr = "";

    @Override
    public int initLayout() {
        mMainActivity= (MainActivity) getActivity();
        return R.layout.fragment_main_fourth;
    }

    @Override
    public void initView() {
        mSearchEt = mView.findViewById(R.id.mainFourth_searchEt);

        mRefreshLayout = mView.findViewById(R.id.mainFourth_refreshLayout);
        mRecyclerView = mView.findViewById(R.id.mainFourth_recyclerview);
        mTypeRl = mView.findViewById(R.id.mainFourth_typeRl);
        mTypeTv = mView.findViewById(R.id.mainFourth_typeTv);

        mAdapter = new MainFourthAdapter(getActivity(), mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置item间距
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(CommonUtils.dip2px(getActivity(), 5)));
        mRecyclerView.setAdapter(mAdapter);

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
                if(mTotalSize%Integer.parseInt(Constants.PAGE_SIZE_LARGE)==0){
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
    public void initListener() {
        mTypeRl.setOnClickListener(this);

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mCurrentPage=1;
                    mTotalSize=0;
                    mList.clear();
                    initListData();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
        initTypeList();
        initListData();
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
    }

    private void initListData() {
        mMainActivity.showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("nodeName", mSearchEt.getText().toString().trim());
        parameter.put("defaultCurrent", String.valueOf(mCurrentPage));
        parameter.put("pageSize", Constants.PAGE_SIZE_LARGE);
        parameter.put("state", "全部状态");
        parameter.put("nodeId","");
        parameter.put("sTime", "");
        parameter.put("eTime", "");
        parameter.put("type", mTypeStr);
        Call<MainFourthAlarmListMoudle> call = BaseAction.newInstance(false, true).queryAlarmEquipmentList(parameter);
        call.enqueue(new MyCallBack<MainFourthAlarmListMoudle>(mMainActivity) {
            @Override
            public void onSuccess(MainFourthAlarmListMoudle response) {
                mMainActivity.cancelProgress();
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
                mMainActivity.cancelProgress();
                SnackbarUtil.ShortSnackbar(mRecyclerView, message, SnackbarUtil.INFO).show();
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mainFourth_typeRl:
                mMainActivity.showSearchTwoDialog(mTypePickerList, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        mTypeTv.setText(mTypePickerList.get(options1).getPickerViewText());
                        mTypeStr = mTypePickerList.get(options1).getId();
                        mList.clear();
                        initListData();
                    }
                });
                break;
        }
    }
}
