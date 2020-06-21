package com.example.socialdeliverysystem.ui.userParcels;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import com.example.socialdeliverysystem.Utils.FirebaseDBManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDeliversAdapter extends BaseAdapter {
    ArrayList<Person> delivers;
    private static LayoutInflater inflater = null;
    private UserParcel userParcel;
    private TextView deliverNameTV;
    private TextView deliverPhoneNumberTV;
    private TextView deliverAddressTV;
    private Person person;
    private Switch canTake;

    public UserDeliversAdapter(FragmentActivity fragmentActivity, ArrayList<Person> delivers, UserParcel userParcel) {
        this.delivers = delivers;
        this.userParcel = userParcel;
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
        findView(itemView);
        person = delivers.get(i);
        setFirebaseListener();
        canTake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FirebaseDBManager.newPackagesRef.child(userParcel.getParcel().getAddresseeKey() + '/' +
                        userParcel.getParcelID() + "/delivers" + '/' + person.getPhoneNumber()).setValue(b ? "accepted" : "applied");
            }
        });
        deliverNameTV.setText(getBuilder("Name: ", person.getFirstName() + " " + person.getLastName()), TextView.BufferType.SPANNABLE);
        deliverPhoneNumberTV.setText(getBuilder("Phone Number: ", person.getPhoneNumber()), TextView.BufferType.SPANNABLE);
        deliverAddressTV.setText(getBuilder("Address:\n", person.getAddress()), TextView.BufferType.SPANNABLE);
        return itemView;
    }

    private void setFirebaseListener() {
        FirebaseDBManager.newPackagesRef.child(userParcel.getParcel().getAddresseeKey() + '/' +
                userParcel.getParcelID() + "/delivers" + '/' + person.getPhoneNumber()).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                canTake.setChecked(dataSnapshot.getValue().equals("accepted"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void findView(View itemView) {
        deliverNameTV = (TextView) itemView.findViewById(R.id.deliver_name);
        deliverPhoneNumberTV = (TextView) itemView.findViewById(R.id.deliver_phone_number);
        deliverAddressTV = (TextView) itemView.findViewById(R.id.deliver_address);
        canTake = (Switch) itemView.findViewById((R.id.can_take_switch));
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
