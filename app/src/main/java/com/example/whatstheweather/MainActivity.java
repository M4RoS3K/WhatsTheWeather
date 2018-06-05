package com.example.whatstheweather;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    protected String APIKey = "e09bb3372e629954725a8095a8e46b34";
    protected EditText txt_city;
    protected TextView txt_output;

    public void getWeather(View view){
        DownloadTask task = new DownloadTask();
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        // to hide keyboard, when this method is called
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(txt_city.getWindowToken(), 0);

        try {
            if (txt_city.getText().toString().equals("")) {
                txt_city.startAnimation(shake);
            } else {
                String city = URLEncoder.encode(txt_city.getText().toString(), "UTF-8");
                String json = task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + APIKey).get();
                JSONObject jObj = new JSONObject(json);
                String weatherData = jObj.getString("weather");
                JSONArray arrWeather = new JSONArray(weatherData);
                String message = "";

                for(int i = 0; i < arrWeather.length(); i++){
                    JSONObject jPart = arrWeather.getJSONObject(i);

                    String main = jPart.getString("main");
                    String description = jPart.getString("description");

                    if(!main.equals("") && !description.equals("")){
                        message += main + ": " + description + "\r\n";
                    }
                }

                Log.i("json output", json);
                Log.i("weather data", weatherData);

                if(!message.equals("")) {
                    txt_output.setText(message);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Invalid city! Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_city = (EditText) findViewById(R.id.txt_city);
        txt_output = (TextView) findViewById(R.id.txt_output);
    }
}