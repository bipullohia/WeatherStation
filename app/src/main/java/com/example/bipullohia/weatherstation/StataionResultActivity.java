package com.example.bipullohia.weatherstation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class StataionResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stataion_result);

        TextView conditionsTextView, humidityTextView, maxTempTextView, minTempTextView, maxWindTextView, aveWindTextView;

        conditionsTextView = (TextView) findViewById(R.id.conditions);
        humidityTextView = (TextView) findViewById(R.id.humidity);
        maxTempTextView = (TextView) findViewById(R.id.maxTemp);
        minTempTextView = (TextView) findViewById(R.id.minTemp);
        maxWindTextView = (TextView) findViewById(R.id.maxWind);
        aveWindTextView = (TextView) findViewById(R.id.aveWind);


        setTitle("Weather Details");

        String resultData = getIntent().getStringExtra("resultData");

        try {

            JSONObject jo = new JSONObject(resultData);

            String conditions = jo.getString("conditions");
            String humidity = jo.getString("avehumidity");
            String maxTemp = jo.getJSONObject("high").getString("fahrenheit");
            String minTemp = jo.getJSONObject("low").getString("fahrenheit");
            String maxWind = jo.getJSONObject("maxwind").getString("mph");
            String maxWindDir = jo.getJSONObject("maxwind").getString("dir");
            String aveWind = jo.getJSONObject("avewind").getString("mph");
            String aveWindDir = jo.getJSONObject("avewind").getString("dir");

            Log.i("details", conditions + humidity + maxTemp + minTemp + maxWind + maxWindDir + aveWind + aveWindDir);

            conditionsTextView.setText(conditions);
            humidityTextView.setText(humidity);
            maxTempTextView.setText(maxTemp + " F");
            minTempTextView.setText(minTemp + " F");
            maxWindTextView.setText(maxWind + " mph " + maxWindDir);
            aveWindTextView.setText(aveWind + " mph " + aveWindDir);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
