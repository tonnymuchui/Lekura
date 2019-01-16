package com.lekura.lekura.Chat;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lekura.lekura.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.Util;

public class MessageChatActivity extends AppCompatActivity {

//    @BindView(R.id.custom_profile_image) CircleImageView mcircleImageView;
//    @BindView(R.id.custom_profile_name) TextView mCustomName;
//    @BindView(R.id.custom_lastseen) TextView mLastSeen;
    @BindView(R.id.private_btn_send) ImageButton private_btn_send;
    @BindView(R.id.private_input_message) EditText private_input_message;
    @BindView(R.id.recyclerView_send_private_message) RecyclerView recyclerView_send_private_message;
    private String MessageReceiverName,MessageReceiverId,MessageReceiverImage, messageSenderId;
    private FirebaseAuth mauth;
    private DatabaseReference RootRef;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdaptor messageAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);
        ButterKnife.bind(this);

        mauth = FirebaseAuth.getInstance();
        messageSenderId = mauth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        MessageReceiverId = getIntent().getExtras().get("visit_user").toString();
        MessageReceiverName = getIntent().getExtras().get("receiverName").toString();
        MessageReceiverImage = getIntent().getExtras().get("receiverImage").toString();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_send_private_message.setLayoutManager(linearLayoutManager);
        recyclerView_send_private_message.setAdapter(messageAdaptor);

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

    @Override
    protected void onStart() {
        super.onStart();
        RootRef.child("Messages").child(messageSenderId).child(MessageReceiverId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Messages messages = dataSnapshot.getValue(Messages.class);
                        messagesList.add(messages);
                        messageAdaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
