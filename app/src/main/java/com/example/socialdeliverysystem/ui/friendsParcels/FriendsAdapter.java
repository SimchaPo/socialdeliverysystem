//Ari Rubin 315528547 & Simcha Podolsky 311215149
//Simcha Podolsky 311215149 & Ari Rubin 315528547
package com.example.socialdeliverysystem.ui.friendsParcels;

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

import com.example.socialdeliverysystem.R;
import com.example.socialdeliverysystem.Utils.FirebaseDBManager;
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
    private View itemView;
    private TextView parcelIDTV;
    private TextView addresseeNameTV;
    private TextView storageLocationTV;
    private TextView addresseeAddressTV;
    private TextView dist;
    private Button takeBtn;
    private Switch canTakeSwitch;
    private FriendsParcel parcel;

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
        itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.friends_parcels_list_item, null) : itemView;
        findView();
        parcel = parcels.get(i);
        mReference = FirebaseDBManager.newPackagesRef.child(parcel.getAddressee().getPhoneNumber() + '/' +
                parcel.getParcelID() + "/delivers" + '/' + FirebaseDBManager.getCurrentUserPerson().getPhoneNumber()).getRef();
        mReferanceListener(takeBtn, canTakeSwitch);
        switchListener(canTakeSwitch, parcel);
        takeBtnListener(takeBtn, parcel);
        parcelIDTV.setText(getBuilder("Parcel ID: ", parcel.getParcelID()), TextView.BufferType.SPANNABLE);
        storageLocationTV.setText(getBuilder("Storage Location:\n", parcel.getParcel().getStorage()), TextView.BufferType.SPANNABLE);
        addresseeAddressTV.setText(getBuilder("Addressee Address:\n", parcel.getAddresseeAddress()), TextView.BufferType.SPANNABLE);
        addresseeNameTV.setText(getBuilder("Addressee Name: ", parcel.getAddresseeName()), TextView.BufferType.SPANNABLE);
        dist.setText(getBuilder("Distance: ", String.format("%.2f", parcel.getDistance()) + " KM"), TextView.BufferType.SPANNABLE);
        return itemView;
    }

    private void findView() {
        parcelIDTV = (TextView) itemView.findViewById(R.id.parcel_id);
        addresseeNameTV = (TextView) itemView.findViewById(R.id.addressee_name);
        storageLocationTV = (TextView) itemView.findViewById(R.id.storage_location);
        addresseeAddressTV = (TextView) itemView.findViewById(R.id.addressee_address);
        dist = (TextView) itemView.findViewById(R.id.distance);
        takeBtn = (Button) itemView.findViewById(R.id.take_pkg_btn);
        canTakeSwitch = (Switch) itemView.findViewById(R.id.can_take_switch);
    }

    private void mReferanceListener(final Button takeBtn, final Switch canTakeSwitch) {
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
    }

    private void takeBtnListener(final Button takeBtn, final FriendsParcel parcel) {
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeBtn.setEnabled(false);
                takeBtn.setText("Taken");
                FirebaseDBManager.newPackagesRef.child(parcel.getAddressee().getPhoneNumber() + '/' +
                        parcel.getParcelID()).removeValue();
                mReference = FirebaseDBManager.oldPackagesRef.child(parcel.getAddressee().getPhoneNumber() + '/' +
                        parcel.getParcelID()).getRef();
                mReference.setValue(parcel.getParcel());
                mReference.child("deliver").setValue(FirebaseDBManager.getCurrentUserPerson());
                mReference.child("parcelID").setValue(parcel.getParcelID());
                mReference.child("date").setValue(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
            }
        });
    }

    private void switchListener(Switch canTakeSwitch, final FriendsParcel parcel) {
        canTakeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mReference = FirebaseDBManager.newPackagesRef.child(parcel.getAddressee().getPhoneNumber() + '/' +
                        parcel.getParcelID() + "/delivers" + '/' + FirebaseDBManager.getCurrentUserPerson().getPhoneNumber()).getRef();
                if (b) {
                    mReference.setValue("applied");
                } else {
                    mReference.removeValue();
                }

            }
        });
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
