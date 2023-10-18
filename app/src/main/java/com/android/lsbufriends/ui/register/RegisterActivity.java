package com.android.lsbufriends.ui.register;

import static com.android.lsbufriends.utils.constants.ValidationConstants.validateEmail;
import static com.android.lsbufriends.utils.constants.ValidationConstants.validateFullName;
import static com.android.lsbufriends.utils.constants.ValidationConstants.validatePassword;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.lsbufriends.R;
import com.android.lsbufriends.data.firebase.Firebase;
import com.android.lsbufriends.data.model.register.RegisterRequest;
import com.android.lsbufriends.databinding.ActivityRegisterBinding;
import com.android.lsbufriends.ui.base.BaseActivity;
import com.android.lsbufriends.utils.fieldwatcher.TextFieldValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity {


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
    }

    private ActivityRegisterBinding binding;
    String firstNameStr,lastNameStr,mailStr,passwordStr,phoneNumberStr,facultyStr;

    private ProgressBar progressBar;
    boolean result;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private HashMap<String, Object> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewSetup();
        listeners();
    }

    private void listeners() {

        binding.firstNameEt.addTextChangedListener(new TextFieldValidation( binding.firstNameEt));
        binding.lastNameEt.addTextChangedListener(new TextFieldValidation( binding.lastNameEt));
        binding.gmailEt.addTextChangedListener(new TextFieldValidation( binding.gmailEt));
        binding.ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber) {
                    binding.phoneNumberEt.setTextColor(getResources().getColor(R.color.success_color));
                    binding.phoneNumberEt.getBackground().setColorFilter(
                            getResources().getColor(R.color.success_color),
                            PorterDuff.Mode.SRC_ATOP);
                } else {
                    binding.phoneNumberEt.setTextColor(getResources().getColor(R.color.error_color));
                    binding.phoneNumberEt.getBackground().setColorFilter(
                            getResources().getColor(R.color.error_color),
                            PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        binding.passwordET.addTextChangedListener(new TextFieldValidation( binding.passwordET));
        binding.confirmPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable p0) {
                if (p0 != null) {
                    if (p0.toString().isEmpty()) {
                        binding.confirmPasswordET.setError("Required Field");
                        binding.confirmPasswordET.requestFocus();
                    } else if (String.valueOf(p0).equals(String.valueOf( binding.passwordET.getText()))) {
                        binding.confirmPasswordET.setError(null);
                    } else {
                        binding.confirmPasswordET.setError("Password Not Matched");
                        binding.confirmPasswordET.requestFocus();
                    }
                }
            }
        });
        binding.registerBtn.setOnClickListener(v ->
        {
            if (isValidate()) {

                showProgressDialog();

                firstNameStr = String.valueOf( binding.firstNameEt.getText());
                lastNameStr = String.valueOf( binding.lastNameEt.getText());
                mailStr = String.valueOf( binding.gmailEt.getText());
                passwordStr = String.valueOf( binding.passwordET.getText());
                phoneNumberStr =  binding.ccp.getFullNumber();

                Firebase.RegisterUser(RegisterActivity.this, new RegisterRequest(firstNameStr, lastNameStr, facultyStr, mailStr, passwordStr, phoneNumberStr,FirebaseAuth.getInstance().getUid()));

            }
        });


        binding.facultyCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                facultyStr = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(RegisterActivity.this, "" + facultyStr, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private ColorStateList setStrokeColorState() {
        //Color from hex string
        int color = Color.parseColor("#290d41");

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

        binding.ccp.registerCarrierNumberEditText(binding.phoneNumberEt);

        binding.firstNameTil.setBoxStrokeColorStateList(setStrokeColorState());
        binding.lastNameTil.setBoxStrokeColorStateList(setStrokeColorState());
        binding.passwordTIL.setBoxStrokeColorStateList(setStrokeColorState());
        binding.confirmPasswordTIL.setBoxStrokeColorStateList(setStrokeColorState());
        binding.facultyTil.setBoxStrokeColorStateList(setStrokeColorState());
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] postCategoryStr = getResources().getStringArray(R.array.faculty);
        ArrayAdapter<String> postCategoryAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, postCategoryStr);
        binding.facultyCategory.setAdapter(postCategoryAdapter);
    }

    private boolean validateConfirmPassword(EditText editText) {
        if (editText.toString().isEmpty()) {
            binding.confirmPasswordET.setError("Required Field");
            binding.confirmPasswordET.requestFocus();
            return false;
        } else if (String.valueOf(editText.getText()).equals(String.valueOf(binding.passwordET.getText()))) {
            binding.confirmPasswordET.setError(null);
            return true;
        } else {
            binding.confirmPasswordET.setError("Password Not Matched");
            binding.confirmPasswordET.requestFocus();
            return false;
        }
    }

    public Boolean validateFaculty() {
        if (facultyStr.isEmpty()) {
            showErrorSnackBar("Please Select Faculty");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidate() {
        return (validateFullName(binding.firstNameEt) && validateFullName(binding.lastNameEt) && validateEmail(binding.gmailEt) && validatePassword(binding.passwordET) && validateConfirmPassword(binding.confirmPasswordET) && binding.ccp.isValidFullNumber() && validateFaculty());
    }
}