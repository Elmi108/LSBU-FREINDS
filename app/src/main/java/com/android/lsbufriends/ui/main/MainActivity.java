package com.android.lsbufriends.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.lsbufriends.R;
import com.android.lsbufriends.databinding.ActivityMainBinding;
import com.android.lsbufriends.ui.main.handler.MainMenuHandler;

public class MainActivity extends AppCompatActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
        activity.finishAffinity();
    }

    private ActivityMainBinding binding;
    private MainMenuHandler mainMenuHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainMenuHandler = new MainMenuHandler(MainActivity.this,binding);
        mainMenuHandler.setUpClickListeners();
        binding.ivHome.performClick();
    }


}