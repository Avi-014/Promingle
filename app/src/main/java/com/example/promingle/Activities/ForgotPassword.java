package com.example.promingle.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.promingle.databinding.ActivityForgotPasswordBinding;

public class ForgotPassword extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}