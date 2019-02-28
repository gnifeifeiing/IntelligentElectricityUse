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
import com.zpxt.zhyd.ui.report.AreaReportListActivity;
import com.zpxt.zhyd.ui.report.WeeklyDateListActivity;
import com.zpxt.zhyd.ui.report.ZoneReportListActivity;

import java.util.List;
import java.util.Map;

/**
 * Description:      历史数据列表适配器
 * Autour：          LF
 * Date：            2018/3/22 15:39
 */

public class MainSecondAdapter extends RecyclerView.Adapter<MainSecondAdapter.MyViewHolder> {

    private List<AlarmListModule.AlarmModule> mList;
    private Context mContext;
    /**
     * 记录当前页面的类型
     * 实时数据、历史数据、检测周报、历史告警
     */
    private String mPType;

    public MainSecondAdapter(Context context,List<AlarmListModule.AlarmModule> list,String pType){
        this.mContext=context;
        this.mList=list;
        this.mPType=pType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main_content_list_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AlarmListModule.AlarmModule item = mList.get(position);
        holder.mTitleAreaTv.setText(item.getNodeName());
        holder.mNodeNumberTv.setText("总数:"+item.getNodeNumbers());
        holder.mAlarmRateTv.setText(item.getAlarmRate());
        holder.mNormalTv.setText(item.getNormalRate());
        holder.mOfflineRateTv.setText(item.getOfflineRate());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<AlarmListModule.AlarmModule> list){
        this.mList=list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mTitleAreaTv;
        TextView mNodeNumberTv;
        TextView mAlarmRateTv;
        TextView mNormalTv;
        TextView mOfflineRateTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleAreaTv=itemView.findViewById(R.id.mainContentListItem_titleAreaTv);
            mNodeNumberTv=itemView.findViewById(R.id.mainContentListItem_nodeNumberTv);
            mAlarmRateTv=itemView.findViewById(R.id.mainContentListItem_alarmRateTv);
            mNormalTv=itemView.findViewById(R.id.mainContentListItem_normalTv);
            mOfflineRateTv=itemView.findViewById(R.id.mainContentListItem_offlineRateTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("pId",mList.get(getPosition()).getId());
                    bundle.putString("pType",mPType);
                    bundle.putString("pNodeName",mList.get(getPosition()).getNodeName());
                    if(mList.get(getPosition()).getNodeType().equals("BOTTOM")||mList.get(getPosition()).getNodeType().equals("GATEWAY")){
                        //检测周报日期列表
                        if(mPType.equals(mContext.getResources().getStringArray(R.array.alarm_detail_type)[2])){
                            Intent intent=new Intent(mContext, WeeklyDateListActivity.class);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                        //非检测周报
                        else{
                            Intent intent=new Intent(mContext, ZoneReportListActivity.class);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }else{
                        Intent intent=new Intent(mContext, AreaReportListActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
