package com.android.lsbufriends.ui.login;

import static com.android.lsbufriends.utils.constants.ValidationConstants.validateEntity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.lsbufriends.R;
import com.android.lsbufriends.data.firebase.Firebase;
import com.android.lsbufriends.data.model.login.LoginRequest;
import com.android.lsbufriends.databinding.ActivityLoginBinding;
import com.android.lsbufriends.ui.base.BaseActivity;
import com.android.lsbufriends.ui.register.RegisterActivity;
import com.android.lsbufriends.ui.resetpassword.ResetPasswordActivity;
import com.android.lsbufriends.utils.constants.ValidationConstants;

public class LoginActivity extends BaseActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
        activity.finishAffinity();
    }

    private ActivityLoginBinding binding;
    private String gmailStr, passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //checkUserLoggedIn();
        setUpViews();
        Listeners();
    }

    @NonNull
    private ColorStateList setStrokeColorState() {
        //Color from hex string
        int color = Color.parseColor("#00F57C");

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused}, // focused
                new int[]{android.R.attr.state_hovered}, // hovered
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{}  //
        };

        int[] colors = new int[]{
                color,
                color,
                color,
                color
        };

        return new ColorStateList(states, colors);
    }

    private void setUpViews() {
        binding.mailTIL.setBoxStrokeColorStateList(setStrokeColorState());
        binding.passwordTIL.setBoxStrokeColorStateList(setStrokeColorState());
    }

    private boolean isValidate() {
        return (ValidationConstants.validateEmail(binding.mailEt) && validateEntity(binding.passwordET));
    }

    private void Listeners() {
        binding.signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    gmailStr = String.valueOf(binding.mailEt.getText());
                    passwordStr = String.valueOf(binding.passwordET.getText());
                    Firebase.LoginUser(LoginActivity.this,new LoginRequest(gmailStr,passwordStr));
                }
            }
        });
        binding.registerTv.setOnClickListener(v -> {
            RegisterActivity.start(this);
        });

        binding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You can reset your password now!", Toast.LENGTH_SHORT).show();
                ResetPasswordActivity.start(LoginActivity.this);
            }
        });
    }

    private void checkUserLoggedIn() {
        Firebase.UserLoggedIn(LoginActivity.this);
    }


}