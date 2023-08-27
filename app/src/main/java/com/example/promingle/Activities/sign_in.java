package com.example.promingle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.promingle.databinding.ActivitySignInBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class sign_in extends AppCompatActivity {
    private ActivitySignInBinding binding;
    FirebaseAuth auth;
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        setListeners();


        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        }
    }

    //      Forgot Password BUTTON cLICK
    private void setListeners() {
        binding.ForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPassword.class)));

//      SIGNuP BUTTON cLICK
        binding.SignUpButton.setOnClickListener(v ->
                startActivity(new Intent(this, sign_up.class)));

//      LOGIN BUTTON cLICK
        binding.SignInButton.setOnClickListener(v -> {
            if (isValidSignInDetails()) {
                String email = binding.InputEmail.getText().toString();
                String password = binding.InputPassword.getText().toString();
                signIn(email , password);
            }
        });
    }

    private void signIn(String email, String password) {
        loading(true);
        auth.signInWithEmailAndPassword(email.trim(), password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                loading(false);
                Toast.makeText(sign_in.this, "Logged In", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(e -> {
            loading(false);
            Log.i("Error",e.getMessage());
            if (Objects.equals(e.getMessage(), "There is no user record corresponding to this identifier. The user may have been deleted.")){
                Toast.makeText(sign_in.this, "Email is not registered\nCreate Account first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //     Checking Data For Login
    private boolean isValidSignInDetails() {
        if (binding.InputEmail.getText().toString().trim().isEmpty()) {
            binding.InputEmail.setError("");
            Toast.makeText(this, "Email can't be Null", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!binding.InputEmail.getText().toString().matches(EmailPattern)) {
            binding.InputEmail.setError("");
            Toast.makeText(this, "Enter a Valid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.InputPassword.getText().toString().trim().isEmpty()) {
            binding.InputPassword.setError("");
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    private void loading(Boolean isLoading){
        if (isLoading){
            binding.SignInButton.setVisibility(View.INVISIBLE);
            binding.ProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.SignInButton.setVisibility(View.VISIBLE);
            binding.ProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}