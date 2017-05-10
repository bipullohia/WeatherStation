package com.example.bipullohia.weatherstation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText zipcodeEdittext;
    public static ArrayList<String> stationsArrayList = new ArrayList<>();
    public static ArrayList<String> cityArrayList = new ArrayList<>();
    public static ArrayList<String> stateArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipcodeEdittext = (EditText) findViewById(R.id.zipcodeEdittext);
        Button findStationButton = (Button) findViewById(R.id.findstationButton);

        findStationButton.setOnClickListener(new View.OnClickListener() {  //This is called when findStation button is clicked

            @Override
            public void onClick(View view) {

                fetchWeatherStations();
            }
        });


    }

    private void fetchWeatherStations() {

        if (!String.valueOf(zipcodeEdittext.getText()).matches("")) {

            String zipcode = String.valueOf(zipcodeEdittext.getText());
            Log.i("hey there", zipcode);

            GeolookupStations geolookupStations = new GeolookupStations();
            geolookupStations.execute("http://api.wunderground.com/api/95c4a38f0f378fe6/geolookup/q/" + zipcode + ".json");

            Log.i("url", "http://api.wunderground.com/api/95c4a38f0f378fe6/geolookup/q/" + zipcode + ".json");

        } else {

            Toast.makeText(this, "Blank input found!", Toast.LENGTH_SHORT).show();
        }
    }

    private class GeolookupStations extends AsyncTask<String, Void, String> {

        String res = "";
        ProgressDialog pd = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Loading List of Stations...");
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

                JSONObject jo = new JSONObject(result);

//                    JSONObject job = jo.getJSONObject("location");
//                    JSONObject jobb = job.getJSONObject("nearby_weather_stations");
//                    JSONObject jobbb = jobb.getJSONObject("pws");

                JSONObject stationsJOB = jo.getJSONObject("location").getJSONObject("nearby_weather_stations").getJSONObject("pws");
                JSONArray jsonArray = stationsJOB.getJSONArray("station");

                for(int i=0; i<jsonArray.length(); i++){

                    Log.i("job", String.valueOf(jsonArray.getJSONObject(i)));

                    stationsArrayList.add(i, jsonArray.getJSONObject(i).getString("neighborhood"));
                    cityArrayList.add(i, jsonArray.getJSONObject(i).getString("city"));
                    stateArrayList.add(i, jsonArray.getJSONObject(i).getString("state"));

                }

                Log.i("pwsJSONObject", String.valueOf(stationsJOB));
                Log.i("stationJSONArray", String.valueOf(jsonArray));

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {

            Intent intent = new Intent(getApplicationContext(), WeatherStationListActivity.class);
            startActivity(intent);

            pd.dismiss();


        }
    }
}
