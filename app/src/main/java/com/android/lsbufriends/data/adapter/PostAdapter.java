package com.android.lsbufriends.data.adapter;

import static com.android.lsbufriends.utils.constants.NameConstants.COMMENTS_REF;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lsbufriends.R;
import com.android.lsbufriends.data.model.post.PostRequest;
import com.android.lsbufriends.data.model.user.User;
import com.android.lsbufriends.databinding.ItemPostBinding;
import com.android.lsbufriends.ui.chat.ChatActivity;
import com.android.lsbufriends.ui.comment.CommentActivity;
import com.android.lsbufriends.ui.post.filter.FilterPosts;
import com.android.lsbufriends.utils.constants.NameConstants;
import com.android.lsbufriends.utils.constants.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements Filterable {

    Context mContext;
    public List<PostRequest> postRequestList, filterList;
    String name,publisherId;

    FirebaseUser firebaseUser;
    private FilterPosts filter;

    public PostAdapter(Context mContext, List<PostRequest> postRequestList) {
        this.mContext = mContext;
        this.postRequestList = postRequestList;
        this.filterList = postRequestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPostBinding postBinding = ItemPostBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(postBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        PostRequest post = postRequestList.get(position);

        String date = Util.getDate(Long.parseLong(post.getTimestamp()));

        holder.binding.date.setText(date);
        holder.binding.messageTv.setText(post.getMessage());
        publisherInfo(holder.binding, post.getPublisher());


        // likes
        isLiked(post.getPostID(), holder.binding);
        holder.binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.binding.like.getTag().equals("Like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostID())
                            .child(firebaseUser.getUid())
                            .setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostID())
                            .child(firebaseUser.getUid())
                            .removeValue();
                }
            }
        });

        // comments
        getCommentsCount(holder.binding, post.getPostID());
        holder.binding.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.start(mContext, post.getPostID(), post.getPublisher());
            }
        });

        // share
        holder.binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Please Wait....", Toast.LENGTH_SHORT).show();
                shareItem(post.getMessage());
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // to open chat
        holder.itemView.setOnClickListener(v -> {
            if (!uid.equals(post.getPublisher())) {
                ChatActivity.start(mContext,String.valueOf(holder.binding.username.getText()),post.getPublisher());
            }
        });
        holder.binding.username.setOnClickListener(v -> {

            if (!uid.equals(post.getPublisher())) {
                ChatActivity.start(mContext,String.valueOf(holder.binding.username.getText()),post.getPublisher());
            };
        });
        holder.binding.profileImage.setOnClickListener(v -> {
            if (!uid.equals(post.getPublisher())) {
                ChatActivity.start(mContext,String.valueOf(holder.binding.username.getText()),post.getPublisher());
            }
        });
    }

    @Override
    public int getItemCount() {
        return postRequestList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            //init filter
            filter = new FilterPosts(this, filterList);
        }
        return filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemPostBinding binding;

        public ViewHolder(@NonNull ItemPostBinding itemPostBinding) {
            super(itemPostBinding.getRoot());
            this.binding = itemPostBinding;
        }
    }

    private void publisherInfo(ItemPostBinding binding, final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference()
                .child(NameConstants.USER_REF);
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userList = snapshot.getValue(User.class);
                assert userList != null;
                name = userList.getFirstName() + " " + userList.getLastName();
                binding.username.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLiked(String postId, final ItemPostBinding binding) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String count = "" + snapshot.getChildrenCount();
                binding.likesCount.setText(count);

                if (snapshot.child(user.getUid()).exists()) {
                    binding.like.setImageResource(R.drawable.ic_liked);
                    binding.like.setTag("Likes");
                } else {
                    binding.like.setImageResource(R.drawable.ic_like);
                    binding.like.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCommentsCount(final ItemPostBinding binding, final String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(COMMENTS_REF)
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String counts = snapshot.getChildrenCount() + "";
                binding.commentsCount.setText(counts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void shareItem(String message) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(Intent.createChooser(intent, "Share via"));
    }

}
