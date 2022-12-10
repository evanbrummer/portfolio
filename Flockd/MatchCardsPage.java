package com.example.flockd_frontend;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MatchCardsPage extends AppCompatActivity {

    String uri = "@drawable/xp";
    int imageResource;

    TextView matchTitle;
    Button btn_decline;
    Button btn_accept;
    String otherUser;
    String other_username_str;
    TextView other_username;
    TextView other_bio;
    int other_uID;
    ImageView other_pfp;
    Drawable res;
    int uID;
    RequestQueue mRequestQueue;

    public void openMatchListPage(View view) {
        finish();
        Intent openMatchList = new Intent(this, MatchList.class);
        startActivity(openMatchList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_cards_page);

        uID = Integer.parseInt(((userInformation)this.getApplication()).getUserID());

        matchTitle = findViewById(R.id.matchTitle);
        btn_decline = findViewById(R.id.btn_decline);
        btn_accept = findViewById(R.id.btn_accept);
        other_username = findViewById(R.id.other_user);
        other_pfp = findViewById(R.id.other_pfp);

        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        res = getResources().getDrawable(imageResource);
        other_bio = findViewById(R.id.other_bio);

        mRequestQueue = Volley.newRequestQueue(this);

        getNextMatch();
    }

    public void getNextMatch() {
        String nextURL = "http://coms-309-051.class.las.iastate.edu:8080/users/"
                + uID
                + "/pairings";

        JsonObjectRequest request;
        request = new JsonObjectRequest(Request.Method.POST, nextURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    otherUser = (response.getJSONObject("user1").getInt("id") == uID) ?
                            "user2" : "user1"; // identify if user1 or user2 is the other person

                    other_username.setText(response.getJSONObject(otherUser).getString("username"));
                    other_uID = response.getJSONObject(otherUser).getInt("id");
                    other_username_str = response.getJSONObject(otherUser).getString("username");

                    updatePhoto(other_username_str);
                    updateButtons(other_uID);
                    updateBio(other_username_str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Request error. You may be out of matches.", Toast.LENGTH_LONG).show();
                System.out.print(error.toString());
                other_pfp.setVisibility(View.GONE);
                matchTitle.setVisibility(View.INVISIBLE);
                btn_decline.setVisibility(View.INVISIBLE);
                btn_accept.setVisibility(View.INVISIBLE);
                other_username.setText("No matches.");
                other_bio.setVisibility(View.GONE);
            }
        });

        mRequestQueue.add(request);


    }

    public void updateButtons(int other_uID) {
        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineMatch(other_uID);
            }
        });
        btn_decline.setEnabled(true);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptMatch(other_uID);
            }
        });
        btn_accept.setEnabled(true);
    }

    public void updateBio(String username) {
        String bioURL = "http://coms-309-051.class.las.iastate.edu:8080/matchcard/"
                + username;
        System.out.println(bioURL);

        StringRequest bioRequest;
        bioRequest = new StringRequest(Request.Method.GET, bioURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
                String[] bio = response.toString().split("has bio: ");

                if (bio.length == 1) {
                    other_bio.setText("This user has no bio.");
                } else {
                    other_bio.setText(bio[bio.length - 1]);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        mRequestQueue.add(bioRequest);
    }

    public void updatePhoto(String username) {
        String url = "http://coms-309-051.class.las.iastate.edu:8080/matchcard/pic/";
        RequestQueue mRequestQueueImage;
        JsonObjectRequest mJsonRequestImage;

        mRequestQueueImage = Volley.newRequestQueue(this);

        mJsonRequestImage = new JsonObjectRequest(Request.Method.GET, url+username, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
                try {
                    String image = response.getString("map");
                    String[] myImage = image.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
                    System.out.println(Arrays.toString(myImage));
                    byte[] imageInByte = new byte[myImage.length];
                    for(int x = 0; x<myImage.length; x++){
                        imageInByte[x] = Byte.parseByte(myImage[x]);
                    }

                    Bitmap bmp = BitmapFactory.decodeByteArray(imageInByte, 0, imageInByte.length);
                    other_pfp.setImageBitmap(bmp);

                }catch(Exception e){
                    System.out.println("THIS DID NOT WORK IN LOGINPAGE IMAGE");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: Image was probably too big.");

                other_pfp.setImageDrawable(res);
                Log.i(TAG, "Error: " + error.toString());
            }
        });
        mRequestQueueImage.add(mJsonRequestImage);
    }

    public void declineMatch(int other_uID) {
        btn_accept.setEnabled(false);
        btn_decline.setEnabled(false);

        String unmatchURL = "http://coms-309-051.class.las.iastate.edu:8080/users/"
                + uID + "/matches/"
                + other_uID + "/unmatch";

        StringRequest request;
        request = new StringRequest(Request.Method.PUT, unmatchURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Response: " + response);
                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                getNextMatch();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        mRequestQueue.add(request);
    }

    public void acceptMatch(int other_uID) {
        btn_decline.setEnabled(false);
        btn_accept.setEnabled(false);

        String matchURL = "http://coms-309-051.class.las.iastate.edu:8080/users/"
                + uID + "/pairings/"
                + other_uID + "/match";

        StringRequest request;
        request = new StringRequest(Request.Method.PUT, matchURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response: " + response);
                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                getNextMatch();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        mRequestQueue.add(request);
    }
}