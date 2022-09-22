package com.mjcdouai.go4lunch.ui;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mjcdouai.go4lunch.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    TextView userName;
    TextView messageTime;
    TextView messageText;



    public ChatViewHolder(View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.message_user);
        messageTime = itemView.findViewById(R.id.message_time);
        messageText = itemView.findViewById(R.id.message_text);
    }

    public void setUserName(String usr) {
        userName.setText(usr);
    }

    public void setMessageTime(long time) {
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                time));
    }

    public void setMessageText(String message) {
        messageText.setText(message);
    }


}

