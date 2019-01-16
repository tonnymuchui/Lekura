package com.lekura.lekura.Chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lekura.lekura.R;

public class MessageChatActivity extends AppCompatActivity {

    private String receive_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

    receive_user = getIntent().getExtras().get("visit_user").toString();

    }
}
