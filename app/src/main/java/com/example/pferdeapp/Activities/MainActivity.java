package com.example.pferdeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pferdeapp.Fragments.CalendarFragment;
import com.example.pferdeapp.Fragments.FeedFragment;
import com.example.pferdeapp.Fragments.ProfilFragment;
import com.example.pferdeapp.ImageToText;
import com.example.pferdeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

//import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private Fragment feedFragment;
    private Fragment calendarFragment;
    private Fragment profilFragment;

    private BottomNavigationView btm_nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // BottomNavigationView
        btm_nav_view = findViewById(R.id.bottomNavigationView);

        feedFragment = new FeedFragment();
        calendarFragment = new CalendarFragment();
        profilFragment = new ProfilFragment();

        navigationListener();
    }


    /**
     * Listener to navigate throw the bottomNavigationView.
     */
    private void navigationListener() {
        btm_nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setFragment(feedFragment);

                switch (item.getItemId()) {
                    case R.id.navigation_feed:
                        setFragment(feedFragment);
                        // Toast.makeText(MainActivity.this, "Futter", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_calendar:
                        setFragment(calendarFragment);
                        // Toast.makeText(MainActivity.this, "Kalender", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_profil:
                        setFragment(profilFragment);
                        // Toast.makeText(MainActivity.this, "Profil", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }

            }
        });
    }

    /**
     * Help-method to open the selected fragment in the MainActivity
     */
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();

    }


    public void addFood(View v) {
        startActivity(new Intent(getApplicationContext(), ImageToText.class));
        finish();
    }

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

}