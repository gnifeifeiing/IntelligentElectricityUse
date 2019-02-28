package com.zpxt.zhyd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zpxt.zhyd.MyApplication;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.utils.CommonUtils;
import com.zpxt.zhyd.common.utils.SnackbarUtil;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.RealTimeDealAdviceModule;
import com.zpxt.zhyd.model.RealTimeListModule;
import com.zpxt.zhyd.retrofit.BaseAction;
import com.zpxt.zhyd.retrofit.MyCallBack;
import com.zpxt.zhyd.ui.report.RealTimeDetailActivity;
import com.zpxt.zhyd.views.DealWithAdviceDialog;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

import retrofit2.Call;

/**
 * Description:      厂-列表适配器
 * Autour：          LF
 * Date：            2018/3/22 17:18
 */

public class RealTimeDetailAdapter extends RecyclerView.Adapter<RealTimeDetailAdapter.MyViewHolder> {

    private List<RealTimeListModule.RealTimeDetailModule> mList;
    private Context mContext;
    private RealTimeDetailActivity mActivity;

    public RealTimeDetailAdapter(Context context, List<RealTimeListModule.RealTimeDetailModule> list) {
        this.mContext = context;
        this.mList = list;
        this.mActivity = (RealTimeDetailActivity) mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.report_detaile_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RealTimeListModule.RealTimeDetailModule item = mList.get(position);
        holder.mTitleTv.setText(item.getNAME());
        if (item.getPOINTID().contains("T")) {
            holder.mValueTv.setText(item.getVALUE() + "℃");
        } else if(item.getPOINTID().equals("IR")){
            holder.mValueTv.setText(item.getVALUE() + "mA");
        }else {
            holder.mValueTv.setText(item.getVALUE() + "A");
        }

        holder.mDateTv.setText(item.getTIME());

        /**
         * 01：告警    2：正常
         */
        if (item.getALARM().equals("1")) {
            holder.mStateTv.setText("告警");
            holder.mStateTv.setBackgroundResource(R.drawable.zone_report_item_state_warn_bg);
            holder.mStateIv.setImageResource(R.mipmap.zone_detail_cheng);
        } else {
            holder.mStateTv.setText("正常");
            holder.mStateTv.setBackgroundResource(R.drawable.zone_report_item_state_normal_bg);
            holder.mStateIv.setImageResource(R.mipmap.zone_detail_lv);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<RealTimeListModule.RealTimeDetailModule> list) {
        this.mList = list;
    }

    public List<RealTimeListModule.RealTimeDetailModule> getData() {
        return mList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mStateIv;
        TextView mTitleTv;
        TextView mValueTv;
        TextView mDateTv;
        TextView mStateTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            mStateIv = itemView.findViewById(R.id.detailItem_stateIv);
            mTitleTv = itemView.findViewById(R.id.detailItem_titleTv);
            mValueTv = itemView.findViewById(R.id.detailItem_valueTv);
            mDateTv = itemView.findViewById(R.id.detailItem_dateTv);
            mStateTv = itemView.findViewById(R.id.detailItem_stateTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //只有告警才提示
                    if (mList.get(getPosition()).getALARM().equals("1")) {
                        showCustomDialog(mContext, new CustomDialogInterface() {
                            @Override
                            public void onCommitClick(DealWithAdviceDialog dialog,EditText editText) {
                                dealRequest(dialog,mList.get(getPosition()), editText);
                            }
                        });
                    }else{
                        SnackbarUtil.ShortSnackbar(mActivity.getAvicityView(), "无需处理", SnackbarUtil.INFO).show();
                    }
                }
            });
        }
    }

    /**
     * 处理意见
     * @param dialog
     * @param item  点击项实体类
     * @param editText  输入意见框
     */
    private void dealRequest(DealWithAdviceDialog dialog,RealTimeListModule.RealTimeDetailModule item, EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            SnackbarUtil.ShortSnackbar(mActivity.getAvicityView(), "请输入处理信息", SnackbarUtil.INFO).show();
            return;
        }
        dialog.dismiss();
        mActivity.showProgress(mContext.getResources().getString(R.string.loading_text));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("alarm", "1");
        parameter.put("deviceSn", item.getSN());
        parameter.put("treatmentSuggestion", editText.getText().toString().trim());
        parameter.put("pointTime", item.getTIME());
        parameter.put("pointValue", item.getVALUE());
        parameter.put("pointSn", item.getPOINTID());
        parameter.put("gatewaySn", item.getSN());
        parameter.put("treatmentState", "1");

        Call<RealTimeDealAdviceModule> call = BaseAction.newInstance(false, true).saveDealWithAdvice(parameter);
        call.enqueue(new MyCallBack<RealTimeDealAdviceModule>(mContext) {
            @Override
            public void onSuccess(RealTimeDealAdviceModule response) {
                mActivity.cancelProgress();
                if (response.getResultCode().equals("1")) {
                    SnackbarUtil.ShortSnackbar(mActivity.getAvicityView(), "已提交", SnackbarUtil.INFO).show();
                } else {
                    SnackbarUtil.ShortSnackbar(mActivity.getAvicityView(), "提交失败", SnackbarUtil.INFO).show();
                }
            }

            @Override
            public void onFail(String message) {
                mActivity.cancelProgress();
                SnackbarUtil.ShortSnackbar(mActivity.getAvicityView(), message, SnackbarUtil.INFO).show();
            }
        });
    }


    DealWithAdviceDialog mDealWithAdviceDialog;

    public void showCustomDialog(Context context, final CustomDialogInterface customDialogInterface) {
        if (mDealWithAdviceDialog == null) {
            mDealWithAdviceDialog = new DealWithAdviceDialog(context, R.style.transparentFrameWindowStyle);
            mDealWithAdviceDialog.setCanceledOnTouchOutside(false);
            mDealWithAdviceDialog.setOnClickListener(new DealWithAdviceDialog.onClickListener() {
                @Override
                public void submitClickListener(DealWithAdviceDialog dialog, EditText editText) {
                    customDialogInterface.onCommitClick(dialog,editText);
                }

            });
            mDealWithAdviceDialog.show();
            //居中
            Window window = mDealWithAdviceDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            //设置宽度
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = MyApplication.W - CommonUtils.dip2px(context, 100);
            window.setAttributes(params);

        } else {
            mDealWithAdviceDialog.show();
        }
    }

    public interface CustomDialogInterface {
        void onCommitClick(DealWithAdviceDialog dialog, EditText editText);
    }
}
