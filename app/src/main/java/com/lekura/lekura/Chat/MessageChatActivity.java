package com.lekura.lekura.Chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lekura.lekura.R;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageChatActivity extends AppCompatActivity {

    @BindView(R.id.custom_profile_image) CircleImageView mcircleImageView;
    @BindView(R.id.custom_profile_name) TextView mCustomName;
    @BindView(R.id.custom_lastseen) TextView mLastSeen;
    private String receive_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

    receive_user = getIntent().getExtras().get("visit_user").toString();

    }
}
