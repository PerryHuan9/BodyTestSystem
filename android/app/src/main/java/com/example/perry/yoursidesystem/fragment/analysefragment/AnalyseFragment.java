package com.example.perry.yoursidesystem.fragment.analysefragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.perry.yoursidesystem.MainActivity;
import com.example.perry.yoursidesystem.R;
import com.example.perry.yoursidesystem.database.UserBodyInfo;
import com.example.perry.yoursidesystem.test.LogUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by perry on 2017/12/15.
 */

public class AnalyseFragment extends Fragment {
    private LineChart bloodPreChart;
    private LineChart temperatureChart;
    private LineChart heartRateChart;
    private TextView longSuggestView;
    private List<UserBodyInfo> infoList;
    private List<Entry> systolicList;
    private List<Entry> diastolicList;
    private List<Entry> temperatureList;
    private List<Entry> heartRateList;
    private List<String> dateStrList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_analyse,
                container, false);
        bloodPreChart = view.findViewById(R.id.systolicChart);
        temperatureChart = view.findViewById(R.id.temperatureChart);
        heartRateChart = view.findViewById(R.id.heartRateChart);
        
        loaDBData();

//        IAxisValueFormatter formatter = new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return dateList.get((int)value).
//            }
//
//        };
//
//        XAxis xAxis = bloodPreChart.getXAxis();
//        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(formatter);

        return view;
    }

    private void setChart(LineChart chart,String descri) {
        Description description = new Description();
        description.setText(descri);
        chart.setDescription(description);
        chart.setNoDataText("没有数据哦");
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        //设置chart动画 
        chart.animateXY(1000, 1000);
//        //设置是否绘制chart边框的线  
//        mLineChart.setDrawBorders(true);
//        //设置chart边框线颜色  
//        mLineChart.setBorderColor(Color.GRAY);
//        //设置chart边框线宽度  
//        mLineChart.setBorderWidth(1f);

        // 像"□ xxx"就是图例  
        Legend legend = chart.getLegend();
        //设置图例显示在chart那个位置 setPosition建议放弃使用了  
        //设置垂直方向上还是下或中  
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //设置水平方向是左边还是右边或中  
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //设置所有图例位置排序方向  
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //设置图例的形状 有圆形、正方形、线  
        legend.setForm(Legend.LegendForm.CIRCLE);
        //是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter  
        legend.setWordWrapEnabled(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dateStrList.get((int) value);
            }
        });


    }

    private void loaDBData() {
        systolicList = new ArrayList<>();
        diastolicList = new ArrayList<>();
        temperatureList = new ArrayList<>();
        heartRateList = new ArrayList<>();
        dateStrList = new ArrayList<>();
        infoList = DataSupport.select("systolicPre", "diastolicPre",
                "temperature", "heartRate", "date").where("name=?", MainActivity
                .userName).find(UserBodyInfo.class);
        float i = 0f;
        if (infoList.size() <= 0) return;

//         * 显示最新的20个数据
        for (int j = /*infoList.size() > 15 ? infoList.size() - 15 :*/ 0; j < infoList
                .size(); j++) {
            UserBodyInfo info = infoList.get(j);
            systolicList.add(new Entry(i, info.getSystolicPre()));
            diastolicList.add(new Entry(i, info.getDiastolicPre()));
            temperatureList.add(new Entry(i, info.getTemperature()));
            heartRateList.add(new Entry(i, info.getHeartRate()));
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            dateStrList.add(format.format(info.getDate()));
            LogUtil.i("tag4", info.toString() + ",i=" + i);
            i++;
        }
        setChart(bloodPreChart,"收缩压:90-140,舒张压:60-90");
        LineDataSet systolicDataSet = new LineDataSet(systolicList, "扩张压");
        LineDataSet diastolicDtaSet = new LineDataSet(diastolicList, "收缩压");
        diastolicDtaSet.setColor(Color.WHITE);
        systolicDataSet.setColor(Color.BLACK);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(systolicDataSet);
        dataSets.add(diastolicDtaSet);
        LineData lineData = new LineData(dataSets);
        bloodPreChart.setData(lineData);
        
        setChart(temperatureChart,"正常体温：37℃");
        LineDataSet temperatureDS=new LineDataSet(temperatureList,"体温");
        LineData lineData1=new LineData(temperatureDS);
        temperatureChart.setData(lineData1);
        
        setChart(heartRateChart,"心率:40-160");
        LineDataSet heartRateDS=new LineDataSet(heartRateList,"心率");
        LineData lineData2=new LineData(heartRateDS);
        heartRateChart.setData(lineData2);
    }


}



