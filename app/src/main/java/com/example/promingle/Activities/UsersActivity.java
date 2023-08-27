package com.example.promingle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.promingle.Adapter.UserAdapter;
import com.example.promingle.Model.UserModel;
import com.example.promingle.databinding.ActivityUsersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {
    private ActivityUsersBinding binding;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<UserModel> userModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        userModelArrayList = new ArrayList<>();

        DatabaseReference reference = database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading(true);
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    userModelArrayList.add(userModel);
                }
                loading(false);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        binding.UserChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(UsersActivity.this,userModelArrayList);
        binding.UserChatRecyclerView.setAdapter(adapter);

        binding.imageback.setOnClickListener(v -> onBackPressed());
    }
    private void loading(Boolean isLoading){
        if (isLoading){
            binding.ChatProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.ChatProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}