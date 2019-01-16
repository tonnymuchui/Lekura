package com.lekura.lekura.Chat;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lekura.lekura.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdaptor extends RecyclerView.Adapter<MessageAdaptor.MessageViewHolder> {
    private List<Messages> userMessageList;
    private FirebaseAuth mauth;
    private DatabaseReference usersref;
    public  MessageAdaptor (List<Messages> userMessageList){
        this.userMessageList = userMessageList;
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText = (TextView) itemView.findViewById(R.id.messender_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.mesreceiver_text);
            receiverProfileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);

        }
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.messages_layout,viewGroup,false);
        mauth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, int i) {
        String messagesenderId = mauth.getCurrentUser().getUid();
        final Messages message = userMessageList.get(i);
        String fromUserId = message.getFrom();
        String fromMessageType = message.getType();
        usersref = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);
        usersref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")){
                    String receiverImage = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(receiverImage).placeholder(R.drawable.user).into(messageViewHolder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (fromMessageType.equals("text")){
            messageViewHolder.receiverMessageText.setVisibility(View.INVISIBLE);
            messageViewHolder.receiverProfileImage.setVisibility(View.INVISIBLE);

            if (fromUserId.equals(messagesenderId)){
                messageViewHolder.senderMessageText.setBackgroundResource(R.drawable.sender_messages);
                messageViewHolder.senderMessageText.setTextColor(Color.BLACK);
                messageViewHolder.senderMessageText.setText(message.getMessage());
            }
            else {
                messageViewHolder.receiverMessageText.setVisibility(View.VISIBLE);
                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);

                messageViewHolder.receiverMessageText.setBackgroundResource(R.drawable.receiver_layout);
                messageViewHolder.receiverMessageText.setTextColor(Color.BLACK);
                messageViewHolder.receiverMessageText.setText(message.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }


}
