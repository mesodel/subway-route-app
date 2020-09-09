package com.example.subwayproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WeatherActivity extends AppCompatActivity {

    DatePicker picker;

    public static Network net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        picker = findViewById(R.id.datePicker1);

        Intent intent = getIntent();

        net = new Network();
        try {
            net.execute(new URL("http://dataservice.accuweather.com/forecasts/v1/daily/5day/287430?apikey=ga9ynfDmEgUD1McSAd9ZD0Mn5gcadxhH"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void check(View view) {
        int month = picker.getMonth() + 1;

        String pickerResult = picker.getYear() + "-" + month + "-" + picker.getDayOfMonth();
        Date chosenDate = parseDatePicker(pickerResult);

        long noDays = chosenDate.getDate() - Calendar.getInstance().getTime().getDate();

        if (noDays > 4 || noDays < 0) {
            Snackbar.make(findViewById(R.id.relLayout), "You should select a date within 5 days", Snackbar.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(WeatherActivity.this, DailyWeatherActivity.class);
            intent.putExtra("date", (Serializable) chosenDate);

            startActivity(intent);
        }

    }

    private Date parseDatePicker(String input) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = sdf.parse(input);
        } catch (Exception e) {
            Log.e("parseDate", e.getMessage());
        }

        return date;
    }
}
