package com.example.socialdeliverysystem.ui.friendsParcels;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import java.util.ArrayList;


public class FriendsAdapter extends BaseAdapter {
    private Activity context;
    ArrayList<FriendsParcel> parcels;
    private static LayoutInflater inflater = null;
    private DatabaseReference mReference;

    public FriendsAdapter(Activity context, ArrayList<FriendsParcel> parcels) {
        this.context = context;
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
        final Switch canTakeSwitch = (Switch) itemView.findViewById(R.id.can_take_switch);
        final FriendsParcel parcel = parcels.get(i);
        mReference = FirebaseDatabase.getInstance().getReference("packages/newPackages" + '/' + parcel.getAddressee().getPhoneNumber() + '/' +
                parcel.getParcelID() + "/delivers" + '/' + parcel.getUser().getPhoneNumber());
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    canTakeSwitch.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        canTakeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                parcels.get(i);
                mReference = FirebaseDatabase.getInstance().getReference("packages/newPackages" + '/' + parcel.getAddressee().getPhoneNumber() + '/' +
                        parcel.getParcelID() + "/delivers" + '/' + parcel.getUser().getPhoneNumber());
                if (b) {
                    mReference.setValue("applied");
                } else {
                    mReference.removeValue();
                }

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
