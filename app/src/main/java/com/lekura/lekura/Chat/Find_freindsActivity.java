package com.lekura.lekura.Chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lekura.lekura.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Find_freindsActivity extends AppCompatActivity {
    @BindView(R.id.mrecyclerView) RecyclerView mrecyclerView;
    private DatabaseReference userRef;
    private final List<Contacts> Contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_freinds);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Find Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = new
                FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(userRef, Contacts.class)
                .build();

        final FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder> adapter = new
                FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FindFriendsViewHolder holder, final int position, @NonNull final Contacts model) {

                        holder.userName.setText(model.getName());
                        holder.userStatus.setText(model.getStatus());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.user).into(holder.profileImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String visit_user = getRef(position).getKey();
                                String receiverName = Contacts.get(position).getName().toString();
                                String receiverImage = Contacts.get(position).getImage().toString();

                                startActivity(new Intent(Find_freindsActivity.this,MessageChatActivity.class)
                                        .putExtra("visit_user", visit_user)
                                        .putExtra("receiverName",receiverName)
                                        .putExtra("receiverImage",receiverImage)
                                );
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.find_friends, viewGroup, false);
                       FindFriendsViewHolder findFriendsViewHolder = new FindFriendsViewHolder(view);
                       return findFriendsViewHolder;
                    }
                };
        mrecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder{
        TextView userName, userStatus;
        CircleImageView profileImage;
        public FindFriendsViewHolder(@NonNull View itemView) {

            super(itemView);
            userName = itemView.findViewById(R.id.muser_profile_name);
            userStatus = itemView.findViewById(R.id.muser_profile_status);
            profileImage = itemView.findViewById(R.id.mUser_profile_image);

        }
    }

}
