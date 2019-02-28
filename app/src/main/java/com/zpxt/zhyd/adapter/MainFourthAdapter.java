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
import com.zpxt.zhyd.model.MainFourthAlarmListMoudle;
import com.zpxt.zhyd.model.RecentlyReportModule;
import com.zpxt.zhyd.ui.report.HistoryAlarmDetailActivity;
import com.zpxt.zhyd.ui.report.RecentlyReportDetailActivity;

import java.util.List;

/**
 * Description:      历史告警适配器
 * Autour：          LF
 * Date：            2018/5/17 14:41
 */

public class MainFourthAdapter  extends RecyclerView.Adapter<MainFourthAdapter.MyViewHolder> {

    private List<MainFourthAlarmListMoudle.MainFourthAlarmModule> mList;
    private Context mContext;

    public MainFourthAdapter(Context context,List<MainFourthAlarmListMoudle.MainFourthAlarmModule> list){
        this.mContext=context;
        this.mList=list;
    }

    @Override
    public MainFourthAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recently_report_list_item,parent,false);
        MainFourthAdapter.MyViewHolder holder = new MainFourthAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainFourthAdapter.MyViewHolder holder, int position) {
        MainFourthAlarmListMoudle.MainFourthAlarmModule item = mList.get(position);

        holder.mTitleAreaTv.setText(item.getDeviceSn());
        if(item.getPointTime().contains("T")){
            holder.mDateTimeTv.setText(item.getPointTime().replace("T"," "));
        }else{
            holder.mDateTimeTv.setText(item.getPointTime());
        }

        holder.mSensorNameTv.setText(item.getPointSn());
        holder.mDataTv.setText(item.getPointValue());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<MainFourthAlarmListMoudle.MainFourthAlarmModule> list){
        this.mList=list;
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
                    RecentlyReportModule recentlyReportModule=new RecentlyReportModule();
                    recentlyReportModule.setNodeID(mList.get(getPosition()).getGatewaySn());
                    recentlyReportModule.setOrgName(mList.get(getPosition()).getDeviceSn());
                    recentlyReportModule.setDistributionBox(mList.get(getPosition()).getDeviceSn());
                    recentlyReportModule.setSensorName(mList.get(getPosition()).getPointSn());
                    recentlyReportModule.setGenerationTimedata(mList.get(getPosition()).getPointTime());
                    recentlyReportModule.setConfirmState(mList.get(getPosition()).getTreatmentState());
                    recentlyReportModule.setData(mList.get(getPosition()).getPointValue());

                    Intent intent=new Intent(mContext,RecentlyReportDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("recentlyReportModule",recentlyReportModule);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

                }
            });
        }
    }
}

