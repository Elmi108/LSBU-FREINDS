package com.android.lsbufriends.ui.resetpassword;

import static com.android.lsbufriends.data.firebase.Firebase.ResetUserPassword;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.android.lsbufriends.R;
import com.android.lsbufriends.databinding.ActivityResetPasswordBinding;
import com.android.lsbufriends.ui.base.BaseActivity;
import com.android.lsbufriends.utils.fieldwatcher.TextFieldValidation;

public class ResetPasswordActivity extends BaseActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ResetPasswordActivity.class);
        activity.overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
        activity.startActivity(intent);
    }

    private ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewSetup();
        Listeners();
    }

    private ColorStateList setStrokeColorState() {
        //Color from hex string
        int color = Color.parseColor("#361156");

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

    private void viewSetup() {
        binding.mailTIL.setBoxStrokeColorStateList(setStrokeColorState());
    }

    private void Listeners() {
        binding.gmailEt.addTextChangedListener(new TextFieldValidation(binding.gmailEt));
        binding.nextTv.setOnClickListener(v -> {
            ResetUserPassword(ResetPasswordActivity.this,String.valueOf(binding.gmailEt.getText()));
        });
    }
}