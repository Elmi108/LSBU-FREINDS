package com.android.lsbufriends.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.android.lsbufriends.databinding.FragmentChatBinding;
import com.android.lsbufriends.ui.base.BaseFragment;

public class ChatFragment extends BaseFragment {


    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    private FragmentChatBinding binding;

    @Override
    protected ViewBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentChatBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}