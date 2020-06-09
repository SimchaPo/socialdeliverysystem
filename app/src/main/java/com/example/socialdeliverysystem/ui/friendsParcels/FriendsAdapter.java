package com.example.socialdeliverysystem.ui.friendsParcels;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.socialdeliverysystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FriendsAdapter extends BaseAdapter {
    ArrayList<FriendsParcel> parcels;
    private static LayoutInflater inflater = null;
    private DatabaseReference mReference;

    public FriendsAdapter(Activity context, ArrayList<FriendsParcel> parcels) {
        this.parcels = parcels;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return parcels.size();
    }

    @Override
    public FriendsParcel getItem(int i) {
        return parcels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.friends_parcels_list_item, null) : itemView;
        TextView parcelIDTV = (TextView) itemView.findViewById(R.id.parcel_id);
        TextView addresseeNameTV = (TextView) itemView.findViewById(R.id.addressee_name);
        TextView storageLocationTV = (TextView) itemView.findViewById(R.id.storage_location);
        TextView addresseeAddressTV = (TextView) itemView.findViewById(R.id.addressee_address);
        TextView dist = (TextView) itemView.findViewById(R.id.distance);
        final Button takeBtn = (Button) itemView.findViewById(R.id.take_pkg_btn);
        final Switch canTakeSwitch = (Switch) itemView.findViewById(R.id.can_take_switch);
        final FriendsParcel parcel = parcels.get(i);
        mReference = FirebaseDatabase.getInstance().getReference("packages/newPackages" + '/' + parcel.getAddressee().getPhoneNumber() + '/' +
                parcel.getParcelID() + "/delivers" + '/' + parcel.getUser().getPhoneNumber());
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (dataSnapshot.getValue().equals("applied")) {
                        canTakeSwitch.setVisibility(View.VISIBLE);
                        takeBtn.setVisibility(View.GONE);
                        canTakeSwitch.setChecked(true);
                    } else if (dataSnapshot.getValue().equals("accepted")) {
                        takeBtn.setVisibility(View.VISIBLE);
                        canTakeSwitch.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        canTakeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mReference = FirebaseDatabase.getInstance().getReference("packages/newPackages" + '/' + parcel.getAddressee().getPhoneNumber() + '/' +
                        parcel.getParcelID() + "/delivers" + '/' + parcel.getUser().getPhoneNumber());
                if (b) {
                    mReference.setValue("applied");
                } else {
                    mReference.removeValue();
                }

            }
        });
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("packages/newPackages" + '/' + parcel.getAddressee().getPhoneNumber() + '/' +
                        parcel.getParcelID()).removeValue();
                mReference = FirebaseDatabase.getInstance().getReference("packages/oldPackages" + '/' + parcel.getAddressee().getPhoneNumber() + '/' +
                        parcel.getParcelID());
                mReference.setValue(parcel.getParcel());
                mReference.child("deliver").setValue(parcel.getUser());
                mReference.child("parcelID").setValue(parcel.getParcelID());
                mReference.child("date").setValue(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
            }
        });
        parcelIDTV.setText((parcel.getParcelID()));
        storageLocationTV.setText(parcel.getParcel().getStorage());
        addresseeAddressTV.setText(parcel.getAddresseeAddress());
        addresseeNameTV.setText(parcel.getAddresseeName());
        dist.setText(String.valueOf(parcel.getDistance()));
        return itemView;
    }
}
