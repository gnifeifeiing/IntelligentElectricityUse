package com.zpxt.zhyd.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zpxt.zhyd.R;

public class DownloadDialog extends Dialog {

	Context context;
	public  static TextView tv_content,uptv_title;
	private String cancel;
	public String content;
	private String title;
	private View view_v;
	public  static WDSeekBar wdSeekBar;

	public DownloadDialog(Context context, int theme, String cancel, TextView tv_content , String title, WDSeekBar bar) {
		super(context, theme);
		this.context = context;
		this.cancel = cancel;
		this.tv_content = tv_content;
		this.title = title;
		this.wdSeekBar=bar;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_download);
		initViews();
	}

	private void initViews() {
		tv_content = (TextView) findViewById(R.id.dialog_content);
		tv_content.setText(this.content);

		wdSeekBar = (WDSeekBar) findViewById(R.id.pb_product_progress);
		uptv_title = (TextView) findViewById(R.id.dialog_title);
		uptv_title.setText(this.title);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if (mListener != null) {
			mListener.dismiss();
		}
	}

	public UpdateOnclickListener mListener = null;

	public void setUpdateOnClickListener(UpdateOnclickListener mListener) {
		this.mListener = mListener;
	}

	public interface UpdateOnclickListener {
		public void dismiss();

		public void BtnYesOnClickListener(View v);

		public void BtnCancleOnClickListener(View v, WDSeekBar bar);
	}
	
}
