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
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.RecentlyReportListModule;
import com.zpxt.zhyd.model.RecentlyReportModule;
import com.zpxt.zhyd.ui.report.RecentlyReportDetailActivity;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Description:      最近告警列表适配器
 * Autour：          LF
 * Date：            2018/3/21 16:32
 */

public class RecentlyReportAdapter extends RecyclerView.Adapter<RecentlyReportAdapter.MyViewHolder> {

    private List<RecentlyReportModule> mList;
    private Context mContext;

    public RecentlyReportAdapter(Context context,List<RecentlyReportModule> list){
        this.mContext=context;
        this.mList=list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recently_report_list_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecentlyReportModule item = mList.get(position);
        holder.mTitleAreaTv.setText(item.getDistributionBox());
        if(item.getGenerationTimedata().contains("T")){
            holder.mDateTimeTv.setText(item.getGenerationTimedata().replace("T"," "));
        }else{
            holder.mDateTimeTv.setText(item.getGenerationTimedata());
        }

        holder.mSensorNameTv.setText(item.getSensorName());
        holder.mDataTv.setText(item.getData());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<RecentlyReportModule> list){
        this.mList=list;
    }

    /**
     * 指定位置添加数据，并添加动画效果
     * @param position
     * @param data
     */
    public void add(int position,RecentlyReportModule data) {
        //控制列表添加数据长度，如果超过30，移除最后一个
        if(mList.size()>30){
            mList.remove(30);
            notifyItemRemoved(30);
        }
        mList.add(position,data);
        notifyItemInserted(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mTitleAreaTv;
        TextView mDateTimeTv;
        TextView mSensorNameTv;
        TextView mDataTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleAreaTv=itemView.findViewById(R.id.recentlyReportListItem_titleAreaTv);
            mDateTimeTv=itemView.findViewById(R.id.recentlyReportListItem_dateTv);
            mSensorNameTv=itemView.findViewById(R.id.recentlyReportListItem_sensorNameTv);
            mDataTv=itemView.findViewById(R.id.recentlyReportListItem_dataTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,RecentlyReportDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("recentlyReportModule",mList.get(getPosition()));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
