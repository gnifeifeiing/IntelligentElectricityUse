package com.zpxt.zhyd.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.HistoryAlarmListModule;
import com.zpxt.zhyd.model.RealTimeListModule;
import com.zpxt.zhyd.model.RecentlyReportListModule;
import com.zpxt.zhyd.model.RecentlyReportModule;
import com.zpxt.zhyd.ui.report.HistoryAlarmDetailActivity;
import com.zpxt.zhyd.ui.report.RealTimeDetailActivity;
import com.zpxt.zhyd.ui.report.RecentlyReportDetailActivity;
import com.zpxt.zhyd.views.DealWithAdviceDialog;

import java.util.List;

/**
 * Description:      历史告警详情列表适配器
 * Autour：          LF
 * Date：            2018/3/29 10:58
 */

public class HistoryAlarmDetailAdapter extends RecyclerView.Adapter<HistoryAlarmDetailAdapter.MyViewHolder> {

    private List<HistoryAlarmListModule.HistoryAlarmModule> mList;
    private Context mContext;
    private HistoryAlarmDetailActivity mActivity;

    public HistoryAlarmDetailAdapter(Context context, List<HistoryAlarmListModule.HistoryAlarmModule> list) {
        this.mContext = context;
        this.mList = list;
        this.mActivity = (HistoryAlarmDetailActivity) mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.report_detaile_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HistoryAlarmListModule.HistoryAlarmModule item = mList.get(position);

        holder.mTitleTv.setText(item.getPointSn());
        holder.mValueTv.setText(item.getPointValue());
        holder.mDateTv.setText(item.getPointTime().replace("T", " "));

        if (item.getTreatmentState() != null && item.getTreatmentState().equals("未处理")) {
            holder.mStateTv.setText("未处理");
            holder.mStateTv.setBackgroundResource(R.drawable.zone_report_item_state_warn_bg);
            holder.mStateIv.setImageResource(R.mipmap.zone_detail_cheng);
        } else {
            holder.mStateTv.setText("已处理");
            holder.mStateTv.setBackgroundResource(R.drawable.zone_report_item_state_normal_bg);
            holder.mStateIv.setImageResource(R.mipmap.zone_detail_lv);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<HistoryAlarmListModule.HistoryAlarmModule> list) {
        this.mList = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTv;
        TextView mValueTv;
        TextView mDateTv;
        TextView mStateTv;
        ImageView mStateIv;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.detailItem_titleTv);
            mValueTv = itemView.findViewById(R.id.detailItem_valueTv);
            mDateTv = itemView.findViewById(R.id.detailItem_dateTv);
            mStateTv = itemView.findViewById(R.id.detailItem_stateTv);
            mStateIv = itemView.findViewById(R.id.detailItem_stateIv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mList.get(getPosition()).getTreatmentState().equals("未处理")) {
                        RecentlyReportModule module = new RecentlyReportModule();
                        module.setOrgName(mList.get(getPosition()).getDeviceSn());
                        module.setSensorName(mList.get(getPosition()).getPointSn());
                        module.setData(mList.get(getPosition()).getPointValue());
                        module.setDistributionBox(mList.get(getPosition()).getDeviceSn());
                        module.setNodeID(mList.get(getPosition()).getFactorySn());
                        module.setGenerationTimedata(mList.get(getPosition()).getPointTime());

                        Intent intent = new Intent(mContext, RecentlyReportDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("recentlyReportModule", module);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    } else {
                        SnackbarUtil.ShortSnackbar(mActivity.getActivityView(), "该项已处理", SnackbarUtil.INFO).show();
                    }
                }
            });
        }
    }
}
