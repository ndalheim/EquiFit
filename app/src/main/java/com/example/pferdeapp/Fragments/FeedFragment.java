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

import com.example.pferdeapp.Activities.AddFeedActivity;
import com.example.pferdeapp.Activities.AddHorseFeedActivity;
import com.example.pferdeapp.Activities.FeedInformationActivity;
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


public class FeedFragment extends Fragment {
    private static final String TAG = "FeedFragment";

    private Button mAddFeedBtn;
    ListView feedListView;
    private List<String> feedList = new ArrayList<>();
    FirebaseUser user;
    String uid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        feedListView = (ListView) rootView.findViewById(R.id.feed_list_view);

        mAddFeedBtn = (Button) rootView.findViewById(R.id.add_feed_button);
        goToAddFeedActivity();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();



        db.collection("Feed").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                feedList.clear();

                for (DocumentSnapshot snapshot : documentSnapshot){
                        feedList.add(snapshot.getString("name") + "_" + snapshot.get("brand"));

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_selectable_list_item, feedList);
                adapter.notifyDataSetChanged();
                feedListView.setAdapter(adapter);
            }
        });

        //
        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getActivity(), FeedInformationActivity.class);
                intent.putExtra("DocumentName", feedList.get(position));
                startActivity(intent);

            }
        });

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

