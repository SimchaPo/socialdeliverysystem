package com.example.socialdeliverysystem.Utils;

import android.content.Context;
import android.location.Address;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.regex.Pattern;

public class Validation {
    static private Pattern phonePattern = Pattern.compile("0([23489]|5[0123458]|77)([0-9]{7})");
    static private Pattern passwordPattern = Pattern.compile("(?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!^&*+=-_]).{4,20}");

    public static boolean validateFirstName(TextInputLayout firstName) {
        String firstNameInput = firstName.getEditText().getText().toString().trim();
        String error;
        if (firstNameInput.isEmpty()) {
            error = "First Name is required";
        } else {
            error = null;
        }
        firstName.setError(error);
        return error == null;
    }

    public static boolean validateLastName(TextInputLayout lastName) {
        String lastNameInput = lastName.getEditText().getText().toString().trim();
        String error;
        if (lastNameInput.isEmpty()) {
            error = "Last Name is required";
        } else {
            error = null;
        }
        lastName.setError(error);
        return error == null;
    }

    public static boolean validatePhoneNumber(TextInputLayout phoneNumber) {
        String phoneInput = phoneNumber.getEditText().getText().toString().trim();
        String error;
        if (!phoneInput.isEmpty() && phonePattern.matcher(phoneInput).matches()) {
            error = null;
        } else if (phoneInput.isEmpty()) {
            error = "Phone is required";
        } else {
            error = "Invalid Phone Number!";
        }
        phoneNumber.setError(error);
        return error == null;
    }

    public static boolean validateID(TextInputLayout id) {
        String idInput = id.getEditText().getText().toString().trim();
        String error = null;
        if (idInput.isEmpty()) {
            error = "ID is required";
        } else if (idInput.length() != 9) {
            error = "ID Number Must Contain 9 Characters";
        } else {
            char[] idChars = idInput.toCharArray();
            int[] idNumbs = new int[9];
            for (int i = 0; i < 9; ++i) {
                idNumbs[i] = Character.getNumericValue(idChars[i]);
            }
            int sum = 0, j;
            for (int i = 0; i <= 6; i += 2) {
                sum += idNumbs[i];
                j = 2 * idNumbs[i + 1];
                if (j > 9) {
                    j = 1 + j % 10;
                }
                sum += j;
            }
            if (10 - (sum % 10) != (int) idNumbs[8]) {
                error = "Invalid ID Number";
            } else {
                error = null;
            }
        }
        id.setError(error);
        return error == null;
    }

    public static boolean validateAddress(Context context, TextInputLayout address) {
        String addressInput = address.getEditText().getText().toString().trim();
        String errorMessage;
        if (addressInput.isEmpty())
            errorMessage = "Address is required";
        else {
            List<Address> addresses = LocationManage.checkLocationFromAddress(context, addressInput);
            if (addresses.size() == 0) {
                errorMessage = "Invalid Address!";
            } else {
                if (addresses.get(0).getThoroughfare() == null) {
                    errorMessage = "Missing Street Name";
                } else {
                    address.getEditText().setText(addresses.get(0).getAddressLine(0));
                    errorMessage = null;
                }
            }
        }
        address.setError(errorMessage);
        return errorMessage == null;
    }

    public static boolean validateEmail(TextInputLayout email) {
        String emailInput = email.getEditText().getText().toString().trim();
        String error;
        if (!emailInput.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            error = null;
        } else if (emailInput.isEmpty()) {
            error = "Email is required";
        } else {
            error = "Invalid Email Address!";
        }
        email.setError(error);
        return error == null;
    }


    public static boolean validatePassword(TextInputLayout password) {
        String passwordInput = password.getEditText().getText().toString().trim();
        String error;
        if (passwordPattern.matcher(passwordInput).matches()) {
            error = null;
        } else if (passwordInput.isEmpty()) {
            error = "Password is required";
        } else {
            error = "Password must contain mix of upper and lower case letters as well as digits and one special character(4-20)";
        }
        password.setError(error);
        return error == null;
    }

}
