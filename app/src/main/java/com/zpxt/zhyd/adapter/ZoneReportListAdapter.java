package com.zpxt.zhyd.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.ui.report.AreaSecReportListActivity;
import com.zpxt.zhyd.ui.report.HistoryAlarmDetailActivity;
import com.zpxt.zhyd.ui.report.HistoryDetailActivity;
import com.zpxt.zhyd.ui.report.RealTimeDetailActivity;
import com.zpxt.zhyd.ui.report.ReportDetailActivity;
import com.zpxt.zhyd.ui.report.WeeklyDetailAvtivity;
import com.zpxt.zhyd.ui.report.ZoneReportListActivity;

import java.util.List;
import java.util.Map;

/**
 * Description:      厂房状态列表适配器
 * Autour：          LF
 * Date：            2018/3/22 16:53
 */

public class ZoneReportListAdapter extends RecyclerView.Adapter<ZoneReportListAdapter.MyViewHolder> {

    private List<AlarmListModule.AlarmModule> mList;
    private Context mContext;
    private ZoneReportListActivity mActivity;
    /**
     * 记录当前页面的类型
     * 实时数据、历史数据、检测周报、历史告警
     */
    private String mPType;

    public ZoneReportListAdapter(Context context, List<AlarmListModule.AlarmModule> list, String pType) {
        this.mContext = context;
        this.mList = list;
        this.mActivity = (ZoneReportListActivity) mContext;
        this.mPType = pType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.zone_report_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AlarmListModule.AlarmModule item = mList.get(position);
        holder.mTitleTv.setText(item.getNodeName());
        if (item.getState() != null) {
            /**
             * 01：告警    2：正常
             */
            if (item.getState().equals("1")) {
                holder.mStateTv.setText("告警");
                holder.mStateTv.setBackgroundResource(R.drawable.zone_report_item_state_warn_bg);
            } else if (item.getState().equals("2")) {
                holder.mStateTv.setText("正常");
                holder.mStateTv.setBackgroundResource(R.drawable.zone_report_item_state_normal_bg);
            } else {
                holder.mStateTv.setText("离线");
                holder.mStateTv.setBackgroundResource(R.drawable.zone_report_item_state_offline_bg);
            }
        }else{
            holder.mStateTv.setText("离线");
            holder.mStateTv.setBackgroundResource(R.drawable.zone_report_item_state_offline_bg);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<AlarmListModule.AlarmModule> list) {
        this.mList = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTv;
        private TextView mStateTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.zoneItem_titleTv);
            mStateTv = itemView.findViewById(R.id.zoneItem_stateTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putString("pId", mList.get(getPosition()).getId());
                    bundle.putString("pType", mPType);
                    bundle.putString("pNodeName", mList.get(getPosition()).getNodeName());
                    bundle.putString("pState", mList.get(getPosition()).getState());

                    Intent intent = null;
                    if (mPType.equals(mContext.getResources().getStringArray(R.array.alarm_detail_type)[0])) {
                        if (mList.get(getPosition()).getState()!=null) {
                            //只有告警和正常才能点击
                            if (mList.get(getPosition()).getState().equals("1") || mList.get(getPosition()).getState().equals("2")) {
                                //实时数据
                                intent = new Intent(mContext, RealTimeDetailActivity.class);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            } else {
//                            SnackbarUtil.ShortSnackbar(mActivity.getActivityView(), "已离线", SnackbarUtil.INFO).show();
                                intent = new Intent(mContext, RealTimeDetailActivity.class);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                            }
                        }else{
                            SnackbarUtil.ShortSnackbar(mActivity.mRecyclerView, "该项告警状态获取为NULL", SnackbarUtil.INFO).show();
                        }
                    }
                    if (mPType.equals(mContext.getResources().getStringArray(R.array.alarm_detail_type)[1])) {
                        //历史数据
                        intent = new Intent(mContext, HistoryDetailActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    } else if (mPType.equals(mContext.getResources().getStringArray(R.array.alarm_detail_type)[2])) {
                        bundle.putString("startTime", mActivity.getStartTime());
                        bundle.putString("endTime", mActivity.getEndTime());
                        bundle.putString("treeParentIds", mList.get(getPosition()).getTreeParentIds());
                        //检测周报
                        intent = new Intent(mContext, WeeklyDetailAvtivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    } else if (mPType.equals(mContext.getResources().getStringArray(R.array.alarm_detail_type)[3])) {
                        //历史告警
                        intent = new Intent(mContext, HistoryAlarmDetailActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }

                }
            });
        }
    }
}
