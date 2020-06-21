package com.example.socialdeliverysystem.ui.userParcels;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialdeliverysystem.Entites.Parcel;
import com.example.socialdeliverysystem.Entites.Person;
import com.example.socialdeliverysystem.R;
import com.example.socialdeliverysystem.Utils.FirebaseDBManager;
import com.example.socialdeliverysystem.ui.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisteredParcelsFragment extends Fragment {

    //private Person user;
    private ArrayList<UserParcel> parcelArrayList = new ArrayList<>();
    private UserParcelAdapter parcelArrayAdapter;
    private DatabaseReference mReference;
    private ListView parcelListView;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_user_parcels, container, false);
        mReference = FirebaseDBManager.newPackagesRef.child(FirebaseDBManager.getCurrentUserPerson().getPhoneNumber());
        parcelArrayAdapter = new UserParcelAdapter(getActivity(), parcelArrayList);
        parcelListView = (ListView) root.findViewById(R.id.parcelListView);
        parcelArrayList.clear();
        parcelListView.setAdapter(parcelArrayAdapter);
        setFirebaseListener();
        return root;
    }

    private void setFirebaseListener() {
        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                  parcelArrayList.add(new UserParcel(dataSnapshot.getKey(), dataSnapshot.getValue(Parcel.class)));
                  parcelArrayAdapter.notifyDataSetChanged();
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
