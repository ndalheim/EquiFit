package com.example.pferdeapp.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pferdeapp.Fragments.CalendarFragment;
import com.example.pferdeapp.Fragments.FeedFragment;
import com.example.pferdeapp.Fragments.HorseFragment;
import com.example.pferdeapp.Fragments.ProfilFragment;
import com.example.pferdeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

//import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private Fragment horseFragment;
    private Fragment feedFragment;
    private Fragment calendarFragment;
    private Fragment profileFragment;


    private BottomNavigationView btm_nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // BottomNavigationView
        btm_nav_view = findViewById(R.id.bottomNavigationView);

        horseFragment = new HorseFragment();
        feedFragment = new FeedFragment();
        calendarFragment = new CalendarFragment();
        profileFragment = new ProfilFragment();

        setFragment(horseFragment);

        navigationListener();
    }


    /**
     * Listener to navigate throw the bottomNavigationView.
     */
    private void navigationListener() {
        btm_nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_horse:
                        setFragment(horseFragment);
                        return true;
                    case R.id.navigation_feed:
                        setFragment(feedFragment);
                        return true;
                    case R.id.navigation_calendar:
                        setFragment(calendarFragment);
                        return true;
                    case R.id.navigation_profil:
                        setFragment(profileFragment);
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
    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();

    }


}