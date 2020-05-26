package com.example.socialdeliverysystem.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Button logInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
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
        logInBtn = findViewById(R.id.log_in_btn);
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
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editCode.getEditText().setText(code);
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
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Go to Activity
                        } else {
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                        }
                    }
                });
    }

    public void OpenTextBoxForCodeOrPassword(View view) {
        phoneOrMailText = editTextPhoneOrMailLogIn.getEditText().getText().toString().trim();
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
                            } else {
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
            //To do - check if phone number is registered
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
        logInBtn.setEnabled(false);
        if (email) {
            if (editPassword.getEditText().getText().toString().isEmpty()) {
                editPassword.setError("Please Enter Password");
                logInBtn.setEnabled(true);
                return;
            }
            mAuth.signInWithEmailAndPassword(phoneOrMailText, editPassword.getEditText().getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                editPassword.setError(null);
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    //startActivity
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please Verify Your Email Address",
                                            Toast.LENGTH_LONG).show();
                                }
                                //FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                                logInBtn.setEnabled(true);
                            }
                        }
                    });
        } else if (phone) {
            if (editCode.getEditText().getText().toString().isEmpty()) {
                editCode.setError("Please Enter Code");
                logInBtn.setEnabled(true);
                return;
            }
            verifyVerificationCode(editCode.getEditText().getText().toString());
        }
        return;
    }
}
