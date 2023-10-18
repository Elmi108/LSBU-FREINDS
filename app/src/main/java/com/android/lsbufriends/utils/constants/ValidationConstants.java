package com.android.lsbufriends.utils.constants;

import android.content.res.Resources;
import android.util.Patterns;
import android.widget.EditText;

import com.android.lsbufriends.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationConstants {

    public static Boolean isValidPattern(String input, String CHECK_PATTERN) {
        Pattern pattern = Pattern.compile(CHECK_PATTERN);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static Boolean validateFullName(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Required Field");
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().length() < 3) {
            editText.setError("Please Enter more then 2");
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().length() > 50) {
            editText.setError("Entered name is too long");
            editText.requestFocus();
            return false;
        } else {
            editText.setError(null);
        }
        return true;
    }

    public static Boolean validateEmail(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Required Field");
            editText.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches()) {
            editText.setError("Email is not valid");
            editText.requestFocus();
            return false;
        } else {
            editText.setError(null);
        }
        return true;
    }

    public static Boolean validatePassword(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Required Field");
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().length() < 8) {
            editText.setError("Password must more than 8");
            editText.requestFocus();
            return false;
        } else {
            editText.setError(null);
        }
        return true;
    }

    public static Boolean validateEntity(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Required Field");
            editText.requestFocus();
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }
}
