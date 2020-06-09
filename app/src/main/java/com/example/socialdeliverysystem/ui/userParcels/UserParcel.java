package com.example.socialdeliverysystem.ui.userParcels;

import com.example.socialdeliverysystem.Entites.Parcel;

public class UserParcel {
    private String parcelID;
    private Parcel parcel;

    public UserParcel(String parcelID, Parcel parcel) {
        this.parcelID = parcelID;
        this.parcel = parcel;
    }

    public String getParcelID() {
        return parcelID;
    }

    public Parcel getParcel() {
        return parcel;
    }
}
