package com.example.promingle.Adapter;

import static com.example.promingle.Activities.ChatActivity.ReceiverImage;
import static com.example.promingle.Activities.ChatActivity.SenderImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.promingle.Model.MessageModel;
import com.example.promingle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageModel> messageModelArrayList;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVED = 2;


    public ChatAdapter(Context context, ArrayList<MessageModel> messageModelArrayList) {
        this.context = context;
        this.messageModelArrayList = messageModelArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.received_message, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModelArrayList.get(position);
        if (holder.getClass() == SenderViewHolder.class){

            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.senderMessage.setText(messageModel.getMessage());
            if ( messageModel.getMessage() == null || messageModel.getMessage().isEmpty() ) {
                viewHolder.senderMessage.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.senderMessage.setVisibility(View.VISIBLE);
            }

            if (messageModel.getAttachments() == null) {
                viewHolder.chooseImg.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.chooseImg.setVisibility(View.VISIBLE);
            }
             Picasso.get().load(SenderImage).into(viewHolder.senderImage);
             Picasso.get().load(messageModel.getAttachments()).into(viewHolder.chooseImg);

        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.receiverMessage.setText(messageModel.getMessage());
            if ( messageModel.getMessage() == null || messageModel.getMessage().isEmpty()) {
                viewHolder.receiverMessage.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.receiverMessage.setVisibility(View.VISIBLE);
            }

            if (messageModel.getAttachments() == null) {
                viewHolder.chooseImg.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.chooseImg.setVisibility(View.VISIBLE);
            }
            Picasso.get().load(ReceiverImage).into(viewHolder.recieverImage);
            Picasso.get().load(messageModel.getAttachments()).into(viewHolder.chooseImg);
        }
    }

    @Override
    public int getItemCount() {
        return messageModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = messageModelArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messageModel.getSenderId())){
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVED;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView senderImage;
        TextView senderMessage;

        ImageView chooseImg;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderImage = itemView.findViewById(R.id.senderImage);
            senderMessage = itemView.findViewById(R.id.senderMessage);
            chooseImg = itemView.findViewById(R.id.chooseImg);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView recieverImage;
        ImageView chooseImg;
        TextView receiverMessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverImage = itemView.findViewById(R.id.recieverImage);
            chooseImg = itemView.findViewById(R.id.chooseImg);
            receiverMessage = itemView.findViewById(R.id.receiverMessage);
        }
    }
}
