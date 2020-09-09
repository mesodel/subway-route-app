package com.example.subwayproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyWeatherActivity extends AppCompatActivity {
    private JSONArray weather;
    Date chosenDate;

    private float minTemp;
    private float maxTemp;
    private String text;

    private TextView tvText;
    private TextView tvTemp;
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);
        Intent intent = getIntent();
        chosenDate = (Date) intent.getSerializableExtra("date");

        try {
            weather = WeatherActivity.net.getResult().getJSONArray("DailyForecasts");
            text = WeatherActivity.net.getResult().getJSONObject("Headline").getString("Text");
        } catch (JSONException e) {
            Log.e("JSONError",e.getMessage());
        }

        parseJson(weather);

        tvText = findViewById(R.id.tvText);
        tvTemp = findViewById(R.id.tvTemp);
        tvDate=findViewById(R.id.tvDate);

        tvTemp.setText(minTemp + "°C - " + maxTemp+"°C");
        tvText.setText(text);
        String date=makeDate(chosenDate);
        tvDate.setText(date);

    }

    private void parseJson(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject current = jsonArray.getJSONObject(i);

                String date = current.getString("Date");

                Date respDate = parseDateResponse(date);

                if (respDate.getDate() == chosenDate.getDate() && respDate.getMonth() == chosenDate.getMonth() && respDate.getYear() == chosenDate.getYear()) {
                    i = jsonArray.length();

                    minTemp = current.getJSONObject("Temperature").getJSONObject("Minimum").getInt("Value");
                    minTemp = (float) ((minTemp - 32) / 1.8);

                    minTemp = (float) Math.floor(minTemp);

                    maxTemp = current.getJSONObject("Temperature").getJSONObject("Maximum").getInt("Value");
                    maxTemp = (float) ((maxTemp - 32) / 1.8);

                    maxTemp = (float) Math.floor(maxTemp);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Date parseDateResponse(String input) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

        try {
            date = sdf.parse(input);
        } catch (Exception e) {
            Log.e("parseDate", e.getMessage());
        }

        return date;
    }

    private String makeDate(Date date) {
        String day = (String) DateFormat.format("dd", date);
        String monthNumber = (String) DateFormat.format("MM", date);
        String year = (String) DateFormat.format("yyyy", date);

        return day + "-" + monthNumber + "-" + year;
    }
}
