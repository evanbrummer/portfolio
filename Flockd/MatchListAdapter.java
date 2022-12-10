package com.example.flockd_frontend;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<MatchListModel> matchListModelArrayList;

    // Constructor
    public MatchListAdapter(Context context, ArrayList<MatchListModel> matchListModelArrayList) {
        this.context = context;
        this.matchListModelArrayList = matchListModelArrayList;
    }

    @NonNull
    @Override
    public MatchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchListAdapter.ViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        MatchListModel model = matchListModelArrayList.get(position);
        //holder.matchIV.setImageBitmap(model.getCard_image());
        holder.usernameTV.setText(model.getCard_username());

        holder.chatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChatScreen.class);
                Bundle bundle = new Bundle();

                i.putExtra("userID", model.getCard_userID());
                i.putExtra("username", model.getCard_username());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return matchListModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView matchIV;
        private final TextView usernameTV;
        private final Button chatBTN;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            matchIV = itemView.findViewById(R.id.img_pfp);
            usernameTV = itemView.findViewById(R.id.match_user);
            chatBTN = itemView.findViewById(R.id.btn_chat);
        }
    }

}
