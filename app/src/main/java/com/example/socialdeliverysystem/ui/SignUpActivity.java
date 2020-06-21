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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout editTextFirstNameSignUp;
    private TextInputLayout editTextLastNameSignUp;
    private TextInputLayout editTextEmailSignUp;
    private TextInputLayout editTextPhoneSignUp;
    private TextInputLayout editTextIdSignUp;
    private TextInputLayout editTextAddressSignUp;
    private TextInputLayout editTextPasswordSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findView();
    }

    private void findView() {
        editTextFirstNameSignUp = findViewById(R.id.sign_up_input_first_name);
        editTextLastNameSignUp = findViewById(R.id.sign_up_input_last_name);
        editTextEmailSignUp = findViewById(R.id.sign_up_input_email);
        editTextPhoneSignUp = findViewById(R.id.sign_up_input_phone);
        editTextIdSignUp = findViewById(R.id.sign_up_input_id);
        editTextAddressSignUp = findViewById(R.id.sign_up_input_address);
        editTextPasswordSignUp = findViewById(R.id.sign_up_input_password);
        mAuth = FirebaseAuth.getInstance();
    }

    private String message;

    public void SignUpOnClick(View view) {
        if (validData()) {
            FirebaseDBManager.usersRef.orderByKey().equalTo(editTextPhoneSignUp.getEditText().getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        message = "Phone User Already Exists For Verification";
                        Toast.makeText(SignUpActivity.this, message,
                                Toast.LENGTH_LONG).show();
                    } else {
                        mAuth.createUserWithEmailAndPassword(editTextEmailSignUp.getEditText().getText().toString(),
                                editTextPasswordSignUp.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                addUserToDB();
                                                message = "Please Check Your Email and Verify";
                                                Toast.makeText(SignUpActivity.this, message,
                                                        Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                                    Intent logInActivity = new Intent(getApplicationContext(), LoginActivity.class);
                                    logInActivity.putExtra("email", editTextEmailSignUp.getEditText().getText().toString());
                                    logInActivity.putExtra("password", editTextPasswordSignUp.getEditText().getText().toString());
                                    startActivity(logInActivity);

                                } else {
                                    message = "Mail User Already Exists For Verification";
                                    Toast.makeText(SignUpActivity.this, message,
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
        return Validation.validateAddress(getApplicationContext(), editTextAddressSignUp) & Validation.validateEmail(editTextEmailSignUp)
                & Validation.validateFirstName(editTextFirstNameSignUp) & Validation.validateID(editTextIdSignUp)
                & Validation.validateLastName(editTextLastNameSignUp) & Validation.validatePassword(editTextPasswordSignUp)
                & Validation.validatePhoneNumber(editTextPhoneSignUp);
    }

    private Person getPersonFromScreen() {
        String firstName = editTextFirstNameSignUp.getEditText().getText().toString();
        String lastName = editTextLastNameSignUp.getEditText().getText().toString();
        String email = editTextEmailSignUp.getEditText().getText().toString().trim();
        String phoneNumber = editTextPhoneSignUp.getEditText().getText().toString().trim();
        String id = editTextIdSignUp.getEditText().getText().toString().trim();
        String address = editTextAddressSignUp.getEditText().getText().toString();
        return new Person(firstName, lastName, email, phoneNumber, id, address);
    }

    private void addUserToDB() {
        try {
            Person user = getPersonFromScreen();
            FirebaseDBManager.addUserToFirebase(user);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error ", Toast.LENGTH_LONG).show();
        }
    }
}
