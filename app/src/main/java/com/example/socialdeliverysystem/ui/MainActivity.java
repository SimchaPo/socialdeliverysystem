package com.example.socialdeliverysystem.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialdeliverysystem.BroadcastService;
import com.example.socialdeliverysystem.Entites.Person;
import com.example.socialdeliverysystem.MyBroadcastReceiver;
import com.example.socialdeliverysystem.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Person user;
    Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        user = (Person) i.getSerializableExtra("user");
        serviceIntent = new Intent(MainActivity.this, BroadcastService.class);
        serviceIntent.putExtra("user", user);
        startService(serviceIntent);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_registered_parcels, R.id.nav_friends_parcels, R.id.nav_parcels_history)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View hView = navigationView.getHeaderView(0);
        TextView userNameTextView = (TextView) hView.findViewById(R.id.userName);
        TextView userEmailTextView = (TextView) hView.findViewById(R.id.userEmail);
        userNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        userEmailTextView.setText(user.getEmail());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "simcha")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("DDB! new package")
                .setContentText("You receive new package")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public Person getUser() {
        return user;
    }

    public void log_out(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        this.finishAffinity();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
