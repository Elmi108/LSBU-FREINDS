package com.android.lsbufriends.data.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lsbufriends.R;
import com.android.lsbufriends.data.model.message.Message;
import com.android.lsbufriends.databinding.DeleteLayoutBinding;
import com.android.lsbufriends.databinding.ReceiveMsgBinding;
import com.android.lsbufriends.databinding.SendMsgBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Message> messages;
    String senderRoom, receiverRoom;

    public ChatAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
        if (messages != null) {
            this.messages = messages;
        }
    }

    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_SEND) {
            SendMsgBinding sendMsgBinding = SendMsgBinding.inflate(layoutInflater, parent, false);
            return new SentMsgHolder(sendMsgBinding);
        } else {
            ReceiveMsgBinding receiveMsgBinding = ReceiveMsgBinding.inflate(layoutInflater, parent, false);
            return new ReceiverMsgHolder(receiveMsgBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if(holder.getClass() == SentMsgHolder.class)
        {
            SentMsgHolder viewHolder = (SentMsgHolder) holder;
            if(message.getMessage().equals("photo"))
            {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                viewHolder.binding.mLinear.setVisibility(View.GONE);
                Glide
                        .with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholder_iv)
                        .into(viewHolder.binding.image);
            }
            viewHolder.binding.message.setText(message.getMessage());
            viewHolder.itemView.setOnLongClickListener(v -> {

                View view = LayoutInflater.from(context)
                        .inflate(R.layout.delete_layout,null);
                DeleteLayoutBinding binding = DeleteLayoutBinding.bind(view);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Message")
                        .setView(binding.getRoot())
                        .create();
                binding.everyOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        message.setMessage("This message is removed");
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(senderRoom)
                                .child("message")
                                .child(message.getMessageId())
                                .setValue(message);
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(receiverRoom)
                                .child("message")
                                .child(message.getMessageId())
                                .setValue(message);
                        dialog.dismiss();
                    }

                });
                binding.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(senderRoom)
                                .child("message")
                                .child(message.getMessageId())
                                .removeValue();
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(receiverRoom)
                                .child("message")
                                .child(message.getMessageId())
                                .removeValue();
                        dialog.dismiss();
                    }
                });
                binding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
               return false;
            });
        } else {
            ReceiverMsgHolder viewHolder = (ReceiverMsgHolder) holder;
            if(message.getMessage().equals("photo"))
            {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                viewHolder.binding.mLinear.setVisibility(View.GONE);
                Glide
                        .with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholder_iv)
                        .into(viewHolder.binding.image);
            }
            viewHolder.binding.message.setText(message.getMessage());
            viewHolder.itemView.setOnLongClickListener(v -> {

                View view = LayoutInflater.from(context)
                        .inflate(R.layout.delete_layout,null);
                DeleteLayoutBinding binding = DeleteLayoutBinding.bind(view);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Message")
                        .setView(binding.getRoot())
                        .create();
                binding.everyOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        message.setMessage("This message is removed");
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(senderRoom)
                                .child("message")
                                .child(message.getMessageId())
                                .setValue(message);
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(receiverRoom)
                                .child("message")
                                .child(message.getMessageId())
                                .setValue(message);
                        dialog.dismiss();
                    }

                });
                binding.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(senderRoom)
                                .child("message")
                                .child(message.getMessageId())
                                .removeValue();
                        FirebaseDatabase.getInstance().getReference().child("chats")
                                .child(receiverRoom)
                                .child("message")
                                .child(message.getMessageId())
                                .removeValue();
                        dialog.dismiss();
                    }
                });
                binding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message messageModel = messages.get(position);
        String uid = FirebaseAuth.getInstance().getUid();
        assert uid != null;
        if(uid.equals(messageModel.getSenderId()))
        {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }

    public static class SentMsgHolder extends RecyclerView.ViewHolder {
        SendMsgBinding binding;

        public SentMsgHolder(@NonNull SendMsgBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static class ReceiverMsgHolder extends RecyclerView.ViewHolder {

        ReceiveMsgBinding binding;

        public ReceiverMsgHolder(@NonNull ReceiveMsgBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

        }
    }
}
