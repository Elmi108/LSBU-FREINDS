package com.android.lsbufriends.utils.fieldwatcher;

import static com.android.lsbufriends.utils.constants.ValidationConstants.validateEmail;
import static com.android.lsbufriends.utils.constants.ValidationConstants.validateFullName;
import static com.android.lsbufriends.utils.constants.ValidationConstants.validatePassword;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.lsbufriends.R;

public class TextFieldValidation implements TextWatcher {
    private final EditText view;

    public TextFieldValidation(EditText v) {
        view = v;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        switch (view.getId()) {
            case R.id.first_name_et:
                validateFullName(view);
                break;
            case R.id.last_name_et:
                validateFullName(view);
                break;
            case R.id.gmail_et:
                validateEmail(view);
                break;
            case R.id.passwordET:
                validatePassword(view);
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
