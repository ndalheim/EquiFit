package com.example.pferdeapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.pferdeapp.Activities.AddFeedActivity;
import com.example.pferdeapp.Activities.AddHorseActivity;
import com.example.pferdeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";


    private FirebaseAuth fAuth;
    private FirebaseUser user;

    private Button mAddFeedBtn;

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

        mAddFeedBtn = (Button) rootView.findViewById(R.id.add_feed_button);
        goToAddFeedActivity();

        return rootView;
    }

    /**
     * Logout-Button OnClickListener:
     * If user click on the logout button he will be logout.
     */
    private void goToAddFeedActivity() {
        mAddFeedBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Du bist in der AddHorse Activity!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "goToAddHorseActivity: success");
                startActivity(new Intent(getActivity(), AddFeedActivity.class));
            }

        });
    }
}
