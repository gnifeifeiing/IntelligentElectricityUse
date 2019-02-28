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
import com.zpxt.zhyd.model.WeeklyDateListModuel;
import com.zpxt.zhyd.ui.report.AreaSecReportListActivity;
import com.zpxt.zhyd.ui.report.ZoneReportListActivity;

import java.util.List;

/**
 * Description:      监测周报日期列表适配器
 * Autour：          LF
 * Date：            2018/3/30 11:07
 */

public class WeeklyDateListAdapter  extends RecyclerView.Adapter<WeeklyDateListAdapter.MyViewHolder> {

    private List<WeeklyDateListModuel.WeeklyDateModule> mList;
    private Context mContext;
    /**
     * 记录当前页面的类型
     * 实时数据、历史数据、检测周报、历史告警
     */
    private String mPType;
    //父节点id和名字
    private String mPId;
    private String mPNodeName;

    public WeeklyDateListAdapter(Context context, List<WeeklyDateListModuel.WeeklyDateModule> list,String id,String nodeName ,String type){
        this.mContext=context;
        this.mList=list;
        this.mPId=id;
        this.mPNodeName=nodeName;
        this.mPType=type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weekly_date_list_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        WeeklyDateListModuel.WeeklyDateModule item = mList.get(position);
        holder.mTitleTv.setText(item.getOrgName());
        holder.mStartDate.setText(item.getStartTime().replace("-","."));
        holder.mEndDate.setText(item.getEndTime().replace("-","."));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<WeeklyDateListModuel.WeeklyDateModule> list){
        this.mList=list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mTitleTv;
        TextView mStartDate;
        TextView mEndDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleTv=itemView.findViewById(R.id.weeklyDateItem_titleTv);
            mStartDate=itemView.findViewById(R.id.weeklyDateItem_startDateTv);
            mEndDate=itemView.findViewById(R.id.weeklyDateItem_endDateTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("pId",mPId);
                    bundle.putString("pType",mPType);
                    bundle.putString("pNodeName",mPNodeName);
                    bundle.putString("startTime",mList.get(getPosition()).getStartTime());
                    bundle.putString("endTime",mList.get(getPosition()).getEndTime());
                    Intent intent=new Intent(mContext, ZoneReportListActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
