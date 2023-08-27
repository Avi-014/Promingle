package com.example.promingle.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.promingle.Model.UserModel;
import com.example.promingle.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class sign_up extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private ActivitySignUpBinding binding;
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imgUri;
    String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        setListeners();
    }
    private void setListeners(){
        binding.BackToLoginButton.setOnClickListener(v -> onBackPressed());

        binding.CreateAccSigUpButton.setOnClickListener(v -> {
            if (isValidSignUpDetails()){
                String email = binding.CreateAccNewEmail.getText().toString();
                String password = Objects.requireNonNull(binding.CreateAccNewPassword.getText()).toString();
                String userName = binding.CreateAccUserName.getText().toString();
                signUp(email , password, userName);
            }
        });

//      Profile Image Selection while Creating new account // Intent from layout to gallery
        binding.LayoutImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10){
            if (data!=null){
                imgUri = data.getData();
                binding.LayoutImage.setImageURI(imgUri);
            }
        }
    }

    //checking input details
    private boolean isValidSignUpDetails() {
        if (imgUri == null) {
            showToast("upload an Image");
            return false;
        } else if (binding.CreateAccUserName.getText().toString().trim().isEmpty()) {
            showToast("UserName Can't be Null");
            binding.CreateAccUserName.setError("");
            return false;
        } else if (binding.CreateAccNewEmail.getText().toString().trim().isEmpty()) {
            binding.CreateAccNewEmail.setError("");
            showToast("Email can't be Null");
            return false;
        } else if (!binding.CreateAccNewEmail.getText().toString().matches(EmailPattern)) {
            binding.CreateAccNewEmail.setError("");
            showToast("Enter a Valid Email");
            return false;
        } else if (Objects.requireNonNull(binding.CreateAccNewPassword.getText()).toString().trim().isEmpty()) {
            binding.CreateAccNewPassword.setError("");
            showToast("Enter Password");
            return false;
        } else if (!binding.CreateAccNewPassword.getText().toString().trim()
                .equals(binding.CreateAccConfirmPassword.getText().toString().trim())) {
            binding.CreateAccConfirmPassword.setError("");
            showToast("New Password and Confirm Password must be same");
            return false;
        } else {
            return true;
        }
    }


    // Signup in firebase
    private void signUp(String email, String password, String userName){
        loading(true);
        mAuth.createUserWithEmailAndPassword(email.trim() , password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        DatabaseReference reference = database.getReference().child("user").child(Objects.requireNonNull(mAuth.getUid()));
                        StorageReference storageReference = storage.getReference().child("upload").child(mAuth.getUid());

                        storageReference.putFile(imgUri).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                    imageUri=uri.toString();
                                    UserModel userModel = new UserModel(mAuth.getUid(), userName, email, imageUri);
                                    reference.setValue(userModel).addOnSuccessListener(unused -> {
                                        if (task.isSuccessful()){
                                            loading(false);
                                            Toast.makeText(sign_up.this, "Account Created :)", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(sign_up.this,sign_in.class));
                                            finishAffinity();
                                        }
                                    });
                                });
                            }
                        });
                    }
                }).addOnFailureListener(e -> {
                    loading(false);
                    showToast(e.getMessage());
                    Log.i("Auth Error" , e.getMessage());
                });
    }


    //progressbar
    private void loading(Boolean isLoading){
        if (isLoading){
            binding.CreateAccSigUpButton.setVisibility(View.INVISIBLE);
            binding.CreateAccProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.CreateAccSigUpButton.setVisibility(View.VISIBLE);
            binding.CreateAccProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    // show Toast message
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}