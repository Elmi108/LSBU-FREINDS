package com.android.lsbufriends.ui.main.handler;

import android.view.View;

import com.android.lsbufriends.R;
import com.android.lsbufriends.data.firebase.Firebase;
import com.android.lsbufriends.databinding.ActivityMainBinding;
import com.android.lsbufriends.ui.base.BaseFragment;
import com.android.lsbufriends.ui.chat.ChatFragment;
import com.android.lsbufriends.ui.home.HomeFragment;
import com.android.lsbufriends.ui.main.MainActivity;
import com.android.lsbufriends.ui.post.AddPostFragment;
import com.android.lsbufriends.ui.profile.UserProfileFragment;

public class MainMenuHandler {

    private MainActivity activity;
    private ActivityMainBinding binding;

    public MainMenuHandler(MainActivity activity, ActivityMainBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

    public void setUpClickListeners() {
        binding.ivHome.setOnClickListener(v -> {
            onClickBottomMenuItem(binding.ivHome, activity, binding);
        });
        binding.ivChat.setOnClickListener(v -> {
            onClickBottomMenuItem(binding.ivChat, activity, binding);
        });
        binding.ivAddPost.setOnClickListener(v -> {
            onClickBottomMenuItem(binding.ivAddPost, activity, binding);
        });
        binding.ivUser.setOnClickListener(v -> {
            onClickBottomMenuItem(binding.ivUser, activity, binding);
        });
        binding.ivLogOut.setOnClickListener(v -> {
            onClickBottomMenuItem(binding.ivLogOut, activity, binding);
        });
    }


    private void onClickBottomMenuItem(
            View button,
            MainActivity activity,
            ActivityMainBinding binding
    ) {
        if (binding.ivHome.equals(button)) {
            replaceFragment(HomeFragment.newInstance(), activity, binding);
            binding.ivHome.setImageResource(R.drawable.ic_home_checked);
            binding.ivChat.setImageResource(R.drawable.ic_chat);
            binding.ivAddPost.setImageResource(R.drawable.ic_add);
            binding.ivUser.setImageResource(R.drawable.ic_user);
            binding.ivLogOut.setImageResource(R.drawable.ic_logout);
        } else if (binding.ivChat.equals(button)) {
            replaceFragment(ChatFragment.newInstance(), activity, binding);
            binding.ivHome.setImageResource(R.drawable.ic_home);
            binding.ivChat.setImageResource(R.drawable.ic_chat_checked);
            binding.ivAddPost.setImageResource(R.drawable.ic_add);
            binding.ivUser.setImageResource(R.drawable.ic_user);
            binding.ivLogOut.setImageResource(R.drawable.ic_logout);
        } else if (binding.ivAddPost.equals(button)) {
            replaceFragment(AddPostFragment.newInstance(), activity, binding);
            binding.ivHome.setImageResource(R.drawable.ic_home);
            binding.ivChat.setImageResource(R.drawable.ic_chat);
            binding.ivAddPost.setImageResource(R.drawable.ic_add_post_checked);
            binding.ivUser.setImageResource(R.drawable.ic_user);
            binding.ivLogOut.setImageResource(R.drawable.ic_logout);
        } else if (binding.ivUser.equals(button)) {
            replaceFragment(UserProfileFragment.newInstance(), activity, binding);
            binding.ivHome.setImageResource(R.drawable.ic_home);
            binding.ivChat.setImageResource(R.drawable.ic_chat);
            binding.ivAddPost.setImageResource(R.drawable.ic_add);
            binding.ivUser.setImageResource(R.drawable.ic_user_checked);
            binding.ivLogOut.setImageResource(R.drawable.ic_logout);
        } else if (binding.ivLogOut.equals(button)) {
            Firebase.LogoutUser(activity);
        }
    }

    private void replaceFragment(
            BaseFragment fragment,
            MainActivity activity,
            ActivityMainBinding binding
    ) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .setCustomAnimations(
                        R.anim.enter,
                        R.anim.exit,
                        R.anim.pop_enter,
                        R.anim.pop_exit
                ).commit();
    }

}

