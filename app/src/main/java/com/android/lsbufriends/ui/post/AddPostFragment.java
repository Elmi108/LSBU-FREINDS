package com.android.lsbufriends.ui.post;

import static com.android.lsbufriends.utils.constants.NameConstants.USER_REF;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.android.lsbufriends.R;
import com.android.lsbufriends.data.firebase.Firebase;
import com.android.lsbufriends.data.model.user.User;
import com.android.lsbufriends.databinding.FragmentAddPostBinding;
import com.android.lsbufriends.ui.base.BaseFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AddPostFragment extends BaseFragment {

    public AddPostFragment() {
        // Required empty public constructor
    }

    public static AddPostFragment newInstance() {
        return new AddPostFragment();
    }

    private FragmentAddPostBinding binding;


    TextView userNameTv;
    User userModel;

    @Override
    protected ViewBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentAddPostBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar();
        setUpViews();
        setListener();
        getUserInfo();
    }

    private void setListener() {
        binding.postTextEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String messageStr = String.valueOf(s);
                if(messageStr.isEmpty())
                {
                    binding.post.setEnabled(false);
                    binding.post.setTextColor(getActivity().getResources().getColor(R.color.textColor));
                } else {
                    binding.post.setEnabled(true);
                    binding.post.setTextColor(getActivity().getResources().getColor(R.color.blue));
                }
            }
        });

        binding.post.setOnClickListener(v -> {
            String message = String.valueOf(binding.postTextEt.getText());
            Firebase.Post(message,userModel.getFaculty(),getActivity());
            binding.postTextEt.setText("");
        });
    }

    private void setUpViews() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            String fullNameStr = user.getDisplayName();
            Log.d("PAKSoft", "setUpViews: " + user.getDisplayName());
            binding.userNameTv.setText(fullNameStr);
        }

    }

    private void setToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(binding.postToolbar);
    }

    private void getUserInfo()
    {
       DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
       mReference
                .child(USER_REF)
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userModel = snapshot.getValue(User.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {
                        Toast.makeText(getActivity(), "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}