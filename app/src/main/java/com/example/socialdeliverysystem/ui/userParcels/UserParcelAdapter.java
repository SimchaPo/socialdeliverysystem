package com.example.socialdeliverysystem.ui.userParcels;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.socialdeliverysystem.Entites.Parcel;
import com.example.socialdeliverysystem.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class UserParcelAdapter extends BaseAdapter {
    ArrayList<UserParcel> parcels;
    private static LayoutInflater inflater = null;
    private FragmentActivity fragmentActivity;
    private UserParcel parcel;
    private TextView parcelIDTV;
    private TextView storageLocationTV;
    private TextView packageTypeTV;
    private TextView packageSizeTV;
    private TextView packageFragileTV;

    public UserParcelAdapter(FragmentActivity fragmentActivity, ArrayList<UserParcel> parcels) {
        this.parcels = parcels;
        this.fragmentActivity = fragmentActivity;
        inflater = (LayoutInflater) fragmentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public UserParcel getParcel() {
        return parcel;
    }

    @Override
    public int getCount() {
        return parcels.size();
    }

    @Override
    public UserParcel getItem(int i) {
        return parcels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, final View convertView, ViewGroup viewGroup) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.user_fragment_list_item, null) : itemView;
        findView(itemView);
        parcel = parcels.get(i);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(parcels.get(i));
            }
        });
        parcelIDTV.setText(getBuilder("Parcel ID: ", parcel.getParcelID()), TextView.BufferType.SPANNABLE);
        storageLocationTV.setText(getBuilder("Storage Location:\n", parcel.getParcel().getStorage()), TextView.BufferType.SPANNABLE);
        packageTypeTV.setText(getBuilder("Type: ", parcel.getParcel().getPackageType()), TextView.BufferType.SPANNABLE);
        packageSizeTV.setText(getBuilder("Size: ", parcel.getParcel().getPackageSize() + " KG   "), TextView.BufferType.SPANNABLE);
        packageFragileTV.setText(getBuilder("Fragility: ", parcel.getParcel().isFragile()), TextView.BufferType.SPANNABLE);
        return itemView;
    }

    private void findView(View itemView) {
        parcelIDTV = (TextView) itemView.findViewById(R.id.parcel_id);
        storageLocationTV = (TextView) itemView.findViewById(R.id.storage_location);
        packageTypeTV = (TextView) itemView.findViewById(R.id.parcel_type);
        packageSizeTV = (TextView) itemView.findViewById(R.id.parcel_weight);
        packageFragileTV = (TextView) itemView.findViewById(R.id.parcel_is_fragile);
    }

    private void openDialog(UserParcel userParcel) {
        UserDeliveryDialog dialog = new UserDeliveryDialog(userParcel);
        dialog.show(fragmentActivity.getSupportFragmentManager(), "delivers dialog");
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
