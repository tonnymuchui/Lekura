package com.lekura.lekura;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lekura.lekura.Auth.LoginActivity;
import com.lekura.lekura.Chat.ChatActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.Update_button) Button Update_button;
    @BindView(R.id.set_profile_image) CircleImageView set_profile_image;
    @BindView(R.id.set_status_name) EditText set_status_name;
    @BindView(R.id.set_user_name) EditText set_user_name;
    private String currentUser;
    private FirebaseAuth sauth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        sauth = FirebaseAuth.getInstance();
        currentUser = sauth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        Update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSetting();
            }
        });
//        RetriveUserData();
    }

    private void updateSetting() {
        String setUserName = set_user_name.getText().toString();
        final String setStatus = set_status_name.getText().toString();

        if (TextUtils.isEmpty(setUserName)){
            Toast.makeText(this,"Enter Your Name",Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String,String> profileMap= new HashMap<>();
            profileMap.put("uid",currentUser);
            profileMap.put("name",setUserName);
            profileMap.put("Status",setStatus);
            reference.child("Users").child(currentUser).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                sendUserToChatActivity();
                                Toast.makeText(SettingActivity.this,"Profile Complete",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(SettingActivity.this,"Error" + message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private void sendUserToChatActivity(){
        Intent mainIntent = new Intent(SettingActivity.this,ChatActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
//    private void RetriveUserData() {
//
//        reference.child("Users").child(currentUser)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))){
//
//                            String retriveUserName = dataSnapshot.child("name").getValue().toString();
//                            String retriveStatus = dataSnapshot.child("status").getValue().toString();
//                            String retriveProfilePhoto = dataSnapshot.child("image").getValue().toString();
//
//                            set_user_name.setText(retriveUserName);
//                            set_status_name.setText(retriveStatus);
//
//                        }
//                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){
//
//                            String retriveUserName = dataSnapshot.child("name").getValue().toString();
//                            String retriveStatus = dataSnapshot.child("status").getValue().toString();
//
//                            set_user_name.setText(retriveUserName);
//                            set_status_name.setText(retriveStatus);
//
//                        } else {
//                            Toast.makeText(SettingActivity.this,"Update profile",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//    }
}
