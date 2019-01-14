package com.lekura.lekura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lekura.lekura.Auth.LoginActivity;
import com.lekura.lekura.Chat.ChatActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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
    private static final int Gallerypick = 1;
   private StorageReference userProfileImageRef;
   private String currentUserID;
   private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        loadingBar = new ProgressDialog(this);
        sauth = FirebaseAuth.getInstance();
        currentUserID = sauth.getCurrentUser().getUid();
        currentUser = sauth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Images");

        Update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSetting();
            }
        });
        RetriveUserData();

        set_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryintent = new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, Gallerypick);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallerypick && resultCode==RESULT_OK && data!=null){
            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                loadingBar.setTitle("Set profile image");
                loadingBar.setMessage("Please wait");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Uri resultUrl = result.getUri();
                StorageReference filePath = userProfileImageRef.child(currentUserID + ".jpg");
                filePath.putFile(resultUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){

                            Toast.makeText(SettingActivity.this,"Profile image Changed",Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getMetadata().getReference().getDownloadUrl().getResult().toString();
                            reference.child("Users").child(currentUserID).child("image")
                                    .setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                           if (!task.isSuccessful()){
                                               String message = task.getException().toString();
                                               loadingBar.dismiss();
                                               Toast.makeText(SettingActivity.this,"Error: "+ message,Toast.LENGTH_SHORT).show();
                                           }else {
                                               loadingBar.dismiss();
                                           }
                                        }
                                    });

                        }else {
                            String message = task.getException().toString();
                            Toast.makeText(SettingActivity.this,"Error: "+ message,Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }

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
    private void RetriveUserData() {

        reference.child("Users").child(currentUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))){

                            String retriveUserName = dataSnapshot.child("name").getValue().toString();
                            String retriveStatus = dataSnapshot.child("Status").getValue().toString();
                            String retriveProfilePhoto = dataSnapshot.child("image").getValue().toString();
                            set_user_name.setText(retriveUserName);
                            set_status_name.setText(retriveStatus);
                            Picasso.get().load(retriveProfilePhoto).into(set_profile_image);


                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){

                            String retriveUserName = dataSnapshot.child("name").getValue().toString();
                            String retriveStatus = dataSnapshot.child("Status").getValue().toString();

                            set_user_name.setText(retriveUserName);
                            set_status_name.setText(retriveStatus);

                        } else {
                            Toast.makeText(SettingActivity.this,"Update profile",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendUserToChatActivity(){
        Intent mainIntent = new Intent(SettingActivity.this,ChatActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}
