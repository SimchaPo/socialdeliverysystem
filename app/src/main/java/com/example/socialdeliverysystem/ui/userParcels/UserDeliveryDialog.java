package com.example.socialdeliverysystem.ui.userParcels;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.socialdeliverysystem.Entites.Parcel;
import com.example.socialdeliverysystem.Entites.Person;
import com.example.socialdeliverysystem.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDeliveryDialog extends AppCompatDialogFragment {
    private ListView listView;
    private ArrayList<Person> deliversArrayList = new ArrayList<>();
    private UserDeliversAdapter deliversAdapter;
    private DatabaseReference mReference;
    UserParcel userParcel;
    public UserDeliveryDialog(UserParcel userParcel){
        this.userParcel = userParcel;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.user_delivery_dialog, null);
        builder.setView(view).setTitle("Optional Delivers").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        deliversAdapter = new UserDeliversAdapter(getActivity(), deliversArrayList, userParcel);
        mReference = FirebaseDatabase.getInstance().getReference().child("packages/newPackages").child(userParcel.getParcel().getAddresseeKey()).child(userParcel.getParcelID()).child("delivers");
        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds1, @Nullable String s) {
                if(ds1.getValue(String.class).equals("applied") || ds1.getValue(String.class).equals("accepted")) {
                    FirebaseDatabase.getInstance().getReference().child("users").child(ds1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds2) {
                            deliversArrayList.add(ds2.getValue(Person.class));
                            deliversAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
        listView = (ListView) view.findViewById(R.id.delivers_list_view);
        listView.setAdapter(deliversAdapter);
        return builder.create();
    }
}
