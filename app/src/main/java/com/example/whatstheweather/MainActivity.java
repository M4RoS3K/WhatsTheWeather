package com.example.whatstheweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView txt_city;
    private TextView txt_output;

    public void getWeather(View view){

        DownloadTask task = new DownloadTask();
        final String APIKey = "e09bb3372e629954725a8095a8e46b34";
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        if (txt_city.getText().length() == 0){
            txt_city.startAnimation(shake);
        } else {
            try{
                String city = txt_city.getText().toString();
                String json = task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APIKey).get();
                JSONObject JObj = new JSONObject(json);
                String weatherData = JObj.getString("weather");
                Log.i("weather data", weatherData);
                JSONArray arr = new JSONArray(weatherData);
                String main = "";
                String description = "";


                for(int i = 0; i < arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");
                }

                txt_output.setText("main: " + main + "\ndescription: " + description);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_city = (TextView) findViewById(R.id.txt_city);
        txt_output = (TextView) findViewById(R.id.txt_output);
    }
}