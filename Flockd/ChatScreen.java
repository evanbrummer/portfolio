package com.example.flockd_frontend;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.util.TimeZone;

public class ChatScreen extends AppCompatActivity {

    private int other_userID;
    private String other_username;
    private String timezone;
    WebSocketClient mWebSocketClient;
    RequestQueue mRequestQueue;

    int uID;
    String username;

    EditText mInput;
    TextView chatText;
    Button btn_send;
    TextView header;
    RelativeLayout chatBox;

    boolean logWasPrinted;

    public void openMatchListPage(View view) {
        mWebSocketClient.close();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        mRequestQueue = Volley.newRequestQueue(this);

        uID = Integer.parseInt(((userInformation)this.getApplication()).getUserID());
        username = ((userInformation) this.getApplication()).getUserName();
        timezone = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
        System.out.println("Timezone: " + timezone);
        logWasPrinted = false;

        other_userID = getIntent().getIntExtra("userID", -1);
        other_username = getIntent().getStringExtra("username");

        mInput = findViewById(R.id.edit_gchat_message);
        chatText = findViewById(R.id.chatText);
        chatText.setMovementMethod(new ScrollingMovementMethod());
        chatText.invalidate();
        chatText.requestLayout();
        btn_send = findViewById(R.id.button_gchat_send);
        btn_send.setEnabled(false);

        header = findViewById(R.id.otherUserHeader);
        chatBox = findViewById(R.id.layout_gchat_chatbox);

        updateHeader(header);

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

        connectWebSocket();
    }

    private void updateHeader(TextView header) {
        header.setText(other_username);
        header.invalidate();
        header.requestLayout();
    }

    private void sendEnteredMessage() {
        String message = mInput.getText().toString();


        if (message != null && message.length() > 0) {
            if (message.charAt(0) == '/') {
                if (message.equals("/unmatch")) {
                    chatText.append("\n\n" + "Un-matching with " + other_username + "...");

                    String unmatchURL = "http://coms-309-051.class.las.iastate.edu:8080/users/"
                            + uID + "/matches/"
                            + other_userID + "/unmatch";

                    StringRequest request;
                    request = new StringRequest(Request.Method.PUT, unmatchURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("Unmatch response: " + response);
                            //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                            finish();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                    mRequestQueue.add(request);


                    finish();

                }


            } else {
                mWebSocketClient.send(message);
            }
            mInput.getText().clear();
            chatText.invalidate();
            chatText.requestLayout();

        } else {
            mWebSocketClient.close();
        }
    }

    private void connectWebSocket() {
        URI uri;
        try {
//            uri = new URI(
//                    "wss://demo.piesocket.com/v3/channel_123" +
//                            "?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV" +
//                            "&notify_self");

            uri = new URI(
                    "ws://coms-309-051.class.las.iastate.edu:8080/chat/" +
                           username + "/" +
                           other_username + "/" +
                           timezone);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("WebSocket", "Opened");
                System.out.println("success!");
                btn_send.setEnabled(true);
            }

            @Override
            public void onMessage(String message) {
                Log.i("WebSocket", "Message received");
                String parsedMsg = message;

                if (!logWasPrinted) {
                    logWasPrinted = true;
                } else {
                    parsedMsg = parsedMsg.substring(11);
                    parsedMsg += "\n";
                }
                chatText.append("\n" + parsedMsg);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("WebSocket", "Closed: " + reason);
                chatText.append("\n\n" + "Disconnected.");
                btn_send.setEnabled(false);
            }

            @Override
            public void onError(Exception ex) {
                Log.i("WebSocket", "Error: " + ex.getMessage());
            }
        };

        mWebSocketClient.connect();
        System.out.println("Connecting... ");
    }

}