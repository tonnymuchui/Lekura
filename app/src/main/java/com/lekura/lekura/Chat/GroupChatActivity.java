package com.lekura.lekura.Chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lekura.lekura.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SimpleTimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupChatActivity extends AppCompatActivity {
    @BindView(R.id.my_scroll_view) ScrollView my_scroll_view;
    @BindView(R.id.myLinearLayout) LinearLayout myLinearLayout;
    @BindView(R.id.input_group_text) EditText input_group_text;
    @BindView(R.id.group_chat_text) TextView group_chat_text;
    @BindView(R.id.send_message_button) ImageButton send_message_button;
    private FirebaseAuth Gauth;
    private DatabaseReference userRef,GroupNameRef,GroupMessageKeyRef;
    private String currentGroupName,currentUserId,currentUserName,currentDate,currentTime, receiverUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        ButterKnife.bind(this);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(GroupChatActivity.this,currentGroupName,Toast.LENGTH_SHORT).show();
        Gauth = FirebaseAuth.getInstance();
        currentUserId = Gauth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);

        GetUserInfo();
        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageToFirebase();
                input_group_text.setText("");
                my_scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    DisplayMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    DisplayMessage(dataSnapshot);
                }
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

    private void GetUserInfo() {

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void saveMessageToFirebase() {

        String message = input_group_text.getText().toString();
        String messageKey = GroupNameRef.push().getKey();
        if (!TextUtils.isEmpty(message)){

            Calendar calForeDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyy");
            currentDate = currentDateFormat.format(calForeDate.getTime());

            Calendar calForeTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calForeTime.getTime());

            HashMap<String, Object> groupmessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupmessageKey);
            GroupMessageKeyRef = GroupNameRef.child(messageKey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", currentUserName);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);
            GroupMessageKeyRef.updateChildren(messageInfoMap);
        }
    }

    private void DisplayMessage(DataSnapshot dataSnapshot) {

        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()){
            String chatdate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatmessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName= (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            group_chat_text.append(chatName + " \n" + chatmessage + "\n" + chatTime + "  " + chatdate + "\n\n\n");

            my_scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

}
