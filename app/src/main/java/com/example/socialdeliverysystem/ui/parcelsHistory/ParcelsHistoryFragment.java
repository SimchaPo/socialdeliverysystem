package com.example.socialdeliverysystem.ui.parcelsHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.socialdeliverysystem.Entites.Parcel;
import com.example.socialdeliverysystem.Entites.Person;
import com.example.socialdeliverysystem.R;
import com.example.socialdeliverysystem.Utils.FirebaseDBManager;
import com.example.socialdeliverysystem.ui.MainActivity;
import com.example.socialdeliverysystem.ui.userParcels.UserParcel;
import com.example.socialdeliverysystem.ui.userParcels.UserParcelAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ParcelsHistoryFragment extends Fragment {

    private ArrayList<ParcelHistory> parcelArrayList = new ArrayList<>();
    private ParcelHistoryAdapter parcelArrayAdapter;
    private ListView parcelListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_parcels_history, container, false);
        parcelArrayAdapter = new ParcelHistoryAdapter(getActivity(), parcelArrayList);
        parcelListView = (ListView) root.findViewById(R.id.list_view);
        parcelArrayList.clear();
        parcelListView.setAdapter(parcelArrayAdapter);
        setFurebaseListener();
        return root;
    }

    private void setFurebaseListener() {
        FirebaseDBManager.oldPackagesRef.child(FirebaseDBManager.getCurrentUserPerson().getPhoneNumber()).getRef()
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                parcelArrayList.add(dataSnapshot.getValue(ParcelHistory.class));
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
