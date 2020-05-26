package com.example.socialdeliverysystem.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.socialdeliverysystem.R;
import com.example.socialdeliverysystem.ui.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.regex.Pattern;

public class SigninActivity extends AppCompatActivity {

    private TextInputLayout editTextFirstNameSignin;
    private TextInputLayout editTextLastNameSignin;
    private TextInputLayout editTextEmailSignin;
    private TextInputLayout editTextPhoneSignin;
    private TextInputLayout editTextIdSignin;
    private TextInputLayout editTextAddressSignin;
    private TextInputLayout editTextPasswordSignin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        findView();
    }

    private void findView() {
        editTextFirstNameSignin = findViewById(R.id.signin_input_firstname);
        editTextLastNameSignin = findViewById(R.id.signin_input_lastname);
        editTextEmailSignin = findViewById(R.id.signin_input_email);
        editTextPhoneSignin = findViewById(R.id.signin_input_phone);
        editTextIdSignin = findViewById(R.id.signin_input_id);
        editTextAddressSignin = findViewById(R.id.signin_input_address);
        editTextPasswordSignin = findViewById(R.id.signin_input_password);
        mAuth = FirebaseAuth.getInstance();
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
        Pattern phonePattern = Pattern.compile("0([23489]|5[0123458]|77)([0-9]{7})");
        if (!phoneInput.isEmpty() && phonePattern.matcher(phoneInput).matches()) {
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

    public void SignInOnClick(View view) {
        if (validData()) {
            mAuth.createUserWithEmailAndPassword(editTextEmailSignin.getEditText().getText().toString(), editTextPasswordSignin.getEditText().getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SigninActivity.this, "Please Check Your Email",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SigninActivity.this, "Mail User Already Exists For Verification",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        return;
    }

    public void logInOnClick(View view) {
        Intent logInActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(logInActivity);
    }

    private boolean validData() {
        return validateSigninAddress() & validateSigninEmail() & validateSigninFirstName() & validateSigninID() &
                validateSigninLastName() & validateSigninPassword() & validateSigninPhone();
    }
}
