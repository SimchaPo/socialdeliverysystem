package com.example.socialdeliverysystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout editTextEmailLogin;
    private TextInputLayout editTextPasswordLogin;
    private TextInputLayout editTextFirstNameSignin;
    private TextInputLayout editTextLastNameSignin;
    private TextInputLayout editTextEmailSignin;
    private TextInputLayout editTextPhoneSignin;
    private TextInputLayout editTextIdSignin;
    private TextInputLayout editTextAddressSignin;
    private TextInputLayout editTextPasswordSignin;
    private LinearLayoutCompat loginLayout;
    private LinearLayoutCompat signinLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        loginLayout.setVisibility(View.GONE);
        signinLayout.setVisibility(View.GONE);
    }

    void findView() {
        editTextEmailLogin = findViewById(R.id.login_input_email);
        editTextPasswordLogin = findViewById(R.id.login_input_password);
        editTextFirstNameSignin = findViewById(R.id.signin_input_firstname);
        editTextLastNameSignin = findViewById(R.id.signin_input_lastname);
        editTextEmailSignin = findViewById(R.id.signin_input_email);
        editTextPhoneSignin = findViewById(R.id.signin_input_phone);
        editTextIdSignin = findViewById(R.id.signin_input_id);
        editTextAddressSignin = findViewById(R.id.signin_input_address);
        editTextPasswordSignin = findViewById(R.id.signin_input_password);
        loginLayout = findViewById(R.id.login_layout);
        signinLayout = findViewById(R.id.signin_layout);
    }

    private boolean validateSigninFirstName() {
        String firstNameInput = editTextFirstNameSignin.getEditText().getText().toString().trim();
        if (firstNameInput.isEmpty()) {
            editTextFirstNameSignin.setError("First Name is required");
            return false;
        }
        editTextFirstNameSignin.setError(null);
        return true;
    }

    private boolean validateSigninLastName() {
        String lastNameInput = editTextLastNameSignin.getEditText().getText().toString().trim();
        if (lastNameInput.isEmpty()) {
            editTextLastNameSignin.setError("Last Name is required");
            return false;
        }
        editTextLastNameSignin.setError(null);
        return true;
    }

    private boolean validateSigninPhone() {
        String phoneInput = editTextPhoneSignin.getEditText().getText().toString().trim();
        // Pattern phonePattern = Pattern.compile("(00972|0|\\+972)[5][0-9]{8} | (00970|0|\\+970)[5][0-9]{8} | (05[0-9]|0[12346789])([0-9]{7}) | (00972|0|\\+972|0|)[2][0-9]{7}");
        if (!phoneInput.isEmpty() /*&&phonePattern.matcher(phoneInput).matches()*/) {
            editTextPhoneSignin.setError(null);
            return true;
        }
        String error;
        if (phoneInput.isEmpty())
            error = "Phone is required";
        else
            error = "Invalid Phone Number!";
        editTextPhoneSignin.setError(error);
        return false;
    }

    private boolean validateSigninID() {
        String idInput = editTextIdSignin.getEditText().getText().toString().trim();
        if (idInput.length() == 9) {
            editTextIdSignin.setError(null);
            return true;
        }
        String error;
        if (idInput.isEmpty())
            error = "ID is required";
        else
            error = "Invalid ID Number!";
        editTextIdSignin.setError(error);
        return false;
    }

    private boolean validateSigninAddress() {
        String addressInput = editTextAddressSignin.getEditText().getText().toString().trim();
        if (!addressInput.isEmpty()) {
            editTextAddressSignin.setError(null);
            return true;
        }
        String error;
        if (addressInput.isEmpty())
            error = "Address is required";
        else
            error = "Invalid Address!";
        editTextAddressSignin.setError(error);
        return false;
    }

    private boolean validateSigninEmail() {
        String emailInput = editTextEmailSignin.getEditText().getText().toString().trim();
        if (!emailInput.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            editTextEmailSignin.setError(null);
            return true;
        }
        String error;
        if (emailInput.isEmpty())
            error = "Email is required";
        else
            error = "Invalid Email Address!";
        editTextEmailSignin.setError(error);
        return false;
    }


    private boolean validateSigninPassword() {
        String passwordInput = editTextPasswordSignin.getEditText().getText().toString().trim();
        if (Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!^&*+=-_]).{4,20})").matcher(passwordInput).matches()) {
            editTextPasswordSignin.setError(null);
            return true;
        }
        String error;
        if (passwordInput.isEmpty())
            error = "Password is required";
        else
            error = "Password must contain mix of upper and lower case letters as well as digits and one special character(4-20)";
        editTextPasswordSignin.setError(error);
        return false;
    }

    public void logInOnClick(View view) {
        if(loginLayout.getVisibility() == View.GONE){
            signinLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        }
        return;
    }

    public void SignInOnClick(View view) {
        if(signinLayout.getVisibility() == View.GONE){
            loginLayout.setVisibility(View.GONE);
            signinLayout.setVisibility(View.VISIBLE);
            return;
        }
        if (!validateSigninAddress() | !validateSigninEmail() | !validateSigninFirstName() | !validateSigninID() | !validateSigninLastName() | !validateSigninPassword() | !validateSigninPhone())
            return;
    }
}
