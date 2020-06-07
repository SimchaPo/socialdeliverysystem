package com.example.socialdeliverysystem.Entites;

enum PACKAGE_SIZE{HALF,ONE,FIVE,TWENTY};
enum PACKAGE_TYPE{ENVELOPE, SMALL_PACKAGE, BIG_PACKAGE};

public class Parcel {
    private  String addresseeKey;
    private  String storage;
    private  PACKAGE_SIZE package_size;
    private  PACKAGE_TYPE package_type;
    private  boolean fragile;

    public Parcel(){
        //addressee = new Person();
    }

    public Parcel(String addresseeKey, PACKAGE_SIZE package_size, PACKAGE_TYPE package_type, boolean fragile) {
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

    public PACKAGE_SIZE getPackage_size() {
        return package_size;
    }

    public void setPackage_size(PACKAGE_SIZE package_size) {
        this.package_size = package_size;
    }

    public PACKAGE_TYPE getPackage_type() {
        return package_type;
    }

    public void setPackage_type(PACKAGE_TYPE package_type) {
        this.package_type = package_type;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

    @Override
    public String toString(){
        return addresseeKey + "\n" + storage + "\n" + package_size + "\n" + package_type + "\n"+ fragile;
    }
}