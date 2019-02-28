package com.zpxt.zhyd.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zpxt.zhyd.R;

/**
 * Description:      处理意见弹出框
 * Autour：          LF
 * Date：            2018/3/28 9:47
 */
public class DealWithAdviceDialog extends Dialog {

    Context mContext;
    public TextView mCancleTv, mSubmitTv;
    public EditText mAdviceEt;

    public DealWithAdviceDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_deal_with_advice);
        initViews();
    }

    private void initViews() {
        mCancleTv = findViewById(R.id.dealAdvice_cancleTv);
        mSubmitTv = findViewById(R.id.dealAdvice_submitTv);

        mAdviceEt = findViewById(R.id.dealAdvice_adviceEt);

        mCancleTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSubmitTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mListener != null) {
                    mListener.submitClickListener(DealWithAdviceDialog.this, mAdviceEt);
                }
            }
        });
    }

    public onClickListener mListener = null;

    public void setOnClickListener(onClickListener mListener) {
        this.mListener = mListener;
    }

    public interface onClickListener {
        void submitClickListener(DealWithAdviceDialog dialog, EditText editText);
    }
}
