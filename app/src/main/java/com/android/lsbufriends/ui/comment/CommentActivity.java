package com.android.lsbufriends.ui.comment;

import static com.android.lsbufriends.utils.constants.NameConstants.COMMENTS_REF;
import static com.android.lsbufriends.utils.constants.NameConstants.USER_REF;
import static com.android.lsbufriends.utils.constants.Util.getDate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.lsbufriends.R;
import com.android.lsbufriends.data.adapter.CommentAdapter;
import com.android.lsbufriends.data.model.comment.CommentRequest;
import com.android.lsbufriends.databinding.ActivityCommentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    public static void start(Context context, String postId, String publisher) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("postId",postId);
        intent.putExtra("publisher",publisher);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//        context.overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
    }

    private ActivityCommentBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;

    String postId, publisher;

    List<CommentRequest> commentList;
    CommentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postId = getIntent().getStringExtra("postId");
        publisher = getIntent().getStringExtra("publisher");

        init();
        setListeners();
    }
    private void getComment() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(COMMENTS_REF).child(postId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    commentList.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        CommentRequest commentRequest = dataSnapshot.getValue(CommentRequest.class);
                        commentList.add(commentRequest);
                    }
                    adapter.notifyDataSetChanged();
                    binding.no.setVisibility(View.GONE);
                } else {
                    binding.no.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommentActivity.this, "Error while load comments", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child(USER_REF);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentList = new ArrayList<>();

        commentList=new ArrayList<>();
        adapter=new CommentAdapter(this,commentList);

        getComment();

        binding.recyclerView.setAdapter(adapter);

    }

    private void setListeners() {
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = String.valueOf(binding.commentEdit.getText());
                if(comment.isEmpty())
                {
                    binding.commentEdit.setError(getResources().getString(R.string.require_field));
                    Toast.makeText(CommentActivity.this, "Empty Comment can't be send", Toast.LENGTH_SHORT).show();
                } else {
                    binding.commentEdit.setError(null);
                    sendComment(comment);
                }
            }
        });
    }

    private void sendComment(String comment) {
        String timestamp = "" + System.currentTimeMillis();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(COMMENTS_REF).child(postId);
        String date = getDate(Long.parseLong(timestamp));
        CommentRequest commentRequest = new CommentRequest(comment,FirebaseAuth.getInstance().getCurrentUser().getUid(),date);

        databaseReference.push().setValue(commentRequest)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    addNotifications();
                                    binding.commentEdit.setText("");
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CommentActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
     }

    private void addNotifications()
    {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Notifications").child(publisher);

        HashMap<String,Object> map=new HashMap<>();

        map.put("userid",user.getUid());
        map.put("comment","Commented: "+binding.commentEdit.getText().toString());
        map.put("postid",postId);
        map.put("ispost",true);

        reference.push().setValue(map);
    }
}