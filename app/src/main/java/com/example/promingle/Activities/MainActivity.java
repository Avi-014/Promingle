package com.example.promingle.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.promingle.Model.UserModel;
import com.example.promingle.R;
import com.example.promingle.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        setListener();


        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            DatabaseReference reference = database.getReference().child("user").child(userId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

                        if (userModel != null) {
                            Picasso.get().load(userModel.getImageURI()).into(binding.ProfilePic);
                            binding.TextUserName.setText(userModel.getUsername());
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        } else {
            Log.i("zsxdcfvgbhjknmgvhbjn","Error");
        }

        if (auth.getCurrentUser()==null){
            startActivity(new Intent(this, sign_in.class));
            finishAffinity();
        }

    }
    private void setListener() {
        binding.ImageSignOut.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blow_up);
            v.startAnimation(animation);
            Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_layout);
            dialog.show();

            Button YesBtn, NoBtn;
            YesBtn = dialog.findViewById(R.id.YesBtn);
            NoBtn = dialog.findViewById(R.id.NoBtn);

            YesBtn.setOnClickListener(v1 -> {
                signOut();
                dialog.dismiss();
            });
            NoBtn.setOnClickListener(v1 -> dialog.dismiss());
        });
        binding.NewChat.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blow_up);
            view.startAnimation(animation);
            startActivity(new Intent(MainActivity.this, UsersActivity.class));
        });
        binding.ProfileHome.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blow_up);
            view.startAnimation(animation);
        });
        binding.settings.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blow_up);
            view.startAnimation(animation);
        });
        binding.ProfilePic.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blow_up);
            view.startAnimation(animation);
        });

        binding.GetStartedBtn.setOnClickListener(view -> startActivity(new Intent(this, UsersActivity.class)));
    }


    private void signOut() {
        Toast.makeText(this,"Signing Out", Toast.LENGTH_SHORT).show();
        auth.signOut();
        startActivity(new Intent(MainActivity.this, sign_in.class));
        finish();
    }
    private void loading(Boolean isLoading){
        if (isLoading){
            binding.ProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.ProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}