package com.example.flockd_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LeaderboardPage extends AppCompatActivity {

    TextView topTen;
    String URL = "http://coms-309-051.class.las.iastate.edu:8080/leaderboard";
    RequestQueue mRequestQueue;
    StringRequest request;

    String[] usernames;
    String responseStr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.leaderboard_page);

            mRequestQueue = Volley.newRequestQueue(this);

            topTen = (TextView) findViewById(R.id.rankList);
            updateString(topTen);
    }

    public void updateString(TextView t) {
        t.setText("Loading...");

        request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                t.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        });

        mRequestQueue.add(request);
    }

    public void backButtonToMainScreen(View view){
        finish();

//        Intent openMainActivity = new Intent(this, MainActivity.class);
//        startActivity(openMainActivity);
    }
}