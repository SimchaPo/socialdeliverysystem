package com.example.socialdeliverysystem.ui.friendsParcels;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.socialdeliverysystem.Entites.Parcel;
import com.example.socialdeliverysystem.R;

import java.util.ArrayList;


public class FriendsAdapter extends BaseAdapter {
    Activity contex;
    ArrayList<FriendsParcel> parcels;
    private static LayoutInflater inflater = null;

    public FriendsAdapter(Activity contex, ArrayList<FriendsParcel> parcels){
        this.contex = contex;
        this.parcels = parcels;
        inflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.friends_parcels_list_item, null) : itemView;
        TextView parcelIDTV = (TextView) itemView.findViewById(R.id.parcel_id);
        TextView addresseeNameTV = (TextView) itemView.findViewById(R.id.addressee_name);
        TextView storageLocationTV = (TextView) itemView.findViewById(R.id.storage_location);
        TextView addresseeAddressTV = (TextView) itemView.findViewById(R.id.addressee_address);
        TextView dist = (TextView) itemView.findViewById(R.id.distance);
        Switch canTakeSwitch = (Switch) itemView.findViewById(R.id.can_take_switch);
        FriendsParcel parcel = parcels.get(i);
        storageLocationTV.setText(parcel.getParcel().getStorage());
        addresseeAddressTV.setText(parcel.getAddresseeAddress());
        addresseeNameTV.setText(parcel.getAddresseeName());
        return itemView;
    }
}
