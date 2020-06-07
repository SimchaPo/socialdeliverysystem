package com.example.socialdeliverysystem.Entites;

public class Parcel {
    private  String addresseeKey;
    private  String storage;
    private  String package_size;
    private  String package_type;
    private  String fragile;

    public Parcel(){
    }

    public Parcel(String addresseeKey, String package_size, String package_type, String fragile) {
        this.addresseeKey = addresseeKey;
        this.package_size = package_size;
        this.package_type = package_type;
        this.fragile = fragile;
    }

    public String getAddresseeKey() {
        return addresseeKey;
    }

    public void setAddresseeKey(String addresseeKey) {
        this.addresseeKey = addresseeKey;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getPackage_size() {
        return package_size;
    }

    public void setPackage_size(String package_size) {
        this.package_size = package_size;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String isFragile() {
        return fragile;
    }

    public void setFragile(String fragile) {
        this.fragile = fragile;
    }

    @Override
    public String toString(){
        return addresseeKey + "\n" + storage + "\n" + package_size + "\n" + package_type + "\n"+ fragile;
    }
}