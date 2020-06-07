package com.example.socialdeliverysystem.ui.userParcels;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.socialdeliverysystem.R;

import java.util.ArrayList;

public class ParcelHandlerActivity extends AppCompatActivity {
    private ArrayList<String> arrayList = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_handler);
        listView = findViewById(R.id.parcelListView);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.txt, arrayList);
        listView.setAdapter(arrayAdapter);
    }
}
