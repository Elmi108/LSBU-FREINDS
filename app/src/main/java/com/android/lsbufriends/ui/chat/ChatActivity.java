package com.android.lsbufriends.ui.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.lsbufriends.data.adapter.ChatAdapter;
import com.android.lsbufriends.data.model.message.Message;
import com.android.lsbufriends.databinding.ActivityChatBinding;
import com.android.lsbufriends.ui.base.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends BaseActivity {

    public static void start(Context context, String publisherName,String publisherID)
    {
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtra("name",publisherName);
        intent.putExtra("uid",publisherID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private ActivityChatBinding binding;
    private ChatAdapter adapter;
    ArrayList<Message> messages;
    String senderRoom,receiverRoom,senderUid,receiverUid;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);
        messages = new ArrayList<>();
        String name = getIntent().getStringExtra("name");
        binding.name.setText(name);
        receiverUid = getIntent().getStringExtra("uid");
        senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom =  receiverUid + senderUid;
        adapter = new ChatAdapter(this,messages,senderRoom,receiverRoom);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        database.getReference().child("chats")
                .child(senderRoom)
                .child("message")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messages.add(message);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("LSBU FRND", "onCancelled: " + error.getMessage());
                    }
                });
        binding.sendIv.setOnClickListener(v -> {
            String messageTxt = String.valueOf(binding.messageBoxEt.getText());
            Date date = new Date();
            Message message = new Message(messageTxt,senderUid,date.getTime());
            binding.messageBoxEt.setText("");
            String randomKey = database.getReference().push().getKey();
            HashMap<String,Object> lastMsgObj = new HashMap<>();
            lastMsgObj.put("lastMsg",message.getMessage());
            lastMsgObj.put("lastMsgTime",date.getTime());

            database.getReference().child("chats").child(senderRoom)
                    .updateChildren(lastMsgObj);
            database.getReference().child("chats").child(receiverRoom)
                    .updateChildren(lastMsgObj);
            database.getReference().child("chats").child(senderRoom)
                    .child("message")
                    .child(randomKey)
                    .setValue(message)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            database.getReference().child("chats")
                                    .child(receiverRoom)
                                    .child("message")
                                    .child(randomKey)
                                    .setValue(message)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                        }
                    });
        });
        binding.attachmentIv.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,25);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 25)
        {
            if(data!=null)
            {
                String selectedImage = data.getAction();
                Calendar calendar = Calendar.getInstance();
                StorageReference reference = storage.getReference().child("chats")
                        .child(calendar.getTimeInMillis() + "");
                dialog.show();
                reference.putFile(Uri.parse(selectedImage))
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                dialog.dismiss();
                                if(task.isSuccessful())
                                {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String filePath = String.valueOf(uri);
                                            String messageTxt = String.valueOf(binding.messageBoxEt.getText());
                                            Date date = new Date();
                                            Message message = new Message(messageTxt,senderUid,date.getTime());
                                            message.setMessage("photo");
                                            message.setImageUrl(filePath);
                                            binding.messageBoxEt.setText("");
                                            String randomKey = database.getReference().push().getKey();
                                            HashMap<String,Object> lastMsgObj = new HashMap<>();
                                            lastMsgObj.put("lastMsg",message.getMessage());
                                            lastMsgObj.put("lastMsgTime",date.getTime());
                                            database.getReference().child("chats")
                                                    .updateChildren(lastMsgObj);
                                            database.getReference().child("chats")
                                                    .child(receiverRoom)
                                                    .updateChildren(lastMsgObj);
                                            database.getReference().child("chats")
                                                    .child(senderRoom)
                                                    .child("message")
                                                    .child(randomKey)
                                                    .setValue(message)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            database.getReference().child("chats")
                                                                    .child(receiverRoom)
                                                                    .child("message")
                                                                    .child(randomKey)
                                                                    .setValue(message)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {

                                                                        }
                                                                    });
                                                        }
                                                    });

                                        }
                                    });
                                }
                            }
                        });
            }
        }
    }
}