package com.example.pferdeapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.Database.Feed;
import com.example.pferdeapp.Database.FeedCosts;
import com.example.pferdeapp.Database.FeedPlan;
import com.example.pferdeapp.Database.Horse;
import com.example.pferdeapp.R;
import com.example.pferdeapp.hilfsklassen.AddFeedAdapter;
import com.example.pferdeapp.hilfsklassen.AddFeedListModel;
import com.example.pferdeapp.hilfsklassen.FeedPlanAdapter;
import com.example.pferdeapp.hilfsklassen.FeedPlanListModel;
import com.example.pferdeapp.hilfsklassen.HorseFeedHelpClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddHorseFeedActivity extends AppCompatActivity {

    private static final String TAG = "AddHorseFeedActivity";


    EditText mFeedInGram;
    Spinner mFeed, mNumberOfMeals;
    Button maddFeedWithGrammBtn, mSaveHorseFeedBtn, mBackToMainBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<String> feedItems;
    ArrayAdapter<String> feedItemsAdapter;
    ListView feedPlanListView, mfeedListView;
    String feedRationItem, feedId, horseId, horseName, feedName;
    Map<String, Object> feedRation = new HashMap<>();

    ArrayList<AddFeedListModel> feedPlanList;
    AddFeedAdapter feedPlanAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_horse_feed);


        mfeedListView = findViewById(R.id.feed_list);
        feedPlanList = new ArrayList<>();
        feedPlanAdapter = new AddFeedAdapter(this, R.layout.list_item, feedPlanList);
        mfeedListView.setAdapter(feedPlanAdapter);

        mFeedInGram = findViewById(R.id.feedInGrammValue);

        mNumberOfMeals = (Spinner) findViewById(R.id.numberOfMealsSpinner);
        ArrayAdapter<CharSequence> numberOfMealsAdapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_meals_array, android.R.layout.simple_spinner_item);
        mNumberOfMeals.setAdapter(numberOfMealsAdapter);


        // Speichern und zurück Button
        mSaveHorseFeedBtn = findViewById(R.id.save_feed_plan_button);
        mBackToMainBtn = findViewById(R.id.back_to_horse_information_button);


        db.collection("Feed").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                feedPlanList.clear();
                AddFeedListModel feedPlan;

                for (DocumentSnapshot snapshot : documentSnapshot){
                    if(snapshot.getId() == null){
                        feedPlan = new AddFeedListModel("Füge Futter dem Futterplan hinzu");
                        feedPlanList.add(feedPlan);

                    }else{
                        Log.d(TAG, "onEvent: --------------" + snapshot.getId().toString());
                        String[] splitFeedId = snapshot.getId().toString().split("_");
                        feedPlan = new AddFeedListModel(splitFeedId[1],  splitFeedId[0], snapshot.getId());

                        feedPlanList.add(feedPlan);
                        Log.d(TAG, "_________________" +  feedPlanList.toString());
                    }
                }
                feedPlanAdapter.notifyDataSetChanged();
            }
        });



        mfeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                for (int i = 0; i < mfeedListView.getChildCount(); i++) {
                    if(position == i ){
                        mfeedListView.getChildAt(i).setBackgroundColor(Color.parseColor("#F19817"));
                        feedId = feedPlanList.get(position).getFeedID();
                    }else{
                        mfeedListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });

        //Zurück-Button zur Main Activity
        mBackToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        //Futter-Speichern-Button. Speichert Futter in der Datenbank
        mSaveHorseFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFeedPlanToFirestore();
            }
        });

        if(getIntent().hasExtra("HorseId") == true) {
            horseId = getIntent().getExtras().getString("HorseId");
            horseName = getIntent().getExtras().getString("HorseName");

        }else{
            Toast.makeText(AddHorseFeedActivity.this, "Daten wurden nicht übergeben", Toast.LENGTH_SHORT).show();
        }
    }

    private void addFeedPlanToFirestore() {

        Double numberOfMeals = null;
        Double feedInGram = null;

        if (!(mNumberOfMeals.getSelectedItem().toString().equals("")||mFeedInGram.getText().toString().trim().equals(""))){
            numberOfMeals = Double.parseDouble(mNumberOfMeals.getSelectedItem().toString());
            feedInGram = Double.parseDouble(mFeedInGram.getText().toString().trim());
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uId = null;
        if (user !=null) {
            uId = user.getUid();
        }


        if(feedId == null){
            Toast.makeText(AddHorseFeedActivity.this, "Es wurde kein Futter ausgewählt", Toast.LENGTH_SHORT).show();
        }else if(!(feedId.equals("") || numberOfMeals==null || feedInGram==null)){

            FeedPlan feedPlan = new FeedPlan(feedId, numberOfMeals, feedInGram);


            db.collection("user").document(uId).collection("Horse").document(horseName).collection("FeedPlan").document(feedId).set(feedPlan)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddHorseFeedActivity.this, "Futter erfolgreich im Futterplan hochgeladen!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "addFeedPlan: success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddHorseFeedActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());

                        }
                    });

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else{
            Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder für das Futter aus", Toast.LENGTH_LONG).show();
        }
    }
}
