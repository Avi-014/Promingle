package com.example.promingle.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.promingle.Adapter.ChatAdapter;
import com.example.promingle.Model.MessageModel;
import com.example.promingle.R;
import com.example.promingle.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    String ReceiverName, ReceiverUid;
    public static String SenderImage, ReceiverImage;
    String SenderId, SenderRoom, ReceiverRoom, chatImage, message;
    Uri imgUri;
    FirebaseDatabase database;
    FirebaseAuth auth;
    private FirebaseStorage storage;

    private ActivityChatBinding binding;
    ArrayList<MessageModel> list;

    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();

        ReceiverName = getIntent().getStringExtra("ReceiverName");
        ReceiverImage = getIntent().getStringExtra("ReceiverImage");
        ReceiverUid = getIntent().getStringExtra("ReceiverUid");
        SenderId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        binding.textUsername.setText(ReceiverName);
        Picasso.get().load(ReceiverImage).into(binding.ImageChatProfile);

        SenderRoom = SenderId+ReceiverUid;
        ReceiverRoom = ReceiverUid+SenderId;

        DatabaseReference reference = database.getReference().child("user").child(SenderId);
        DatabaseReference ChatReference = database.getReference().child("chats").child(SenderRoom).child("Messages");


        adapter = new ChatAdapter(ChatActivity.this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatRecyclerView.setLayoutManager(linearLayoutManager);
        binding.chatRecyclerView.setAdapter(adapter);
        ChatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading(true);
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MessageModel msgModel = dataSnapshot.getValue(MessageModel.class);
                    list.add(msgModel);
                }
                loading(false);
                adapter.notifyDataSetChanged();
                binding.chatRecyclerView.smoothScrollToPosition(list.size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SenderImage = Objects.requireNonNull(snapshot.child("imageURI").getValue()).toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        binding.SendMessageButton.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.blow_up);
            v.startAnimation(animation);
            message = binding.inputMessage.getText().toString();
            if (message.isEmpty()){
                Log.i("Messages","Enter Something");
            } else {
                binding.inputMessage.setText("");
                Date date = new Date();

                MessageModel msgModel = new MessageModel(message, SenderId, date.getTime());
                database.getReference().child("chats")
                        .child(SenderRoom)
                        .child("Messages")
                        .push()
                        .setValue(msgModel).addOnCompleteListener(task -> database.getReference().child("chats")
                                .child(ReceiverRoom)
                                .child("Messages")
                                .push().setValue(msgModel).addOnCompleteListener(task1 -> {
                                }));
            }
        });

        binding.galleryIcon.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.blow_up);
            view.startAnimation(animation);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),20);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       try {
           if (requestCode == 20) {
               if (data != null) {
                   imgUri = data.getData();
                   Intent intent = new Intent(this, ImgPreview.class);
                   intent.putExtra("SelectedImage", imgUri);
                   intent.putExtra("senderRoom",SenderRoom );
                   intent.putExtra("receiverRoom",ReceiverRoom );
                   startActivity(intent);
               }
           }
       } catch (Exception e){
           e.printStackTrace();
       }
    }
    private void loading(Boolean isLoading){
        if (isLoading){
            binding.ProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.ProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}