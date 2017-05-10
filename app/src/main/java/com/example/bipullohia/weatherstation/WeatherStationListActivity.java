package com.example.bipullohia.weatherstation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static com.example.bipullohia.weatherstation.MainActivity.stationsArrayList;

public class WeatherStationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_station_list);

        ListView stationListView = (ListView) findViewById(R.id.stationListView);

        ArrayAdapter stationArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stationsArrayList);
        stationListView.setAdapter(stationArrayAdapter);

    }
}
