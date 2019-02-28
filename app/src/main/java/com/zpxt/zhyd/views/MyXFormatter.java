package com.zpxt.zhyd.views;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Description:      MpAndroidChart自定义x轴显示
 * Autour：          LF
 * Date：            2017/9/26 17:52
 */

public class MyXFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public MyXFormatter(String[] values) {
        this.mValues = values;
    }
    private static final String TAG = "MyXFormatter";

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        Log.d(TAG, "----->getFormattedValue: "+value);
        return mValues[(int) value % mValues.length];
    }
}
