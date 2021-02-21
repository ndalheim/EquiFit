package com.example.pferdeapp.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pferdeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";


    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private TextView tvPricePerMonth;
    private Double pricePerMonth;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect to Firebase user
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        tvPricePerMonth = (TextView) rootView.findViewById(R.id.pricePerMonthTextView);
        calculateExpenditure();



        return rootView;
    }


    private void calculateExpenditure() {
        final List<Double> kosten = new ArrayList<Double>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        db.collection("user").document(uid).collection("Horse").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                // Läuft über alle Pferde des Users
                for (DocumentSnapshot snapshot1 : value) {
                    if(uid.equals(snapshot1.getString("uid"))){
                    }
                    snapshot1.getReference().collection("FeedPlan").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value2, @Nullable FirebaseFirestoreException error) {
                            // Läuft über den Futterplan des Pferdes
                            for (DocumentSnapshot snapshot2 : value2) {
                                final Double feedInGram = snapshot2.getDouble("feedInGram");
                                final Double numberOfMeals = snapshot2.getDouble("numberOfMeals");
                                final String feedId = snapshot2.getString("feedId");
                                final Double gramPerDay = feedInGram* numberOfMeals;

                                // Sucht die preise von den verfütterten Futtern
                                db.collection("FeedCosts").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value3, @Nullable FirebaseFirestoreException error) {

                                        // Durchsucht die Futter-Datenbank nach dem Futter aus dem Futterplan
                                        for (DocumentSnapshot snapshot3 : value3) {
                                            if(feedId.equals(snapshot3.getId().toString())){
                                                Double amount = snapshot3.getDouble("amount");
                                                Double price = snapshot3.getDouble("price");

                                                Double test = 30 * price / (amount/(gramPerDay/1000));
                                                kosten.add(test);

                                                Double calculatedPrice = 0.0;
                                                for(Double val: kosten){
                                                    calculatedPrice += val;
                                                }
                                                NumberFormat n = NumberFormat.getInstance();
                                                n.setMaximumFractionDigits(2);

                                                tvPricePerMonth.setText(n.format(calculatedPrice));
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });


    }

}
