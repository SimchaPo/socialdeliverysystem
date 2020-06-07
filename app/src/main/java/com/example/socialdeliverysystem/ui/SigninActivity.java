package com.example.socialdeliverysystem.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.socialdeliverysystem.Entites.Person;
import com.example.socialdeliverysystem.R;
import com.example.socialdeliverysystem.Utils.FirebaseDBManager;
import com.example.socialdeliverysystem.Utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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


    public void SignInOnClick(View view) {
        if (validData()) {
            FirebaseDBManager.usersRef.orderByKey().equalTo(editTextPhoneSignin.getEditText().getText().toString())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String[] message = {"Phone User Already Exists"};
                            if (dataSnapshot.exists()) {
                                Toast.makeText(SigninActivity.this, message[0],
                                        Toast.LENGTH_LONG).show();
                            } else {
                                mAuth.createUserWithEmailAndPassword(editTextEmailSignin.getEditText().getText().toString(),
                                        editTextPasswordSignin.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        addUserToDB();
                                                        message[0] = "Please Check Your Email";
                                                        Toast.makeText(SigninActivity.this, message[0],
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                            // Sign in success, update UI with the signed-in user's information
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            message[0] = "Mail User Already Exists For Verification";
                                            Toast.makeText(SigninActivity.this, message[0],
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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
        return Validation.validateAddress(getApplicationContext() ,editTextAddressSignin) & Validation.validateEmail(editTextEmailSignin)
                & Validation.validateFirstName(editTextFirstNameSignin) & Validation.validateID(editTextIdSignin)
                & Validation.validateLastName(editTextLastNameSignin) & Validation.validatePassword(editTextPasswordSignin)
                & Validation.validatePhoneNumber(editTextPhoneSignin);
    }

    private Person getPersonFromScreen() {
        String firstName = editTextFirstNameSignin.getEditText().getText().toString();
        String lastName = editTextLastNameSignin.getEditText().getText().toString();
        String email = editTextEmailSignin.getEditText().getText().toString();
        String phoneNumber = editTextPhoneSignin.getEditText().getText().toString();
        String id = editTextIdSignin.getEditText().getText().toString();
        String address = editTextAddressSignin.getEditText().getText().toString();
        String password = editTextPasswordSignin.getEditText().getText().toString();
        return new Person(firstName, lastName, email, phoneNumber, id, address);
    }

    private void addUserToDB() {
        try {
            Person user = getPersonFromScreen();
            FirebaseDBManager.addUserToFirebase(user, new FirebaseDBManager.Action<String>() {
                @Override
                public void onSuccess(String obj) {

                }

                @Override
                public void onFailure(Exception exception) {

                }

                @Override
                public void onProgress(String status, double percent) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error ", Toast.LENGTH_LONG).show();
        }
    }
}
