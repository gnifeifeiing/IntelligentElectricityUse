package com.zpxt.zhyd.ui.report;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zpxt.zhyd.R;
import com.zpxt.zhyd.common.base.BaseActivity;
import com.zpxt.zhyd.model.HistoryListModule;
import com.zpxt.zhyd.views.MyXFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:      历史数据-折线图
 * Autour：          LF
 * Date：            2018/3/30 16:44
 */
public class MPChartActivity extends BaseActivity {

    private LineChart mChart;

    //折线图数据
    private HistoryListModule.ObjectModule mObjectModule;
    //折线图类别，0：电流趋势  1：温度趋势
    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpchart);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mType = bundle.getString("type");
            mObjectModule = (HistoryListModule.ObjectModule) bundle.getSerializable("objectModule");
        }
        initView();
        initChart();
    }

    private void initView() {
        mChart = findViewById(R.id.chart);
    }

    private void initChart() {
        mChart.getDescription().setEnabled(false);

        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        mChart.setPinchZoom(true);

        setData(20, 30);

        mChart.animateX(500);

        Legend l = mChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.parseColor("#999999"));
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.parseColor("#999999"));
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(Color.parseColor("#999999"));//设置x轴线颜色
        //电流趋势
        if (mType.equals("0")) {
            setActionBarTitle("电流趋势变化图");
            if(mObjectModule.getCurrentList()!=null&mObjectModule.getCurrentList().size()>0){
                List<HistoryListModule.ObjectModule.CurrentModule> currentModuleList = mObjectModule.getCurrentList();
                //自定义x轴显示
                String[] values = new String[currentModuleList.size()];
                for (int i = 0; i < currentModuleList.size(); i++) {
                    values[i] = currentModuleList.get(i).getTime().replace("\n"," ");
                }
                MyXFormatter formatter = new MyXFormatter(values);
                xAxis.setLabelCount(6);
                xAxis.setValueFormatter(formatter);
            }
        }else{
            setActionBarTitle("温度趋势变化图");
            if(mObjectModule.getTemperatureList()!=null&&mObjectModule.getTemperatureList().size()>0){
                List<HistoryListModule.ObjectModule.TemperatureListModule> temperatureListModules = mObjectModule.getTemperatureList();
                //自定义x轴显示
                String[] values = new String[temperatureListModules.size()];
                for (int i = 0; i < temperatureListModules.size(); i++) {
                    values[i] = temperatureListModules.get(i).getTime();
                }
                MyXFormatter formatter = new MyXFormatter(values);
                xAxis.setLabelCount(6);
                xAxis.setValueFormatter(formatter);
            }
        }

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(Color.parseColor("#999999"));
        leftAxis.setGridColor(Color.parseColor("#999999"));

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }


    private void setData(int count, float range) {
        //电流趋势
        if (mType.equals("0")&&mObjectModule.getCurrentList()!=null&&mObjectModule.getCurrentList().size()>0) {
            count = mObjectModule.getCurrentList().size();
            List<HistoryListModule.ObjectModule.CurrentModule> currentModuleList = mObjectModule.getCurrentList();

            ArrayList<Entry> AC = new ArrayList<Entry>();
            for (int i = 0; i < count; i++) {
                AC.add(new Entry(i, Float.parseFloat(currentModuleList.get(i).getA相电流())));
            }

            ArrayList<Entry> BC = new ArrayList<Entry>();
            for (int i = 0; i < count - 1; i++) {
                BC.add(new Entry(i, Float.parseFloat(currentModuleList.get(i).getB相电流())));
            }

            ArrayList<Entry> CC = new ArrayList<Entry>();
            for (int i = 0; i < count; i++) {
                CC.add(new Entry(i, Float.parseFloat(currentModuleList.get(i).getC相电流())));
            }

            LineDataSet set1, set2, set3;
            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
                set3 = (LineDataSet) mChart.getData().getDataSetByIndex(2);
                set1.setValues(AC);
                set2.setValues(BC);
                set3.setValues(CC);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                set1 = new LineDataSet(AC, "A相电流");

                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(ColorTemplate.getHoloBlue());
                set1.setCircleColor(Color.BLUE);
                set1.setFillColor(ColorTemplate.getHoloBlue());
                setLineDataSet(set1);

                set2 = new LineDataSet(BC, "B相电流");
                set2.setAxisDependency(YAxis.AxisDependency.LEFT);
                set2.setColor(Color.RED);
                set2.setCircleColor(Color.BLUE);
                set2.setFillColor(ColorTemplate.getHoloBlue());
                setLineDataSet(set2);

                set3 = new LineDataSet(CC, "C相电流");
                set3.setAxisDependency(YAxis.AxisDependency.LEFT);
                set3.setColor(Color.YELLOW);
                set3.setCircleColor(Color.BLUE);
                set3.setFillColor(ColorTemplate.getHoloBlue());
                setLineDataSet(set3);

                LineData data = new LineData(set1, set2, set3);
                data.setValueTextColor(Color.WHITE);
                data.setValueTextSize(9f);

                mChart.setData(data);
            }
        }else{
            if(mObjectModule.getCurrentList()!=null&&mObjectModule.getCurrentList().size()>0){
                count = mObjectModule.getTemperatureList().size();
                List<HistoryListModule.ObjectModule.TemperatureListModule> temperatureListModules = mObjectModule.getTemperatureList();

                ArrayList<Entry> AC = new ArrayList<Entry>();
                for (int i = 0; i < count; i++) {
                    AC.add(new Entry(i, Float.parseFloat(temperatureListModules.get(i).getA相温度())));
                }

                ArrayList<Entry> BC = new ArrayList<Entry>();
                for (int i = 0; i < count - 1; i++) {
                    BC.add(new Entry(i, Float.parseFloat(temperatureListModules.get(i).getB相温度())));
                }

                ArrayList<Entry> CC = new ArrayList<Entry>();
                for (int i = 0; i < count; i++) {
                    CC.add(new Entry(i, Float.parseFloat(temperatureListModules.get(i).getB相温度())));
                }

                ArrayList<Entry> DC = new ArrayList<Entry>();
                for (int i = 0; i < count; i++) {
                    DC.add(new Entry(i, Float.parseFloat(temperatureListModules.get(i).get箱体温度())));
                }

                LineDataSet set1, set2, set3,set4;
                if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                    set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                    set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
                    set3 = (LineDataSet) mChart.getData().getDataSetByIndex(2);
                    set4= (LineDataSet) mChart.getData().getDataSetByIndex(3);
                    set1.setValues(AC);
                    set2.setValues(BC);
                    set3.setValues(CC);
                    set4.setValues(DC);
                    mChart.getData().notifyDataChanged();
                    mChart.notifyDataSetChanged();
                } else {
                    set1 = new LineDataSet(AC, "A相电流");

                    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set1.setColor(ColorTemplate.getHoloBlue());
                    set1.setCircleColor(Color.BLUE);
                    set1.setFillColor(ColorTemplate.getHoloBlue());
                    setLineDataSet(set1);

                    set2 = new LineDataSet(BC, "B相电流");
                    set2.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set2.setColor(Color.RED);
                    set2.setCircleColor(Color.BLUE);
                    set2.setFillColor(ColorTemplate.getHoloBlue());
                    setLineDataSet(set2);

                    set3 = new LineDataSet(CC, "C相电流");
                    set3.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set3.setColor(Color.YELLOW);
                    set3.setCircleColor(Color.BLUE);
                    set3.setFillColor(ColorTemplate.getHoloBlue());
                    setLineDataSet(set3);

                    set4 = new LineDataSet(CC, "箱体温度");
                    set4.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set4.setColor(Color.GREEN);
                    set4.setCircleColor(Color.BLUE);
                    set4.setFillColor(ColorTemplate.getHoloBlue());
                    setLineDataSet(set4);

                    LineData data = new LineData(set1, set2, set3,set4);
                    data.setValueTextColor(Color.WHITE);
                    data.setValueTextSize(9f);

                    mChart.setData(data);
                }
            }

        }

    }

    /**
     * 设置两条折线的公共属性
     *
     * @param set
     */
    private void setLineDataSet(LineDataSet set) {
        //线模式为圆滑曲线（默认折线）
        //set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawCircles(false);//图表上的数据点是否用小圆圈表示
        set.setDrawValues(false);//隐藏折线图每个数据点的值
        set.setLineWidth(2f);
        set.setCircleRadius(3f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.GRAY);
        set.setDrawCircleHole(false);
    }

}
