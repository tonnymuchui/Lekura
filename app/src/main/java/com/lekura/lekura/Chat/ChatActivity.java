package com.lekura.lekura.Chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lekura.lekura.Auth.LoginActivity;
import com.lekura.lekura.Auth.SignupActivity;
import com.lekura.lekura.R;
import com.lekura.lekura.SettingActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.listView) ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_group = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference rootReference;
    private DatabaseReference GroupRef;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list_group){
            @Override
            public View getView(int position, View view, ViewGroup viewGroup){
                TextView item = (TextView) super.getView(position,view,viewGroup);
                item.setTypeface(mTypeface);
                item.setTextColor(Color.parseColor("#000000"));
                item.setTypeface(item.getTypeface(),Typeface.BOLD);
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                return item;
            }
        };
        listView.setAdapter(arrayAdapter);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        RetriveDataBase();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentGroupName = parent.getItemAtPosition(position).toString();
                Intent group_Intent = new Intent(getApplicationContext(),GroupChatActivity.class);
                group_Intent.putExtra("groupName", currentGroupName);
                startActivity(group_Intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.options_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.main_find_Logout_option){
            auth.signOut();
            startActivity(new Intent(ChatActivity.this,LoginActivity.class));

        }
        if (item.getItemId() == R.id.main_find_friends_option){

            startActivity(new Intent(ChatActivity.this,Find_freindsActivity.class));
        }
        if (item.getItemId() == R.id.main_find_Settings_option){
            sendUserToSettingActivity();

        }
        if (item.getItemId() == R.id.main_find_group_option){
            RequestNewGroup();
        }
        return true;
    }

    private void RequestNewGroup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this,R.style.AlertDialog);
        builder.setTitle("Group Name");
        final EditText groupNameField = new EditText(ChatActivity.this);
        groupNameField.setHint("fifa");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String groupName = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName)){
                    Toast.makeText(ChatActivity.this,"Enter group Name",Toast.LENGTH_SHORT).show();
                }
                else {

                    CreateNewGroup(groupName);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void CreateNewGroup(final String groupName) {
        rootReference.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChatActivity.this,groupName + "Created Succesfully",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user == null){
            Intent signUp= new Intent(ChatActivity.this,SignupActivity.class);
            signUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signUp);
            finish();
        }
        else {
            veryfyUser();
        }
    }
    private void veryfyUser() {
        String currentUserId = auth.getCurrentUser().getUid();
        rootReference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("name").exists())){
                    Toast.makeText(ChatActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                }
                else {

                    sendUserToSettingActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendUserToSettingActivity(){
        Intent settingIntent = new Intent(ChatActivity.this,SettingActivity.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingIntent);
        finish();
    }
    private void RetriveDataBase() {
        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                list_group.clear();
                list_group.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
