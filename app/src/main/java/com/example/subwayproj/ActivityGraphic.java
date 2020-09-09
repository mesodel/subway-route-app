package com.example.subwayproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityGraphic extends AppCompatActivity {
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);
        Intent intent = getIntent();
        barChart = (BarChart) findViewById(R.id.bargraph);
        ArrayList<BarEntry> barEntries = new ArrayList<>();


        List<Magistrala> magistrale = MainActivity.database.magistralaDao().getAll();
        List<Integer> counters = new ArrayList<>();
        for (int i = 0; i < magistrale.size(); i++)
            counters.add(magistrale.get(i).getStations().size());
        barEntries.add(new BarEntry(44f, counters.get(0)));
        barEntries.add(new BarEntry(88f, counters.get(1)));
        barEntries.add(new BarEntry(66f, counters.get(2)));
        barEntries.add(new BarEntry(12f, counters.get(3)));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Subway stations");

        ArrayList<String> linesNames = new ArrayList<>();
        for (int i = 0; i < magistrale.size(); i++)
            linesNames.add(magistrale.get(i).getNameMagistrala());

        BarData theData = new BarData(barDataSet);

        barChart.setData(theData);

        //barChart.setTouchEnabled(true);
    }


}
