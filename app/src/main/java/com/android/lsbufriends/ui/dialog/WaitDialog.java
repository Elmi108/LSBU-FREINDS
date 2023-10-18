package com.android.lsbufriends.ui.dialog;

import android.content.Context;
import android.os.Bundle;

import com.android.lsbufriends.databinding.DialogWaitBinding;
import com.android.lsbufriends.ui.base.BaseDialog;

public class WaitDialog extends BaseDialog {

    public WaitDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogWaitBinding binding = DialogWaitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCancelable(false);
    }
}