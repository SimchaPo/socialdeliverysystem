//Ari Rubin 315528547 & Simcha Podolsky 311215149
//Simcha Podolsky 311215149 & Ari Rubin 315528547
package com.example.socialdeliverysystem;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.socialdeliverysystem.Utils.FirebaseDBManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.jetbrains.annotations.NotNull;

public class MyBroadcastService extends Service {

    private ChildEventListener listener;
    private String usersKey;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BroadcastReceiver mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("new_package_service");
        this.registerReceiver(mReceiver, filter);
        setFirebaseListener();
        return START_REDELIVER_INTENT;
    }

    private void setFirebaseListener() {
        usersKey = FirebaseDBManager.getCurrentUserPerson().getPhoneNumber();
        FirebaseDBManager.newPackagesRef.child(usersKey).
                addChildEventListener(newPackagesEventListener());
    }

    @NotNull
    private ChildEventListener newPackagesEventListener() {
        if(listener == null) {
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (!dataSnapshot.child("notified").exists() && FirebaseDBManager.getCurrentUser() != null
                            && (dataSnapshot.child("addresseeKey").getValue()).equals(FirebaseDBManager.getCurrentUserPerson().getPhoneNumber())) {
                        dataSnapshot.child("notified").getRef().setValue(true);
                        Intent i = new Intent("new_package_service");
                        i.putExtra("parcelId", dataSnapshot.getKey());
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
            };
        }
        return listener;
    }

    @Override
    public void onDestroy() {
        FirebaseDBManager.newPackagesRef.child(usersKey).
                removeEventListener(newPackagesEventListener());
        stopSelf();
        super.onDestroy();
    }
}
