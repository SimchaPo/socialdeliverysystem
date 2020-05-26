package com.example.socialdeliverysystem.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.socialdeliverysystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout editTextPhoneOrMailLogIn;
    private LinearLayoutCompat textBoxPhoneOrMailLayout;
    private LinearLayoutCompat passwordOrCodeEntryLayout;
    private LinearLayoutCompat passwordEntryLayout;
    private TextInputLayout editPassword;
    private LinearLayoutCompat codeEntryLayout;
    private TextInputLayout editCode;
    private boolean phone;
    private boolean email;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private String phoneOrMailText;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void findView() {
        editTextPhoneOrMailLogIn = findViewById(R.id.login_input_phone_mail);
        textBoxPhoneOrMailLayout = findViewById(R.id.email_or_phone_layout);
        passwordOrCodeEntryLayout = findViewById(R.id.password_code_entry);
        passwordEntryLayout = findViewById(R.id.password_entry);
        editPassword = findViewById(R.id.login_input_password);
        codeEntryLayout = findViewById(R.id.code_entry);
        editCode = findViewById(R.id.login_input_sms);
        mAuth = FirebaseAuth.getInstance();
    }

    public void SignInOnClick(View view) {
        Intent signIn = new Intent(getApplicationContext(), SigninActivity.class);
        startActivity(signIn);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+972" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editCode.getEditText().setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            PhoneAuthProvider.ForceResendingToken mResendToken = forceResendingToken;
        }
    };

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                        }
                    }
                });
    }

    public void OpenTextBoxForCodeOrPassword(View view) {
        phoneOrMailText = editTextPhoneOrMailLogIn.getEditText().getText().toString();
        if (phoneOrMailText.isEmpty()) {
            editTextPhoneOrMailLogIn.setError("Email or Phone Number is Required");
            return;
        }
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(phoneOrMailText).matches()) {
            mAuth.fetchSignInMethodsForEmail(phoneOrMailText)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.getResult().getSignInMethods().isEmpty()) {
                                editTextPhoneOrMailLogIn.setError("No Such User, Please Sign In");
                            }
                            else {
                                email = true;
                                phone = false;
                                textBoxPhoneOrMailLayout.setVisibility(View.GONE);
                                passwordOrCodeEntryLayout.setVisibility(View.VISIBLE);
                                passwordEntryLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
            return;
        }
        Pattern phonePattern = Pattern.compile("0([23489]|5[0123458]|77)([0-9]{7})");
        if (phonePattern.matcher(phoneOrMailText).matches()) {
            //To do - check if phone number is registed
            phone = true;
            email = false;
            textBoxPhoneOrMailLayout.setVisibility(View.GONE);
            passwordOrCodeEntryLayout.setVisibility(View.VISIBLE);
            codeEntryLayout.setVisibility(View.VISIBLE);
            sendVerificationCode(phoneOrMailText);
            return;
        }
        editTextPhoneOrMailLogIn.setError("Invalid Text");
        editTextPhoneOrMailLogIn.getEditText().setText(null);
    }

    public void GoBackOnClick(View view) {
        textBoxPhoneOrMailLayout.setVisibility(View.VISIBLE);
        passwordOrCodeEntryLayout.setVisibility(View.GONE);
        passwordEntryLayout.setVisibility(View.GONE);
        codeEntryLayout.setVisibility(View.GONE);
    }

    public void resetPasswordOnClick(View view) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(phoneOrMailText)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Mail For Reset Was Sent",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void logInOnClick(View view) {
        if (email) {
            mAuth.signInWithEmailAndPassword(phoneOrMailText, editPassword.getEditText().getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if(mAuth.getCurrentUser().isEmailVerified()){
                                    //startActivity
                                } else{
                                    Toast.makeText(LoginActivity.this, "Please Verify Your Email Address",
                                            Toast.LENGTH_LONG).show();
                                }
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else if (phone) {
            verifyVerificationCode(editCode.getEditText().getText().toString());
        }
        return;
    }
}
