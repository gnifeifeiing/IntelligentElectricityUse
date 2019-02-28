package com.zpxt.zhyd.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description:      描述
 * Autour：          LF
 * Date：            2017/7/10 15:56
 */
public abstract class BaseFragment extends Fragment {

    public View mView;
    public Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = initLayout();
        mView = inflater.inflate(layoutId, container, false);
        mContext = getActivity();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        initData();
    }

    /**
     * 初始化布局
     * @return  布局id
     */
    public abstract int initLayout();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 初始化事件
     */
    public abstract void initListener();

    /**
     * 初始化数据
     */
    public abstract void initData();
}
