package com.android.lsbufriends.data.adapter;

import static com.android.lsbufriends.utils.constants.NameConstants.USER_REF;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lsbufriends.data.model.comment.CommentRequest;
import com.android.lsbufriends.data.model.user.User;
import com.android.lsbufriends.databinding.ItemCommentBinding;
import com.android.lsbufriends.ui.chat.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context mContext;
    List<CommentRequest> commentRequestList;
    String userName;

    public CommentAdapter(Context mContext, List<CommentRequest> commentRequestList) {
        this.mContext = mContext;
        this.commentRequestList = commentRequestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCommentBinding commentBinding = ItemCommentBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(commentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentRequest commentRequest = commentRequestList.get(position);
        holder.binding.comment.setText(commentRequest.getComment());
        holder.binding.date.setText(commentRequest.getDate());

        getUserInfo(holder.binding, commentRequest.getPublisher());


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // open chat
        holder.binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uid.equals(commentRequest.getPublisher())) {
                    ChatActivity.start(mContext, userName, commentRequest.getPublisher());
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (!uid.equals(commentRequest.getPublisher())) {
                ChatActivity.start(mContext, userName, commentRequest.getPublisher());
            }
        });

        holder.binding.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uid.equals(commentRequest.getPublisher())) {
                    ChatActivity.start(mContext, userName, commentRequest.getPublisher());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemCommentBinding binding;

        public ViewHolder(@NonNull ItemCommentBinding itemPostBinding) {
            super(itemPostBinding.getRoot());
            this.binding = itemPostBinding;
        }
    }

    private void getUserInfo(ItemCommentBinding binding, String publisher) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER_REF).child(publisher);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                userName = user.getFirstName() + " " + user.getLastName();
                binding.username.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
