package com.example.socialdeliverysystem.ui.userParcels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.socialdeliverysystem.Entites.Person;
import com.example.socialdeliverysystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDeliversAdapter extends BaseAdapter {
    ArrayList<Person> delivers;
    private static LayoutInflater inflater = null;
    private DatabaseReference mReference;
    private FragmentActivity fragmentActivity;
    private UserParcel userParcel;

    public UserDeliversAdapter(FragmentActivity fragmentActivity, ArrayList<Person> delivers, UserParcel userParcel) {
        this.delivers = delivers;
        this.userParcel = userParcel;
        this.fragmentActivity = fragmentActivity;
        inflater = (LayoutInflater) fragmentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return delivers.size();
    }

    @Override
    public Person getItem(int i) {
        return delivers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, final View convertView, ViewGroup viewGroup) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.user_deliver_item_list, null) : itemView;
        TextView deliverNameTV = (TextView) itemView.findViewById(R.id.deliver_name);
        TextView deliverPhoneNumberTV = (TextView) itemView.findViewById(R.id.deliver_phone_number);
        TextView deliverAddressTV = (TextView) itemView.findViewById(R.id.deliver_address);
        final Person person = delivers.get(i);
        final Switch canTake = (Switch) itemView.findViewById((R.id.can_take_switch));
        FirebaseDatabase.getInstance().getReference("packages/newPackages" + '/' + userParcel.getParcel().getAddresseeKey() + '/' +
                userParcel.getParcelID() + "/delivers" + '/' + person.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                canTake.setChecked(dataSnapshot.getValue().equals("accepted"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        canTake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FirebaseDatabase.getInstance().getReference("packages/newPackages" + '/' + userParcel.getParcel().getAddresseeKey() + '/' +
                        userParcel.getParcelID() + "/delivers" + '/' + person.getPhoneNumber()).setValue(b ? "accepted" : "applied");
            }
        });
        deliverNameTV.setText(person.getFirstName() + " " + person.getLastName());
        deliverPhoneNumberTV.setText(person.getPhoneNumber());
        deliverAddressTV.setText(person.getAddress());
        return itemView;
    }
}