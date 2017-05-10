package com.example.bipullohia.weatherstation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.example.bipullohia.weatherstation.MainActivity.cityArrayList;
import static com.example.bipullohia.weatherstation.MainActivity.stateArrayList;
import static com.example.bipullohia.weatherstation.MainActivity.stationsArrayList;

public class WeatherStationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_station_list);

        setTitle("List of Weather Stations");

        ListView stationListView = (ListView) findViewById(R.id.stationListView);

        ArrayAdapter stationArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stationsArrayList);
        stationListView.setAdapter(stationArrayAdapter);

        stationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                FetchWeather fetch = new FetchWeather();

                String state = stateArrayList.get(i);
                String city = cityArrayList.get(i);
                String cityUtf = "";

                try {

                    cityUtf = URLEncoder.encode(city, "UTF-8").replace("+", "%20");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String url = "http://api.wunderground.com/api/95c4a38f0f378fe6/forecast/q/" + state + "/" + cityUtf + ".json";

                Log.i("urll", url);
                fetch.execute(url);
            }
        });

    }

    private class FetchWeather extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(WeatherStationListActivity.this);
        String resultData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Loading current weather...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {

                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;
                    result += current;

                    data = reader.read();

                }

                JSONObject job = new JSONObject(result);
                JSONArray jsonArray = job.getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday");

                JSONObject jo = jsonArray.getJSONObject(0);
                resultData = String.valueOf(jo);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {

            Intent intent = new Intent(getApplicationContext(), StataionResultActivity.class);
            intent.putExtra("resultData", resultData);
            startActivity(intent);

            pd.dismiss();

        }
    }

}