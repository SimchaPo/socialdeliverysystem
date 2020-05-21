package com.example.socialdeliverysystem.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout editTextPhoneLogin;
    private TextInputLayout editTextPasswordLogin;
    private TextInputLayout getEditTextSmsLogin;
    private LinearLayoutCompat loginLayout;
    private LinearLayoutCompat sms_login;
    private LinearLayoutCompat password_login;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    private EditText editTextCode;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        mAuth = FirebaseAuth.getInstance();
        sms_login.setVisibility(View.GONE);
    }

    private void findView() {
        editTextPhoneLogin = findViewById(R.id.login_input_phone);
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
        if (!checkPhoneEditTextBox(editTextPhoneLogin.getEditText().getText().toString())) {
            editTextPhoneLogin.setError("PHONE NUMBER NOT CORRECT!");
            return;
        }
        editTextPhoneLogin.setEnabled(false);
        editTextPasswordLogin.getEditText().getText().clear();
        password_login.setVisibility(View.GONE);
        sms_login.setVisibility(View.VISIBLE);
        sendVerificationCode(editTextPhoneLogin.getEditText().getText().toString().trim());
    }

    private boolean checkPhoneEditTextBox(String toString) {
        //To Do!!
        return true;
    }

    public void logInPasswordOnClick(View view) {
        editTextPhoneLogin.setEnabled(true);
        getEditTextSmsLogin.getEditText().getText().clear();
        sms_login.setVisibility(View.GONE);
        password_login.setVisibility(View.VISIBLE);

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
                getEditTextSmsLogin.getEditText().setText(code);
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
            mResendToken = forceResendingToken;
            //   resendVerificationCode(editTextPhoneLogin.getEditText().getText().toString().trim(), mResendToken);

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
                            //verification successful we will start the profile activity
                            //  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //  startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            //Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            //snackbar.setAction("Dismiss", new View.OnClickListener() {
                            //    @Override
                            //    public void onClick(View v) {

                            //    }
                            //});
                            //snackbar.show();
                        }
                    }
                });
    }
}
