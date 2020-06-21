package com.example.socialdeliverysystem.ui.parcelsHistory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
    private TextView parcelIDTV;
    private TextView deliverNameTV;
    private TextView deliverPhoneNumberTV;
    private TextView dateTV;
    private ParcelHistory parcel;

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
        findView(itemView);
        parcel = parcels.get(i);
        parcelIDTV.setText(getBuilder("Parcel ID: ", parcel.getParcelID()), TextView.BufferType.SPANNABLE);
        deliverNameTV.setText(getBuilder("Deliver: ", parcel.getDeliver().getFirstName() + " " + parcel.getDeliver().getLastName()), TextView.BufferType.SPANNABLE);
        deliverPhoneNumberTV.setText(getBuilder("Deliver Phone Number: ", parcel.getDeliver().getPhoneNumber()), TextView.BufferType.SPANNABLE);
        dateTV.setText(getBuilder("Delivery Date: ", parcel.getDate()), TextView.BufferType.SPANNABLE);
        return itemView;
    }

    private void findView(View itemView) {
        parcelIDTV = (TextView) itemView.findViewById(R.id.parcel_id);
        deliverNameTV = (TextView) itemView.findViewById(R.id.deliver_name);
        deliverPhoneNumberTV = (TextView) itemView.findViewById(R.id.deliver_phone_number);
        dateTV = (TextView) itemView.findViewById(R.id.date);
    }


    private SpannableStringBuilder getBuilder(String s1, String s2) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str1 = new SpannableString(s1);
        str1.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, str1.length(), 0);
        builder.append(str1);
        SpannableString str2 = new SpannableString(s2);
        str2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, str2.length(), 0);
        builder.append(str2);
        return builder;
    }
}
