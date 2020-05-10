package com.example.socialdeliverysystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout editTextEmailLogin;
    private TextInputLayout editTextPasswordLogin;
    private TextInputLayout getEditTextSmsLogin;
    private LinearLayoutCompat loginLayout;
    private LinearLayoutCompat sms_login;
    private LinearLayoutCompat password_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        sms_login.setVisibility(View.GONE);
    }

    private void findView() {
        editTextEmailLogin = findViewById(R.id.login_input_email);
        editTextPasswordLogin = findViewById(R.id.login_input_password);
        getEditTextSmsLogin = findViewById(R.id.login_input_sms);
        loginLayout = findViewById(R.id.login_layout);
        sms_login = findViewById(R.id.sms_entry);
        password_login = findViewById(R.id.password_entry);
    }


    public void logInOnClick(View view) {
        return;
    }

    public void logInSmsOnClick(View view) {
        if (!checkEmailEditTextBox(editTextEmailLogin.getEditText().getText().toString())){
            editTextEmailLogin.setError("EMAIL ADDRESS NOT CORRECT!");
            return;
        }
        editTextEmailLogin.setEnabled(false);
        editTextPasswordLogin.getEditText().getText().clear();
        password_login.setVisibility(View.GONE);
        sms_login.setVisibility(View.VISIBLE);
    }

    private boolean checkEmailEditTextBox(String toString) {
        //To Do!!
        return  true;
    }

    public void logInPasswordOnClick(View view) {
        editTextEmailLogin.setEnabled(true);
        getEditTextSmsLogin.getEditText().getText().clear();
        sms_login.setVisibility(View.GONE);
        password_login.setVisibility(View.VISIBLE);
    }

    public void SignInOnClick(View view) {
        Intent signIn = new Intent(getApplicationContext(), SigninActivity.class);
        startActivity(signIn);
    }
}
