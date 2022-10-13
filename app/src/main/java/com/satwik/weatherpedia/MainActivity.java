package com.satwik.weatherpedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------------------------Variables---------------------------------------------//
        String latitude = "24.7914";
        String longitude = "85.0002";
        String API_KEY = "e6b21d8047295278a759fe83b69f6f1b";
        String URL = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";

        //-------------------------------------------Hooks----------------------------------------------//
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        TextView textView_place = findViewById(R.id.textView_place);
        TextView textView_weather = findViewById(R.id.textView_weather);
        TextView textView_temprature = findViewById(R.id.textView_temprature);
        TextView textView_feels_like = findViewById(R.id.textView_feels_like);
        ImageView imageView_weatherImage = findViewById(R.id.imageView_weatherImage);
        TextView textView_humidity = findViewById(R.id.textView_humidity);
        TextView textView_windSpeed = findViewById(R.id.textView_windSpeed);


        //-------------------------------------------JSON-Request---------------------------------------------//
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
            @Override

            public void onResponse(JSONObject response) {
                try {
                    //Parsing place
                    String place = response.getString("name");
                    textView_place.setText(place);

                    //Parsing weather
                    JSONArray weatherArray = new JSONArray(response.getString("weather"));
                    for (int i = 0; i < weatherArray.length(); i++) {
                        JSONObject jsonObject = weatherArray.getJSONObject(i);
                        String weather = jsonObject.getString("main");
                        textView_weather.setText(weather);
                        setWeatherImage(imageView_weatherImage, weather);
                    }
                    //Parsing temprature, feels-like, wind-speed and humidity
                    String temprature = response.getJSONObject("main").getString("temp");
                    textView_temprature.setText(getRoundOff(temprature) + "°");
                    String feels_like = response.getJSONObject("main").getString("feels_like");
                    textView_feels_like.setText("Feels like " + getRoundOff(feels_like) + "°");
                    String humidity = response.getJSONObject("main").getString("humidity");
                    textView_humidity.setText(humidity + "%");
                    String windSpeed = response.getJSONObject("wind").getString("speed");
                    textView_windSpeed.setText(windSpeed + "km/h");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message;
                if(error instanceof ServerError){
                    message = "Server-side error, Please contact developer";
                }
                else{
                    message = "Something went wrong";
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                Log.d("Error Occurred", "" + error);


            }
        });

        //----------------------------------------Direct-Function-Calls------------------------------------------//
        //Checking Internet
        if (!checkConnection()){
            Intent intent = new Intent(MainActivity.this, NoConnection_Activity.class);
            startActivity(intent);
        }

        // adding json request to queue
        requestQueue.add(jsonObjectRequest);

        //-------------------------------------------Nav-Drawer OnClick----------------------------------------------//
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this, "Boom", Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case R.id.about:
                        Toast.makeText(MainActivity.this, "Viola", Toast.LENGTH_SHORT).show();
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Lola", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }

    //------------------------------------------------Functions---------------------------------------------------//
    public String getRoundOff(String string_value) {
        return string_value.substring(0, string_value.indexOf("."));
    }

    public void openNavDrawer(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.END);
    }

    public void closeNavDrawer(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    public void setWeatherImage(ImageView img, String weather) {
        switch (weather) {
            case "Clouds":
                img.setImageResource(R.drawable.ic_weather_clouds);
                break;
            case "Rain":
                img.setImageResource(R.drawable.ic_weather_rain);
                break;
            case "Mist":
                img.setImageResource(R.drawable.ic_weather_mist);
                break;
            case "Thunderstorm":
                img.setImageResource(R.drawable.ic_weather_thunderstrom);
                break;
            case "Haze":
                img.setImageResource(R.drawable.ic_weather_haze);
                break;
            default:
                img.setImageResource(R.drawable.ic_weather_default);
        }

    }

    public boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

//
}
