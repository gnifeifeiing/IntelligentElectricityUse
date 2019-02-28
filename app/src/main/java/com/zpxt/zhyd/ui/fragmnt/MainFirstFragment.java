package com.zpxt.zhyd.ui.fragmnt;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zpxt.zhyd.MyApplication;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.adapter.MainFirstAdapter;
import com.zpxt.zhyd.common.base.BaseFragment;
import com.zpxt.zhyd.common.cache.SharedPreferenceCache;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.UserModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.ui.login.LoginActivity;
import com.zpxt.zhyd.ui.main.MainActivity;
import com.zpxt.zhyd.views.GlideImageLoader;
import com.zpxt.zhyd.views.MyListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


/**
 * Description:      首页
 * Autour：          LF
 * Date：            2018/1/4 16:40
 */
public class MainFirstFragment extends BaseFragment {


    private Banner mBanner;
    private MyListView mListview;

    private List<AlarmListModule.AlarmModule> mList=new ArrayList<>();
    private MainFirstAdapter mAdapter;

    private MainActivity mMainActivity;

    @Override
    public int initLayout() {
        mMainActivity= (MainActivity) getActivity();
        return R.layout.fragment_main_first;
    }

    @Override
    public void initView() {
        mBanner = mView.findViewById(R.id.mainFirst_banner);
        mListview = mView.findViewById(R.id.mainFirst_listview);

        mAdapter=new MainFirstAdapter(getActivity(),mList,getResources().getStringArray(R.array.alarm_detail_type)[0]);
        mListview.setAdapter(mAdapter);
        mListview.setFocusable(false);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        initBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        initListData();
    }

    private void initBanner() {
//        List<Integer> images=new ArrayList<>();
//        images.add(1);
//        images.add(2);
//        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//        mBanner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyApplication.H / 4));
//        //简单使用
//        mBanner.setImages(images).setImageLoader(new GlideImageLoader())
//                .setDelayTime(3000).start();
        //本地图片数据（资源文件）
        List<Integer> list=new ArrayList<>();
        list.add(R.mipmap.main_banner1);
        list.add(R.mipmap.main_banner2);
        mBanner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyApplication.H / 4));
        mBanner.setImages(list).setImageLoader(new GlideImageLoader()).setDelayTime(3000).start();
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
                        SnackbarUtil.ShortSnackbar(mListview, "没有数据", SnackbarUtil.INFO).show();
                    }
                }else{
                    SnackbarUtil.ShortSnackbar(mListview, "获取失败", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                mMainActivity.cancelProgress();
                SnackbarUtil.ShortSnackbar(mListview, message, SnackbarUtil.INFO).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }
}
