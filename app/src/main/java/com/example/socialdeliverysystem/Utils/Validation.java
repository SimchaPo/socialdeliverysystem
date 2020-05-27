package com.example.socialdeliverysystem.Utils;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class Validation {
    static private Pattern phonePattern = Pattern.compile("0([23489]|5[0123458]|77)([0-9]{7})");
    static private Pattern passwordPattern = Pattern.compile("(?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!^&*+=-_]).{4,20}");

    public static boolean validateFirstName(TextInputLayout firstName) {
        String firstNameInput = firstName.getEditText().getText().toString().trim();
        if (firstNameInput.isEmpty()) {
            firstName.setError("First Name is required");
            return false;
        }
        firstName.setError(null);
        return true;
    }

    public static boolean validateSigninLastName(TextInputLayout lastName) {
        String lastNameInput = lastName.getEditText().getText().toString().trim();
        if (lastNameInput.isEmpty()) {
            lastName.setError("Last Name is required");
            return false;
        }
        lastName.setError(null);
        return true;
    }

    public static boolean validatePhoneNumber(TextInputLayout phoneNumber) {
        String phoneInput = phoneNumber.getEditText().getText().toString().trim();
        if (!phoneInput.isEmpty() && phonePattern.matcher(phoneInput).matches()) {
            phoneNumber.setError(null);
            return true;
        }
        String error;
        if (phoneInput.isEmpty())
            error = "Phone is required";
        else
            error = "Invalid Phone Number!";
        phoneNumber.setError(error);
        return false;
    }

    public static boolean validateID(TextInputLayout id) {
        String idInput = id.getEditText().getText().toString().trim();
        if (idInput.length() == 9) {
            id.setError(null);
            return true;
        }
        String error;
        if (idInput.isEmpty())
            error = "ID is required";
        else
            error = "Invalid ID Number!";
        id.setError(error);
        return false;
    }

    public static boolean validateAddress(TextInputLayout address) {
        String addressInput = address.getEditText().getText().toString().trim();
        if (!addressInput.isEmpty()) {
            address.setError(null);
            return true;
        }
        String error;
        if (addressInput.isEmpty())
            error = "Address is required";
        else
            error = "Invalid Address!";
        address.setError(error);
        return false;
    }

    public static boolean validateEmail(TextInputLayout email) {
        String emailInput = email.getEditText().getText().toString().trim();
        if (!emailInput.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError(null);
            return true;
        }
        String error;
        if (emailInput.isEmpty())
            error = "Email is required";
        else
            error = "Invalid Email Address!";
        email.setError(error);
        return false;
    }


    public static boolean validatePassword(TextInputLayout password) {
        String passwordInput = password.getEditText().getText().toString().trim();
        if (passwordPattern.matcher(passwordInput).matches()) {
            password.setError(null);
            return true;
        }
        String error;
        if (passwordInput.isEmpty())
            error = "Password is required";
        else
            error = "Password must contain mix of upper and lower case letters as well as digits and one special character(4-20)";
        password.setError(error);
        return false;
    }

}
