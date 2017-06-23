package com.example.thierry.archiverts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by Thierry on 22.06.2017.
 */

public class ChartActivity extends MainActivity implements OnChartValueSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pie_chart);

        // Get the facette
        Intent i = getIntent();
        //Facette a = (Facette)i.getSerializableExtra("facette");

        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        //ArrayList<Entry> yvalues = new ArrayList<Entry>();
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        int lengthFacette = 10;

        for(int j=0; j < lengthFacette; j++)
        {
            yvalues.add(new PieEntry(j, j));
            xVals.add("January");
        }

        //PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");
        PieDataSet dataSet = new PieDataSet(yvalues, "Facettes Result");
        /*
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("January");
        xVals.add("February");
        xVals.add("March");
        xVals.add("April");
        xVals.add("May");
        xVals.add("June");
        */
        //PieData data = new PieData(xVals, dataSet);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        //pieChart.setDescription("This is Pie Chart");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(25f);
        pieChart.setHoleRadius(25f);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);

    }

    //@Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + dataSetIndex);
    }


    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {

        if (entry == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + entry.getY() + ", xIndex: " + entry.getX());
    }

    //@Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}