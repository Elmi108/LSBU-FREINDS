package com.android.lsbufriends.ui.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

import com.android.lsbufriends.databinding.FragmentUserProfileBinding;
import com.android.lsbufriends.ui.base.BaseFragment;

public class UserProfileFragment extends BaseFragment {


    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    private FragmentUserProfileBinding binding;

    @Override
    protected ViewBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentUserProfileBinding.inflate(getLayoutInflater());
        return binding;
    }
}