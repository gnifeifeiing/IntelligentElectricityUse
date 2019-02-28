package com.zpxt.zhyd.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zpxt.zhyd.R;

/**
 * Description:      温馨提示对话框
 * Autour：          LF
 * Date：            2018/4/3 14:32
 */
public class OneSureDialog extends Dialog {

    Context context;
    public TextView btn_yes;
    private TextView tv_content, uptv_title;
    private String ok;
    private String content;
    private String title;
    private View view_v;

    public OneSureDialog(Context context, int theme, String ok, String content, String title) {
        super(context, theme);
        this.context = context;
        this.ok = ok;
        this.content = content;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_one_sure);
        initViews();
    }

    private void initViews() {
        btn_yes = findViewById(R.id.btn_ok);
        btn_yes.setText(this.ok);
        view_v = findViewById(R.id.view_v);

        tv_content = findViewById(R.id.dialog_content);
        tv_content.setText(this.content);
        uptv_title = findViewById(R.id.dialog_title);
        if (this.title == null) {
            uptv_title.setVisibility(View.GONE);
        } else {
            uptv_title.setVisibility(View.VISIBLE);
            uptv_title.setText(this.title);
        }
        btn_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.BtnYesOnClickListener(OneSureDialog.this);
                }
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public UpdateOnclickListener mListener = null;

    public void setOnClickListener(UpdateOnclickListener mListener) {
        this.mListener = mListener;
    }

    public interface UpdateOnclickListener {
        void BtnYesOnClickListener(OneSureDialog dialog);
    }

}
