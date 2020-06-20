package com.example.socialdeliverysystem;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.socialdeliverysystem.Entites.Person;
import com.example.socialdeliverysystem.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;


public class BroadcastService extends IntentService {

    public BroadcastService() {
        super("BroadcastService");
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final Person user = (Person) intent.getSerializableExtra("user");
        BroadcastReceiver mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("new_package_service");
        this.registerReceiver(mReceiver, filter);
        FirebaseDatabase.getInstance().getReference("packages/newPackages").child(user.getPhoneNumber()).
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (FirebaseAuth.getInstance().getCurrentUser() != null &&
                                FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(user.getEmail())) {
                            Intent i = new Intent("new_package_service");
                            sendBroadcast(i);

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
