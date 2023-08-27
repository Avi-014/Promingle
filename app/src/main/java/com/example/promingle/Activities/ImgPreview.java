package com.example.promingle.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.promingle.Model.MessageModel;
import com.example.promingle.databinding.ActivityImgPreviewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.Objects;

public class ImgPreview extends AppCompatActivity {
    private ActivityImgPreviewBinding binding;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImgPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();


        Intent intent = getIntent();
        String SenderRoom = intent.getStringExtra("senderRoom");
        String ReceiverRoom = intent.getStringExtra("receiverRoom");
        if (intent.hasExtra("SelectedImage")) {
             imgUri = intent.getParcelableExtra("SelectedImage");
             binding.choseImg.setImageURI(imgUri);
        }
        binding.imgSendBtn.setOnClickListener(view -> {
            String secs = "" + System.currentTimeMillis() / 1000;
            StorageReference storageReference = storage.getReference().child(secs);

            storageReference.putFile(imgUri).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()){
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Date date = new Date();
                        String SenderId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                        MessageModel msgModel = new MessageModel(null, SenderId,uri.toString(), date.getTime());
                        database.getReference().child("chats")
                                .child(SenderRoom)
                                .child("Messages")
                                .push()
                                .setValue(msgModel).addOnCompleteListener(task -> database.getReference().child("chats")
                                        .child(ReceiverRoom)
                                        .child("Messages")
                                        .push().setValue(msgModel).addOnCompleteListener(task2 -> {
                                            finish();
                                        }));
                    });
                }});
        });
    }
}