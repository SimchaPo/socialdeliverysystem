package com.example.socialdeliverysystem.ui.friendsParcels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    private Person addressee;
    private ListView listViewParcels;
    private ArrayList<FriendsParcel> parcels = new ArrayList<>();
    private FriendsAdapter parcelAdapter;
    private DatabaseReference mReference1;
    private DatabaseReference mReference2;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends_parcels, container, false);
        user = ((MainActivity) getActivity()).getUser();
        parcelAdapter = new FriendsAdapter(getActivity(), parcels);
        listViewParcels = (ListView) root.findViewById(R.id.parcelListView);
        listViewParcels.setAdapter(parcelAdapter);
        mReference1 = FirebaseDatabase.getInstance().getReference().child("packages").child("newPackages");
        mReference2 = FirebaseDatabase.getInstance().getReference().child("users");
        mReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot ds1, @Nullable String s) {
                if (!ds1.getKey().equals(user.getPhoneNumber())) {
                    mReference2.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot ds2, @Nullable String s) {
                            if (ds1.getKey().equals(ds2.getKey())) {
                                addressee = ds2.getValue(Person.class);
                                for (DataSnapshot ds : ds1.getChildren()) {
                                    parcels.add(new FriendsParcel(user, addressee, ds.getValue(Parcel.class), ds.getKey(), getContext()));
                                    Collections.sort(parcels, new Comparator<FriendsParcel>() {
                                        public int compare(FriendsParcel a, FriendsParcel b) {
                                            return Float.compare(a.getDistance(), b.getDistance());
                                        }
                                    });
                                    parcelAdapter.notifyDataSetChanged();
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
        listViewParcels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "item clicked: " + i, Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

}
