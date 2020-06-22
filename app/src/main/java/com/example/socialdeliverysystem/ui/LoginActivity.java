package com.example.socialdeliverysystem.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.socialdeliverysystem.Entites.Person;
import com.example.socialdeliverysystem.R;
import com.example.socialdeliverysystem.Utils.FirebaseDBManager;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseDBManager.getCurrentUser() != null && FirebaseDBManager.getCurrentUser().isEmailVerified()) {
            FirebaseDBManager.usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("email").getValue(String.class).equals(FirebaseDBManager.getCurrentUser().getEmail())) {
                            user = dataSnapshot.child(ds.getKey()).getValue(Person.class);
                            startMainActivity();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            setContentView(R.layout.activity_login);
            findView();
            mAuth = FirebaseAuth.getInstance();
            Intent i = getIntent();
            if (i.getSerializableExtra("email") != null && i.getSerializableExtra("password") != null) {
                editTextPhoneOrMailLogIn.getEditText().setText((String) i.getSerializableExtra("email"));
                phoneOrMailText = editTextPhoneOrMailLogIn.getEditText().getText().toString().trim();
                setLayotForEmailPassword();
                editPassword.getEditText().setText((String) i.getSerializableExtra("password"));
            }
        }
    }

    private void startMainActivity() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        FirebaseDBManager.setCurrentUserPerson(user);
        this.finishAffinity();
        startActivity(mainActivity);
    }

    private void findView() {
        editTextPhoneOrMailLogIn = findViewById(R.id.login_input_phone_mail);
        textBoxPhoneOrMailLayout = findViewById(R.id.email_or_phone_layout);
        passwordOrCodeEntryLayout = findViewById(R.id.password_code_entry);
        passwordEntryLayout = findViewById(R.id.password_entry);
        editPassword = findViewById(R.id.login_input_password);
        codeEntryLayout = findViewById(R.id.code_entry);
        editCode = findViewById(R.id.login_input_sms);
    }

    public void SignUpOnClick(View view) {
        Intent signUp = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(signUp);
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
                            FirebaseDBManager.usersRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    String userPhone = dataSnapshot.getKey();

                                    if (userPhone.equals(phoneOrMailText)) {
                                        user = dataSnapshot.getValue(Person.class);
                                        startMainActivity();
                                    }

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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
            singInWithMail();
            return;
        }
        Pattern phonePattern = Pattern.compile("05[0123458][0-9]{7}");
        if (phonePattern.matcher(phoneOrMailText).matches()) {
            FirebaseDBManager.usersRef.orderByKey().equalTo(phoneOrMailText).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        phone = true;
                        email = false;
                        editTextPhoneOrMailLogIn.setError(null);
                        textBoxPhoneOrMailLayout.setVisibility(View.GONE);
                        passwordOrCodeEntryLayout.setVisibility(View.VISIBLE);
                        codeEntryLayout.setVisibility(View.VISIBLE);
                        sendVerificationCode(phoneOrMailText);
                    } else {
                        editTextPhoneOrMailLogIn.setError("No Such User, Please Sign Up");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return;
        }
        editTextPhoneOrMailLogIn.setError("Invalid Text");
        editTextPhoneOrMailLogIn.getEditText().setText(null);
    }

    private void singInWithMail() {
        mAuth.fetchSignInMethodsForEmail(phoneOrMailText)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.getResult().getSignInMethods().isEmpty()) {
                            editTextPhoneOrMailLogIn.setError("No Such User, Please Sign In");
                        } else {
                            setLayotForEmailPassword();
                        }
                    }
                });
    }

    private void setLayotForEmailPassword() {
        email = true;
        phone = false;
        textBoxPhoneOrMailLayout.setVisibility(View.GONE);
        passwordOrCodeEntryLayout.setVisibility(View.VISIBLE);
        passwordEntryLayout.setVisibility(View.VISIBLE);
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
            if (editPassword.getEditText().getText().toString().isEmpty()) {
                editPassword.setError("Please Enter Password");
                return;
            }
            mAuth.signInWithEmailAndPassword(phoneOrMailText, editPassword.getEditText().getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                editPassword.setError(null);
                                if (FirebaseDBManager.getCurrentUser().isEmailVerified()) {
                                    FirebaseDBManager.usersRef.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                            String userEmail = dataSnapshot.child("email").getValue().toString().toLowerCase();

                                            if (userEmail.equals(phoneOrMailText.toLowerCase())) {
                                                user = dataSnapshot.getValue(Person.class);
                                                startMainActivity();
                                            }

                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please Verify Your Email Address",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else if (phone) {
            if (editCode.getEditText().getText().toString().isEmpty()) {
                editCode.setError("Please Enter Code");
                return;
            }

            verifyVerificationCode(editCode.getEditText().getText().toString());
        }
        return;
    }
}
