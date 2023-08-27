package com.example.promingle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.promingle.Activities.ChatActivity;
import com.example.promingle.Activities.UsersActivity;
import com.example.promingle.Model.UserModel;
import com.example.promingle.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    Context usersActivity;
    ArrayList<UserModel> userModelArrayList;
    public UserAdapter(UsersActivity usersActivity, ArrayList<UserModel> userModelArrayList) {
        this.usersActivity = usersActivity;
        this.userModelArrayList = userModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(usersActivity).inflate(R.layout.user_item_container, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserModel userModel = userModelArrayList.get(position);
        holder.TextChatUserName.setText(userModel.username);
        holder.TextChatUserEmail.setText(userModel.email);
        Picasso.get().load(userModel.imageURI).into(holder.ImageChatProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(usersActivity, ChatActivity.class);
                intent.putExtra("ReceiverName",userModel.getUsername());
                intent.putExtra("ReceiverImage",userModel.getImageURI());
                intent.putExtra("ReceiverUid",userModel.getUid());
                usersActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView ImageChatProfile;
        TextView TextChatUserName, TextChatUserEmail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageChatProfile = itemView.findViewById(R.id.ImageChatProfile);
            TextChatUserName = itemView.findViewById(R.id.TextChatUserName);
            TextChatUserEmail = itemView.findViewById(R.id.TextChatUserEmail);
        }
    }
}
