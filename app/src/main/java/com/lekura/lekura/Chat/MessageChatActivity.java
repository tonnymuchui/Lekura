package com.lekura.lekura.Chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lekura.lekura.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.Util;

public class MessageChatActivity extends AppCompatActivity {

    @BindView(R.id.custom_profile_image) CircleImageView mcircleImageView;
    @BindView(R.id.custom_profile_name) TextView mCustomName;
    @BindView(R.id.custom_lastseen) TextView mLastSeen;
    @BindView(R.id.private_btn_send) ImageButton private_btn_send;
    @BindView(R.id.private_input_message) EditText private_input_message;
    private String MessageReceiverName,MessageReceiverId,MessageReceiverImage, messageSenderId;
    private FirebaseAuth mauth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

        mauth = FirebaseAuth.getInstance();
        messageSenderId = mauth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        MessageReceiverId = getIntent().getExtras().get("").toString();
        MessageReceiverName = getIntent().getExtras().get("visit_user").toString();
        MessageReceiverImage = getIntent().getExtras().get("").toString();
    private_btn_send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendMessage();
        }
    });

    }

    private void sendMessage() {
        String messageText = private_input_message.getText().toString();
        if (!TextUtils.isEmpty(messageText)){
            String messageSenderREf = "Messages/" + messageSenderId + "/" + MessageReceiverId;
            String messageReceiverREf = "Messages/" + MessageReceiverId + "/" + messageSenderId;

            DatabaseReference userMessageRef = RootRef.child("Messages")
                    .child(messageSenderId).child(MessageReceiverId).push();

            String messagePushId = userMessageRef.getKey();
            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderId);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderREf + "/" + messagePushId, messageTextBody);
            messageBodyDetails.put(messageReceiverREf + "/" + messagePushId, messageTextBody);
            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(MessageChatActivity.this,"Message Not sent",Toast.LENGTH_SHORT).show();
                    }
                    private_input_message.setText("");
                }
            });
        }
    }
}
