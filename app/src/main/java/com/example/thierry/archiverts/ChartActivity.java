package com.example.thierry.archiverts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.*;

/**
 * Created by Thierry on 22.06.2017.
 */

public class ChartActivity extends MainActivity implements OnChartValueSelectedListener {

    private String searchString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pie_chart);

        // Get the facette from MainActivity
        ArrayList<String> facette_for_pie = new ArrayList<String>();
        Intent ident = getIntent();
        facette_for_pie = ident.getStringArrayListExtra("facette_for_pie");
        // Get searchString
        searchString = ident.getStringExtra("searchString");

        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        String[] stringArray = facette_for_pie.toArray(new String[facette_for_pie.size()]);

        int[] arrayOfValues = new int[facette_for_pie.size()/2];
        int total = 0;
        // Extract the number of articles by program
        for(int i = 0;i < stringArray.length/2;i++)
        {
            try
            {
                //Structure of stringArray
                arrayOfValues[i] = Integer.parseInt(stringArray[i*2+1]);
                total = total + arrayOfValues[i]; //Sum of the values
            }
            catch(Exception e)
            {
                System.out.println("Not an integer value");
            }
        }

        Iterator<String> it = facette_for_pie.iterator();
        int j = 0, filtered_value = 0;
				
        double pourcent = 3;

        for (j= 0; j < arrayOfValues.length;  j++){
           //Prepare the values to show
           if (arrayOfValues[j] > (total*(pourcent/100))) { //Filter the value less than x% of the total
                String prog = it.next().toString();
                xVals.add(prog);
                String percent = it.next().toString();
                yvalues.add(new PieEntry(arrayOfValues[j], prog));
           }
           else{ // value less than x% of the total
               filtered_value = filtered_value + arrayOfValues[j];
               it.next(); //jump the value and program name
               it.next();
            }
        }
        if(filtered_value > 0) {
            String prog = "Other";
            xVals.add(prog); //other is the addition of the filtered values
            yvalues.add(new PieEntry(filtered_value, prog));
        }
        //Set the legend
        PieDataSet dataSet = new PieDataSet(yvalues, "Emissions");
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);

        Description description = new Description();
        description.setText("Liste des émissions");
        pieChart.setDescription(description);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(25f);
        pieChart.setHoleRadius(25f);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setOnChartValueSelectedListener(this);

		Legend l = pieChart.getLegend();
        l.setEnabled(false);

        // entry label styling
        data.setValueTextColor(Color.BLACK);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(1f);
        dataSet.setValueLinePart2Length(.1f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.animateXY(1900, 1900); //1400
    }

    // When click on a slice
    public void onValueSelected(Entry entry, Highlight highlight) {

        if (entry == null)
            return;
        // Cast in PieEntry -> so we can use getLabel()
        PieEntry pe = (PieEntry) entry;
        Log.i("VAL SELECTED entry",
                "Value: " + entry.getY() + ", xIndex: " + entry.getX() + ", label: " + pe.getLabel());
        // Return to main activity and filter a new search
        Intent resultIntent = new Intent();
        resultIntent.putExtra("keyword", searchString);
        resultIntent.putExtra("program", pe.getLabel());
        setResult(Activity.RESULT_OK, resultIntent);
        // finish activity
        finish();
    }

    // When use back button - finish the activity but no new search
    @Override
    public void onBackPressed()
    {
        finish();
    }

    //@Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}