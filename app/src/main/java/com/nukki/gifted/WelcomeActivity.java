package com.nukki.gifted;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class WelcomeActivity extends AppCompatActivity {
    public static final String prefs_name = "MyPreferencesFile";
    private String urlJsonObj; //= "http://api.giphy.com/v1/gifs/trending?api_key=dc6zaTOxFJmzC";
    private ArrayList<String> links;
    private String gif_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        links = new ArrayList<>();
        getSettings();
        if (isOnline()){
            makeRequest();
        } else {
            Intent intent = new Intent(WelcomeActivity.this, ErrorActivity.class);
            intent.putExtra("problem", "Internet");
            startActivity(intent);
        }
    }


    private void getSettings() {
        SharedPreferences settings = getSharedPreferences(prefs_name, 0);
        gif_pref = settings.getString("setting", "Trending");

        String beginning = "http://api.giphy.com/v1/gifs/";
        String api_info = "api_key=dc6zaTOxFJmzC";
        if (gif_pref.equals("Trending")){
            urlJsonObj = beginning + "trending?" + api_info;
        }
        if (gif_pref.equals("Random")){
            urlJsonObj = beginning + "random?" + api_info + "&tag=reaction";
        }
        if (gif_pref.equals("Custom")){
            urlJsonObj = beginning + "search?q=" + settings.getString("search_phrase", "pug") + "&" + api_info;
        }
    }


    private void makeRequest() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CONNECTIVITY SHIT", response.toString());
                try {
                    if (!gif_pref.equals("Random")) {
                        JSONArray data = response.getJSONArray("data");
                        String giffy = "";
                        for (int i = 0; i < 21; i++) {
                            giffy = data.getJSONObject(i).getJSONObject("images").getJSONObject("original").getString("url");
                            Log.i("*************** GIFFY", giffy);
                            links.add(giffy);
                        }
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        intent.putStringArrayListExtra("urls", links);
                        startActivity(intent);
                    } else {
                        String resp = response.getJSONObject("data").getString("image_original_url");
                        Log.i("****** RANDOM ", resp);
                        Intent intent = new Intent(WelcomeActivity.this, RandomActivity.class);
                        intent.putExtra("url", resp);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent intent = new Intent(WelcomeActivity.this, ErrorActivity.class);
                    intent.putExtra("problem", "JSON");
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }); // end of creating json object

        // Adding request to request queue
        AppController.getInstance(WelcomeActivity.this).addToRequestQueue(jsonObjReq);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
