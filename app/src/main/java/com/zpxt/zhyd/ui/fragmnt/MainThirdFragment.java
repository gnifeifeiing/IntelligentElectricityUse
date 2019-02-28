package com.zpxt.zhyd.ui.fragmnt;


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.MainSecondAdapter;
import com.zpxt.zhyd.common.base.BaseFragment;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
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
 * Description:      描述
 * Autour：          LF
 * Date：            2018/1/4 16:55
 */

public class MainThirdFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private MainSecondAdapter mAdapter;
    private List<AlarmListModule.AlarmModule> mList=new ArrayList<>();

    private MainActivity mMainActivity;

    @Override
    public int initLayout() {
        mMainActivity= (MainActivity) getActivity();
        return R.layout.fragment_main_third;
    }

    @Override
    public void initView() {
        mRecyclerView = mView.findViewById(R.id.mainThird_recyclerview);

        mAdapter = new MainSecondAdapter(getActivity(), mList,getResources().getStringArray(R.array.alarm_detail_type)[2]);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置item间距
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(CommonUtils.dip2px(getActivity(), 5)));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        initListData();
    }

    private void initListData() {
        mMainActivity.showProgress(getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("parentId", "");
        Call<AlarmListModule> call = BaseAction.newInstance(false, true).getNodeList(parameter);
        call.enqueue(new MyCallBack<AlarmListModule>(mMainActivity) {
            @Override
            public void onSuccess(AlarmListModule response) {
                mMainActivity.cancelProgress();
                if (response.getResultCode().equals("1")) {
                    if(response.getRows()!=null&&response.getRows().size()>0){
                        mList=response.getRows();
                        mAdapter.setData(mList);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        SnackbarUtil.ShortSnackbar(mRecyclerView, "没有数据", SnackbarUtil.INFO).show();
                    }
                }else{
                    SnackbarUtil.ShortSnackbar(mRecyclerView, "获取失败", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                mMainActivity.cancelProgress();
                SnackbarUtil.ShortSnackbar(mRecyclerView, message, SnackbarUtil.INFO).show();
            }
        });
    }
}
