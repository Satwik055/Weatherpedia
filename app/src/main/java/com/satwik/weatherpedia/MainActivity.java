package com.satwik.weatherpedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------------------------Variables---------------------------------------------//
        String latitude = "24.7914";
        String longitude = "85.0002";
        String API_KEY = "e6b21d8047295278a759fe83b69f6f1b";
        String URL = "https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid="+API_KEY;

        //-------------------------------------------Hooks----------------------------------------------//
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        TextView textView_place = findViewById(R.id.textView_place);
        TextView textView_weather = findViewById(R.id.textView_weather);
        TextView textView_temprature = findViewById(R.id.textView_temprature);
        TextView textView_feels_like = findViewById(R.id.textView_feels_like);
        navigationView.setNavigationItemSelectedListener(this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
            @Override

            public void onResponse(JSONObject response) {
                try {
                    //Parsing place
                    String place = response.getString("name");
                    textView_place.setText(place);

                    //Parsing weather
                    JSONArray weatherArray = new JSONArray(response.getString("weather"));
                    for(int i=0; i<weatherArray.length(); i++){
                        JSONObject jsonObject = weatherArray.getJSONObject(i);
                        String weather = jsonObject.getString("main");
                        textView_weather.setText(weather);
                    }
                    //Parsing temprature (in Kelvin) and feels-like
                    String temprature = response.getJSONObject("main").getString("temp");
                    textView_temprature.setText(toCelsius(temprature) + "°");
                    String feels_like = response.getJSONObject("main").getString("feels_like");
                    textView_feels_like.setText("Feels like " + toCelsius(feels_like) + "°");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.d("Error Occured", ""+error);
            }
        });

        requestQueue.add(jsonObjectRequest);

        navigationView.setCheckedItem(R.id.weather);
    }

    //-------------------------------------------Functions----------------------------------------------//
    public String toCelsius(String temp_in_kelvin){
        double double_celsius = Double.parseDouble(temp_in_kelvin) - 273.15;
        String string_celsius = Double.toString(double_celsius);
        return string_celsius.substring(0, string_celsius.indexOf("."));
    }

    public void openNavDrawer(View view){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.END);
    }

    public void closeNavDrawer(View view){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.weather:
                break;
            case R.id.settings:
                break;
            case R.id.invite:
                break;
            case R.id.contactus:
                break;
            case R.id.about:
                break;
        }
        return false;
    }
}