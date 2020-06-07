package com.example.socialdeliverysystem.ui.userParcels;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisteredParcelsFragment extends Fragment {

    private static final String USER_KEY = "user";
    private Person user;
    private ArrayList<String> parcelArrayList = new ArrayList<>();
    private ArrayAdapter<String> parcelArrayAdapter;
    private DatabaseReference mReference;
    private ListView parcelListView;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = root.findViewById(R.id.text_home);
        user = (Person) getActivity().getIntent().getSerializableExtra(USER_KEY);
        textView.setText(user.toString());
        mReference = FirebaseDatabase.getInstance().getReference().child("packages").child("newPackages").child(user.getPhoneNumber());
        parcelArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, parcelArrayList);
        parcelListView = (ListView) root.findViewById(R.id.parcelListView);
        parcelListView.setAdapter(parcelArrayAdapter);
        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                  parcelArrayList.add(dataSnapshot.getKey() + "\n" + dataSnapshot.getValue(Parcel.class).toString());
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
        return root;
    }
}
