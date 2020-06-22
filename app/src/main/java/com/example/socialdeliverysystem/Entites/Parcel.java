//Ari Rubin 315528547 & Simcha Podolsky 311215149
//Simcha Podolsky 311215149 & Ari Rubin 315528547
package com.example.socialdeliverysystem.Entites;

public class Parcel {
    private  String addresseeKey;
    private  String storage;
    private  String packageSize;
    private  String packageType;
    private  String fragile;

    public Parcel(){
    }

    public Parcel(String addresseeKey, String package_size, String package_type, String fragile) {
        this.addresseeKey = addresseeKey;
        this.packageSize = package_size;
        this.packageType = package_type;
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

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String isFragile() {
        return fragile;
    }

    public void setFragile(String fragile) {
        this.fragile = fragile;
    }

    @Override
    public String toString(){
        return addresseeKey + "\n" + storage + "\n" + packageSize + "\n" + packageType + "\n"+ fragile;
    }
}