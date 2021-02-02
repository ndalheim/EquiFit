package com.example.pferdeapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;

import com.example.pferdeapp.Activities.AddHorseActivity;
import com.example.pferdeapp.Activities.ShowHorseInformationActivity;
import com.example.pferdeapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HorseFragment extends Fragment {
    private static final String TAG = "FeedFragment";

    private Button mAddHorseBtn;
    ListView listView;
    private List<String> horseList = new ArrayList<>();
    FirebaseUser user;
    String uid, horseNameAndUid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public HorseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_horse, container, false);

        listView = (ListView) rootView.findViewById(R.id.horse_list_view);

        mAddHorseBtn = (Button) rootView.findViewById(R.id.add_horse_button);
        goToAddHorseActivity();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        showHorses(uid);

        // geht beim Klicken auf ein ListenItem (Pferd) zu der ShowHorseInformationActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getActivity(), ShowHorseInformationActivity.class);
                horseNameAndUid = horseList.get(position) + "_" + uid;

                // Übergibt der nächsten Activity Informationen
                intent.putExtra("HorseId", horseNameAndUid);
                intent.putExtra("HorseName", horseList.get(position));
                intent.putExtra("UserId", uid);
                startActivity(intent);

            }
        });

        return rootView;
    }

    private void showHorses(final String uID) {
        db.collection("user").document(uid).collection("Horse")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        horseList.clear();
                        // Schleife die alle Pferde des Users der Liste hinzufügt (die Pferdenamen)
                        for (DocumentSnapshot snapshot : documentSnapshot){
                            if(uID.equals(snapshot.getString("uid"))){
                                horseList.add(snapshot.getString("horseName"));
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                                .getApplicationContext(),
                                android.R.layout.simple_selectable_list_item, horseList);
                        adapter.notifyDataSetChanged();
                        // Liste der Pfertde wird angezeigt
                        listView.setAdapter(adapter);
                    }
                });
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

