package com.example.socialdeliverysystem.ui.parcelsHistory;

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
import androidx.annotation.Nullable;

import com.example.socialdeliverysystem.R;
import com.example.socialdeliverysystem.ui.friendsParcels.FriendsParcel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ParcelHistoryAdapter extends BaseAdapter {
    ArrayList<ParcelHistory> parcels;
    private static LayoutInflater inflater = null;
    private DatabaseReference mReference;

    public ParcelHistoryAdapter(Activity context, ArrayList<ParcelHistory> parcels) {
        this.parcels = parcels;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return parcels.size();
    }

    @Override
    public ParcelHistory getItem(int i) {
        return parcels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.parcel_history_list_item, null) : itemView;
        TextView parcelIDTV = (TextView) itemView.findViewById(R.id.parcel_id);
        TextView deliverNameTV = (TextView) itemView.findViewById(R.id.deliver_name);
        TextView deliverPhoneNumberTV = (TextView) itemView.findViewById(R.id.deliver_phone_number);
        TextView dateTV = (TextView) itemView.findViewById(R.id.date);
        final ParcelHistory parcel = parcels.get(i);
        parcelIDTV.setText((parcel.getParcelID()));
        deliverNameTV.setText(parcel.getDeliver().getFirstName() + " " + parcel.getDeliver().getLastName());
        deliverPhoneNumberTV.setText(parcel.getDeliver().getPhoneNumber());
        dateTV.setText(parcel.getDate());
        return itemView;
    }
}
