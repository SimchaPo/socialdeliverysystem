package com.example.socialdeliverysystem.ui.friendsParcels;

import android.content.Context;

import com.example.socialdeliverysystem.Entites.Parcel;
import com.example.socialdeliverysystem.Entites.Person;
import com.example.socialdeliverysystem.Utils.LocationManage;

public class FriendsParcel {

    private String parcelID;
    private Person user;
    private Person addressee;
    private String addresseeName;
    private Parcel parcel;
    private String addresseeAddress;
    private float distance;

    public FriendsParcel(Person user, Person addressee, Parcel value, String parcelID, Context context) {
        this.user = user;
        this.addressee = addressee;
        this.parcel = value;
        this.parcelID = parcelID;
        this.distance = LocationManage.getDistanceBetweenTwoLocations(context, user.getAddress(), addressee.getAddress());
        this.addresseeAddress = addressee.getAddress();
        this.addresseeName = addressee.getFirstName() + " " + addressee.getLastName();
    }

    public Person getAddressee() {
        return addressee;
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

}
