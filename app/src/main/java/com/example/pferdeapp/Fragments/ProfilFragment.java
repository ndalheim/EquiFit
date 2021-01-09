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

import com.example.pferdeapp.Activities.LoginActivity;
import com.example.pferdeapp.Activities.MainActivity;
import com.example.pferdeapp.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilFragment extends Fragment {
    private static final String TAG = "ProfilFragment";

    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private Button logoutBtn;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();

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
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        logoutBtn = (Button) rootView.findViewById(R.id.logout_button);
        setLogoutUserListener();

        return rootView;
    }

    /**
     * Logout-Button OnClickListener:
     * If user click on the logout button he will be logout.
     */
    private void setLogoutUserListener() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Du hast Dich erfolgreich abgemeldet!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "logoutUser: success");
                startActivity(new Intent(getActivity(), LoginActivity.class));
                fAuth.signOut();
            }

        });
    }
}
