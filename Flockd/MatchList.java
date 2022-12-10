package com.example.flockd_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Array;
import java.util.ArrayList;

public class MatchList extends AppCompatActivity {

    RequestQueue mRequestQueue;
    int uID;

    public void backButtonToMainScreen(View view){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        mRequestQueue = Volley.newRequestQueue(this);

        TextView noMatches = findViewById(R.id.NoMatches);
        RecyclerView matchListRV = findViewById(R.id.MatchListRV);
        uID = Integer.parseInt(((userInformation)this.getApplication()).getUserID());

        ArrayList<MatchListModel> matchListArrayList = new ArrayList<MatchListModel>();
        String matchesURL = "http://coms-309-051.class.las.iastate.edu:8080/users/"
                + uID + "/matches"; // SHOULD BE MATCHES

        MatchListAdapter matchListAdapter = new MatchListAdapter(this, matchListArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        JsonArrayRequest request;
        request = new JsonArrayRequest(Request.Method.GET, matchesURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.length());
                if (response.length() == 0) {
                    noMatches.setText("No matches.");
                }

                String otherUser;
                String temp_username;
                int temp_userID;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        otherUser = (response.getJSONObject(i).getJSONObject("user1").getInt("id") == uID) ?
                                "user2" : "user1";

                        temp_username = response.getJSONObject(i).getJSONObject(otherUser).getString("username");
                        temp_userID = response.getJSONObject(i).getJSONObject(otherUser).getInt("id");

                        matchListArrayList.add(new MatchListModel(null, temp_username, temp_userID));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                matchListRV.setLayoutManager(linearLayoutManager);
                matchListRV.setAdapter(matchListAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
            }
        });
        mRequestQueue.add(request);

        matchListRV.setLayoutManager(linearLayoutManager);
        matchListRV.setAdapter(matchListAdapter);

        // Methods so nice we call them twice >:)

    }

    public void openChatScreen(View view) {
        finish();
        Intent openChatScreen = new Intent(this, ChatScreen.class);
        startActivity(openChatScreen);
    }

    public void openMatchCards(View view) {
        finish();
        Intent openMatchCards = new Intent(this, MatchCardsPage.class);
        startActivity(openMatchCards);
    }
}

