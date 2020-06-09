package com.example.socialdeliverysystem.ui.userParcels;

import android.app.Activity;
import android.content.Context;
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
    private DatabaseReference mReference;
    private FragmentActivity fragmentActivity;
    private UserParcel parcel;

    public UserParcelAdapter(FragmentActivity fragmentActivity, ArrayList<UserParcel> parcels) {
        this.parcels = parcels;
        this.fragmentActivity = fragmentActivity;
        inflater = (LayoutInflater) fragmentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public UserParcel getParcel(){
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
        TextView parcelIDTV = (TextView) itemView.findViewById(R.id.parcel_id);
        TextView storageLocationTV = (TextView) itemView.findViewById(R.id.storage_location);
        TextView packageTypeTV = (TextView) itemView.findViewById(R.id.parcel_type);
        TextView packageSizeTV = (TextView) itemView.findViewById(R.id.parcel_weight);
        TextView packageFragileTV = (TextView) itemView.findViewById(R.id.parcel_is_fragile);
        parcel = parcels.get(i);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(parcels.get(i));
            }
        });
        parcelIDTV.setText((parcel.getParcelID()));
        storageLocationTV.setText(parcel.getParcel().getStorage());
        packageTypeTV.setText(parcel.getParcel().getPackageType());
        packageSizeTV.setText(parcel.getParcel().getPackageSize());
        packageFragileTV.setText(String.valueOf(parcel.getParcel().isFragile()));
        return itemView;
    }

    private void openDialog(UserParcel userParcel) {
        UserDeliveryDialog dialog = new UserDeliveryDialog(userParcel);
        dialog.show(fragmentActivity.getSupportFragmentManager(), "delivers dialog");
    }
}
