package com.example.socialdeliverysystem.Entites;

import java.io.Serializable;

public class Person implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String ID;
    private String address;
    //private String password;

    public Person() {
    }

    public Person(String firstName, String lastName, String email, String phoneNumber, String ID, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ID = ID;
        this.address = address;
        //  this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {

        return "User Details:\n" + this.getFirstName() + " " + this.getLastName() + "\n" + this.getID() + "\n"
                + this.getEmail() + "\n" + this.getPhoneNumber() + "\n" + this.getAddress();
    }
}
