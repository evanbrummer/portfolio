package com.example.flockd_frontend;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminConsole extends AppCompatActivity {

    WebSocketClient mWebSocketClient;
    RequestQueue mRequestQueue;

    int uID;
    String username;

    EditText mInput;
    TextView chatText;
    Button btn_send;
    TextView header;
    RelativeLayout chatBox;
    String result;

    public void backToHome(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_console);

        mRequestQueue = Volley.newRequestQueue(this);

        uID = Integer.parseInt(((userInformation)this.getApplication()).getUserID());
        username = ((userInformation) this.getApplication()).getUserName();

        mInput = findViewById(R.id.edit_gchat_message);
        chatText = findViewById(R.id.chatText);
        chatText.setMovementMethod(new ScrollingMovementMethod());
        chatText.invalidate();
        chatText.requestLayout();
        btn_send = findViewById(R.id.button_gchat_send);

        header = findViewById(R.id.otherUserHeader);
        chatBox = findViewById(R.id.layout_gchat_chatbox);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEnteredMessage();
            }
        });

        mInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    btn_send.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void sendEnteredMessage() {
        String message = mInput.getText().toString();

        getResponse(message);

        mInput.getText().clear();
        chatText.invalidate();
        chatText.requestLayout();
    }

    private void getResponse(String msg) {
        result = "";

        if (msg.equals("/help")) {
            result += ("\nAll available commands: \n" + getAllCommands());
            chatText.append(result + "\n");
        }
        else if (msg.contains("/getid ")) {
            String user = msg.split(" ")[1];
            String URL = "http://coms-309-051.class.las.iastate.edu:8080/users/" + user;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    int id;
                    try {
                        id = response.getInt("id");
                        result += "\n" + user + ": " + id;
                        chatText.append(result + "\n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error: couldn't get id");
                    result += "Couldn't find user \"" + user + "\"";
                    chatText.append(result + "\n");
                }
            });
            mRequestQueue.add(jsonObjectRequest);
        }
        else if (msg.contains("/ban ")) {
            String user = msg.substring(5);
            String URL = "http://coms-309-051.class.las.iastate.edu:8080/users/" + user;

            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    int id;
                    if (response.contains("User deleted")) {
                        result += "\n" + user + ": " + response;
                        chatText.append(result + "\n");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    result += "\nDeletion of " + user + " failed!";
                    chatText.append(result + "\n");
                }
            });
            mRequestQueue.add(stringRequest);
        }
        else if (msg.contains("/op ")) {
            String user = msg.substring(4);
            String URL = "http://coms-309-051.class.las.iastate.edu:8080/users/" +
                    "admin/changetype/" + user + "/3";

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    if (response.contains("true")) {
                        result += "\n" + user + " promoted to admin";
                        chatText.append(result + "\n");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    result += "\nPromotion of " + user + " failed (" +
                            error.toString() + ")";
                    chatText.append(result + "\n");
                }
            });
            mRequestQueue.add(stringRequest);
        }
        else if (msg.contains("/demote ")) {
            String user = msg.substring(8);
            String URL = "http://coms-309-051.class.las.iastate.edu:8080/users/" +
                    "admin/changetype/" + user + "/1";

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    result += "\n" + user + ": " + response;
                    chatText.append(result + "\n");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    result += "\nDemotion of " + user + " failed (" +
                            error.toString() + ")";
                    chatText.append(result + "\n");
                }
            });
            mRequestQueue.add(stringRequest);
        }
        else if (msg.contains("/postscore ")) {
            if (msg.split(" ").length != 3) {
                result += "\n" + "Usage: /postscore {id} {score}";
                chatText.append(result + "\n");
            } else {
                String URL = "http://coms-309-051.class.las.iastate.edu:8080/Scores/";
                String score = msg.split(" ")[2].trim() + "/" +
                        msg.split(" ")[1];
                System.out.println(score);
                System.out.println(URL.concat(score)
                        .concat(msg.split(" ")[1]));

                mRequestQueue = Volley.newRequestQueue(this);

                StringRequest mStringRequest;
                mStringRequest = new StringRequest(Request.Method.POST, URL.concat(score), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        result += "\n" + response;
                        chatText.append(result + "\n");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error: " + error.toString());
                        result += "\nError: " + error;
                        chatText.append(result + "\n");
                    }
                });

                mRequestQueue.add(mStringRequest);
            }
        }

        else {
            result += "Unrecognized command \"" + msg + "\". Type /help for a list of commands";
            chatText.append(result + "\n");
        }

    }

    private String getAllCommands() {
        String result = "";

        result += "/getid {username} \n" +
                "/ban {username} \n" +
                "/op {username} \n" +
                "/demote {username} \n" +
                "/postscore {id} {score}";

        return result;
    }
}