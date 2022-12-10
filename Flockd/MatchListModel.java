package com.example.flockd_frontend;

import android.graphics.Bitmap;

public class MatchListModel {

    private Bitmap card_image;
    private String card_username;
    private int card_userID; // stored for initiating chat

    public MatchListModel(Bitmap card_image, String card_username, int card_userID) {
        this.card_image = card_image;
        this.card_username = card_username;
        this.card_userID = card_userID;
    }

    public Bitmap getCard_image() {
        return card_image;
    }

    public String getCard_username() {
        return card_username;
    }

    public int getCard_userID() {
        return card_userID;
    }

    public void setCard_image(Bitmap card_image) {
        this.card_image = card_image;
    }

    public void setCard_username(String card_username) {
        this.card_username = card_username;
    }

    public void setCard_userID(int card_userID) {
        this.card_userID = card_userID;
    }

}
