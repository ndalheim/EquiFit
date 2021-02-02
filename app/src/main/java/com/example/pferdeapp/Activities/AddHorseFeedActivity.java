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

    private List<String> feedList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_horse_feed);


        mfeedListView = findViewById(R.id.feed_list);


        //------------------------------
        //feedName = getFeed();
        //getNumberOfFeeds(feedName);
        Toast.makeText(getApplicationContext(), feedRation.toString(), Toast.LENGTH_LONG).show();

        //Futter
        //mFeed = (Spinner) findViewById(R.id.feedSpinner);
        //mFeed.setOnItemSelectedListener(this);

        /**ArrayAdapter feedAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, feedName);
        mFeed.setAdapter(feedAdapter);*/

        /**ArrayAdapter<CharSequence> feedAdapter = ArrayAdapter.createFromResource(this,
                R.array.defects_array, android.R.layout.simple_spinner_item);*/

        /**feedName = HorseFeedHelpClass.getFeedNames();

        ArrayAdapter<String> feedAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, feedName); //selected item will look like a spinner set from XML
        feedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mFeed.setAdapter(feedAdapter);*/


        //-----------------------------

        mFeedInGram = findViewById(R.id.feedInGrammValue);

        mNumberOfMeals = (Spinner) findViewById(R.id.numberOfMealsSpinner);
        ArrayAdapter<CharSequence> numberOfMealsAdapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_meals_array, android.R.layout.simple_spinner_item);
        mNumberOfMeals.setAdapter(numberOfMealsAdapter);


        //ListView mit Inhaltstoffen
        //feedPlanListView = findViewById(R.id.feed_ration_list_view);
        //maddFeedWithGrammBtn = findViewById(R.id.addFeedWithGrammButton);

        // Speichern und zurück Button
        mSaveHorseFeedBtn = findViewById(R.id.save_feed_plan_button);
        mBackToMainBtn = findViewById(R.id.back_to_horse_information_button);


        //Inhaltstoffe-Hinzufügen-Button
        /**maddFeedWithGrammBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Double> feedWithGramm = new HashMap<>();
                feedWithGramm.put("NumberOfMeals",Double.parseDouble(mNumberOfMeals.getSelectedItem().toString()));
                feedWithGramm.put("feedInGram",Double.parseDouble(mFeedInGram.getText().toString().trim()));
                feedRation.put(feedId, feedWithGramm);
                Toast.makeText(getApplicationContext(), feedRation.toString(), Toast.LENGTH_LONG).show();
                addFeedItem(view);

            }
        });*/

        /**feedItems = new ArrayList<>();
        feedItemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, feedItems);
        feedPlanListView.setAdapter(feedItemsAdapter);*/

        db.collection("Feed").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                feedList.clear();

                for (DocumentSnapshot snapshot : documentSnapshot){
                    //feedList.add(snapshot.getString("horseName"));
                    feedList.add(snapshot.getId());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, feedList);
                adapter.notifyDataSetChanged();
                mfeedListView.setAdapter(adapter);
            }
        });



        mfeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                for (int i = 0; i < mfeedListView.getChildCount(); i++) {
                    if(position == i ){
                        mfeedListView.getChildAt(i).setBackgroundColor(Color.BLUE);
                        feedId = feedList.get(position);
                    }else{
                        mfeedListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });

        //deleteFeedItem();


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

        Double numberOfMeals = Double.parseDouble(mNumberOfMeals.getSelectedItem().toString());
        Double feedInGram = Double.parseDouble(mFeedInGram.getText().toString().trim());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uId = null;
        if (user !=null) {
            uId = user.getUid();
        }


        if(feedId == null){
            Toast.makeText(AddHorseFeedActivity.this, "Es wurde kein Futter ausgewählt", Toast.LENGTH_SHORT).show();
        }else if(!(feedId.equals("") || numberOfMeals.equals("") || feedInGram.equals(""))){

            FeedPlan feedPlan = new FeedPlan(feedId, numberOfMeals, feedInGram);


            db.collection("user").document(uId).collection("Horse").document(horseName).collection("FeedPlan").document(feedId).set(feedPlan)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddHorseFeedActivity.this, "Futterplan erfolgreich hochgeladen!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder für den Futterplan aus", Toast.LENGTH_LONG).show();
        }


    }

    /**private void addFeedItem(View view) {
        String feedInGramm = mFeedInGram.getText().toString().trim();
        String numberOfMeals = mNumberOfMeals.getSelectedItem().toString();

        feedRationItem = feedId + "  " + numberOfMeals + "x tägl. " +feedInGramm + "g";

        if (!(feedId.equals("")||feedInGramm.equals("")||numberOfMeals.equals(""))){
            feedItemsAdapter.add(feedRationItem);
            mFeedInGram.setText("");
        }else{
            Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus", Toast.LENGTH_LONG).show();
        }
    }*/

    //löscht Inhalsstoffe aus der ListView
    /**private void deleteFeedItem() {
        feedPlanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Toast.makeText(getApplicationContext(), feedItems.get(position), Toast.LENGTH_LONG).show();
                //soll Inhaltstoff aus der Map löschen
                feedRation.remove(feedItems.get(position).split("  ")[0]);
                //ingredientsUnit.remove(items.get(position).split("  ")[0]);

                feedItems.remove(position);
                feedItemsAdapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(), "Inhaltsstoff aus der Liste gelöscht", Toast.LENGTH_LONG).show();
            }
        });
    }*/

    /**public void getFeed() {
        final ArrayList<String> feed = new ArrayList<>();
        db.collection("Feed").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> ids = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getId();
                        //Log.d(TAG, "_________________#####" + document.getId());
                        //feed.add((String) document.get("name"));

                    }
                }
                //mFeed = (Spinner) findViewById(R.id.feedSpinner);
                //ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this, ids, android.R.layout.simple_spinner_item);
                //mFeed.setAdapter(unitAdapter);
            }
        });
        //return feed;
    }*/




    /**public  String[] getNumberOfFeeds(ArrayList<String> feed) {
        String[] feedArray = new String[feed.size()];
        Log.d(TAG, "_________________" + feed.size());
        for (int i = 0; i < feed.size(); i++) {
            feedArray[i] = feed.get(i);

        }

    return feedArray;
    }*/


}
