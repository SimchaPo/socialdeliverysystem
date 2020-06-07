package com.example.socialdeliverysystem.ui.friendsParcels;

import android.widget.CheckBox;

import com.example.socialdeliverysystem.Entites.Parcel;
import com.example.socialdeliverysystem.Entites.Person;

public class FriendsParcel {

    private String parcelID;
    private Person user;
    private String addresseeName;
    private Parcel parcel;
    private String addresseeAddress;
    private float distance;
    private CheckBox box;

    public FriendsParcel(Person user, Parcel parcel, String addresseeAddress, float distance, String parcelID, String addresseeName) {
        this.user = user;
        this.parcel = parcel;
        this.addresseeAddress = addresseeAddress;
        this.distance = distance;
        this.parcelID = parcelID;
        this.addresseeName = addresseeName;
    }

    public Person getUser() {
        return user;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public String getAddresseeName() {
        return addresseeName;
    }

    public String getAddresseeAddress() {
        return addresseeAddress;
    }

    public float getDistance() {
        return distance;
    }

    public String getParcelID() {
        return parcelID;
    }

    public CheckBox getBox() {
        return box;
    }
}
