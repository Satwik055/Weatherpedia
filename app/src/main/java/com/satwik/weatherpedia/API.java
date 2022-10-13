package com.satwik.weatherpedia;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class API{
    //-------------------------------------------API-Call Attributes---------------------------------------------//
    String latitude = "24.7914";
    String longitude = "85.0002";
    String API_KEY = "e6b21d8047295278a759fe83b69f6f1b";
    String URL = "https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid="+API_KEY;



    //-----------------------------------------------Variables--------------------------------------------------//
    String weather;
    String feels_like;
    String temprature;
    String place;
    VolleyError volleyError;

//-----------------------------------------------Constructor--------------------------------------------------//
    public API(Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    //-----------------------------------------------Request--------------------------------------------------//
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {

                //Parsing weather
                JSONArray weatherArray = new JSONArray(response.getString("weather"));
                for(int i=0; i<weatherArray.length(); i++){
                    JSONObject jsonObject = weatherArray.getJSONObject(i);
                    String weather = jsonObject.getString("main");
                }

                //Parsing temprature (in Kelvin) ,place and feels-like
                String place = response.getString("name");
                String temprature_local = response.getJSONObject("main").getString("temp");
                temprature = temprature_local; //POE



                String feels_like = response.getJSONObject("main").getString("feels_like");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            volleyError = error;
        }
    });


    //-----------------------------------------------Functions--------------------------------------------------//
    public String getWeather() {return weather;}

    public String getFeels_like() {return feels_like;}

    public String getTemprature() {return temprature;}

    public String getPlace() {return place;}

    public String getVolleyError(){return volleyError.toString();}

}



