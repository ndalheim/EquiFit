package com.example.pferdeapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pferdeapp.Activities.AddHorseActivity;
import com.example.pferdeapp.Activities.LoginActivity;
import com.example.pferdeapp.Activities.MainActivity;
import com.example.pferdeapp.Database.Feed;
import com.example.pferdeapp.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FeedFragment extends Fragment {
    private static final String TAG = "FeedFragment";

    private Button mAddHorseBtn;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        mAddHorseBtn = (Button) rootView.findViewById(R.id.add_horse_button);
        goToAddHorseActivity();

        return rootView;
    }

    /**
     * Logout-Button OnClickListener:
     * If user click on the logout button he will be logout.
     */
    private void goToAddHorseActivity() {
        mAddHorseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Du bist in der AddHorse Activity!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "goToAddHorseActivity: success");
                startActivity(new Intent(getActivity(), AddHorseActivity.class));
            }

        });
    }
}

