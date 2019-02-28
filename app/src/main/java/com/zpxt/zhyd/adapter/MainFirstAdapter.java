package com.zpxt.zhyd.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zpxt.zhyd.R;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.ui.report.AreaReportListActivity;
import com.zpxt.zhyd.ui.report.ZoneReportListActivity;

import java.util.List;
import java.util.Map;

/**
 * Description:      首页列表适配器
 * Autour：          LF
 * Date：            2018/3/21 14:36
 */

public class MainFirstAdapter extends BaseAdapter {

    private List<AlarmListModule.AlarmModule> mList;
    private Context mContext;
    /**
     * 记录当前页面的类型
     * 实时数据、历史数据、检测周报、历史告警
     */
    private String mPType;

    public MainFirstAdapter(Context context, List<AlarmListModule.AlarmModule> list,String pType) {
        this.mContext = context;
        this.mList = list;
        this.mPType=pType;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        MyViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_main_content_list_item, null);
            holder = new MyViewHolder();
            holder.mTitleAreaTv = view.findViewById(R.id.mainContentListItem_titleAreaTv);
            holder.mNodeNumberTv = view.findViewById(R.id.mainContentListItem_nodeNumberTv);
            holder.mAlarmRateTv = view.findViewById(R.id.mainContentListItem_alarmRateTv);
            holder.mNormalTv = view.findViewById(R.id.mainContentListItem_normalTv);
            holder.mOfflineRateTv = view.findViewById(R.id.mainContentListItem_offlineRateTv);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }
        holder.mTitleAreaTv.setText(mList.get(i).getNodeName());
        holder.mNodeNumberTv.setText("总数:"+mList.get(i).getNodeNumbers());
        holder.mAlarmRateTv.setText(mList.get(i).getAlarmRate());
        holder.mNormalTv.setText(mList.get(i).getNormalRate());
        holder.mOfflineRateTv.setText(mList.get(i).getOfflineRate());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("pId",mList.get(i).getId());
                bundle.putString("pType",mPType);
                bundle.putString("pNodeName",mList.get(i).getNodeName());
                if(mList.get(i).getNodeType().equals("BOTTOM")||mList.get(i).getNodeType().equals("GATEWAY")){
                    Intent intent=new Intent(mContext, ZoneReportListActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }else{
                    Intent intent=new Intent(mContext, AreaReportListActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
        return view;
    }

    class MyViewHolder {
        TextView mTitleAreaTv;
        TextView mNodeNumberTv;
        TextView mAlarmRateTv;
        TextView mNormalTv;
        TextView mOfflineRateTv;
    }

    public void setData(List<AlarmListModule.AlarmModule> list) {
        this.mList = list;
    }
}
