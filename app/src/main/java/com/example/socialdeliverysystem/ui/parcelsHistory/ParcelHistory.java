package com.example.socialdeliverysystem.ui.parcelsHistory;

import com.example.socialdeliverysystem.Entites.Person;

public class ParcelHistory {
    private Person deliver;
    private String parcelID;
    private String date;

    public ParcelHistory() {

    }

    public Person getDeliver() {
        return deliver;
    }

    public void setDeliver(Person deliver) {
        this.deliver = deliver;
    }

    public String getParcelID() {
        return parcelID;
    }

    public void setParcelID(String parcelID) {
        this.parcelID = parcelID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
