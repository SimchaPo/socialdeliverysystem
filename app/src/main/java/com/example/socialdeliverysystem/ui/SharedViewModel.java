package com.example.socialdeliverysystem.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialdeliverysystem.Entites.Person;

public class SharedViewModel extends ViewModel {
    public MutableLiveData<Person> user;

    public SharedViewModel(){
        user = new MutableLiveData<>();;
    }
    public void setUser(Person user) {
        this.user.setValue(user);
    }

    //public MutableLiveData<Person> getUser() {
    //    return user;
    //}

    public Person getUser() {
        return user.getValue();
    }
}
