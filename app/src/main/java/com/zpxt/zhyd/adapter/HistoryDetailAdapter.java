package com.zpxt.zhyd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zpxt.zhyd.R;
import com.zpxt.zhyd.model.HistoryAlarmListModule;
import com.zpxt.zhyd.model.HistoryListModule;
import com.zpxt.zhyd.ui.report.HistoryAlarmDetailActivity;
import com.zpxt.zhyd.ui.report.HistoryDetailActivity;

import java.util.List;

/**
 * Description:      历史数据列表适配器
 * Autour：          LF
 * Date：            2018/3/29 14:28
 */

public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.MyViewHolder> {

    private List<HistoryListModule.HistoryModule> mList;
    private Context mContext;
    private HistoryDetailActivity mActivity;

    public HistoryDetailAdapter(Context context, List<HistoryListModule.HistoryModule> list) {
        this.mContext = context;
        this.mList = list;
        this.mActivity = (HistoryDetailActivity) mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.report_detaile_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HistoryListModule.HistoryModule item = mList.get(position);

        holder.mTitleTv.setText(item.getSensorName());

        holder.mDateTv.setText(item.getPointTime().replace("T"," "));

        if (item.getPointSn().contains("T")) {
            holder.mValueTv.setText(item.getPointValue() + "℃");
        } else {
            holder.mValueTv.setText(item.getPointValue() + "A");
        }
        //默认全部是告警
        holder.mStateTv.setText("告警");
        holder.mStateTv.setBackgroundResource(R.drawable.zone_report_item_state_warn_bg);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<HistoryListModule.HistoryModule> list) {
        this.mList = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTv;
        TextView mValueTv;
        TextView mDateTv;
        TextView mStateTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.detailItem_titleTv);
            mValueTv = itemView.findViewById(R.id.detailItem_valueTv);
            mDateTv = itemView.findViewById(R.id.detailItem_dateTv);
            mStateTv = itemView.findViewById(R.id.detailItem_stateTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
