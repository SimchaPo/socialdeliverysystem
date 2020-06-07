package com.example.socialdeliverysystem.ui.friendsParcels;

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
import com.example.socialdeliverysystem.Utils.LocationManage;
import com.example.socialdeliverysystem.ui.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FriendsParcelsFragment extends Fragment {

    private Person user;
    private TextView textView;
    private ArrayList<String> parcelArrayList = new ArrayList<>();
    private ArrayAdapter<String> parcelArrayAdapter;
    private DatabaseReference mReference;
    private ListView parcelListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends_parcels, container, false);
        user = ((MainActivity) getActivity()).getUser();
        textView = root.findViewById(R.id.text_gallery);
        textView.setText(((MainActivity) getActivity()).getUser().getFirstName());
        mReference = FirebaseDatabase.getInstance().getReference().child("packages").child("newPackages");
        parcelArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, parcelArrayList);
        parcelListView = (ListView) root.findViewById(R.id.parcelListView);
        parcelListView.setAdapter(parcelArrayAdapter);
        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot ds1, @Nullable String s) {
                if (!ds1.getKey().equals(user.getPhoneNumber())) {
                    FirebaseDatabase.getInstance().getReference().child("users").child(ds1.getKey()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot ds2, @Nullable String s) {
                            if (ds2.getKey().equals("address")) {
                                String address = ds2.getValue().toString();
                                float distance = LocationManage.getDistanceBetweenTwoLocations(getContext(), user.getAddress(), address);
                                for (DataSnapshot ds : ds1.getChildren()) {
                                    parcelArrayList.add(distance + " km\n" + ds.getKey() + "\n" + ds.getValue(Parcel.class).toString() + "\n");
                                    Collections.sort(parcelArrayList, new Comparator<String>() {
                                        public int compare(String a, String b) {
                                            Float n1 = Float.parseFloat(a.split(" ")[0]);
                                            Float n2 = Float.parseFloat(b.split(" ")[0]);
                                            return Float.compare(n1, n2);
                                        }
                                    });
                                    parcelArrayAdapter.notifyDataSetChanged();
                                }
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
