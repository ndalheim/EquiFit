package com.example.pferdeapp.Activities;

import android.content.Intent;
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
import com.example.pferdeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddHorseFeedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "AddHorseFeedActivity";


    EditText mFeedInGram;
    Spinner mFeed, mNumberOfMeals;
    Button maddFeedWithGrammBtn, mSaveHorseFeedBtn, mBackToMainBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<String> feedItems, feedName;
    //String[] feedName;
    ArrayAdapter<String> feedItemsAdapter;
    ListView feedListView;
    String feedRationItem;
    Map<String, Object> feedRation = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_horse_feed);


        //------------------------------
        feedName = getFeed();
        getNumberOfFeeds(feedName);

        //Futter
        mFeed = (Spinner) findViewById(R.id.feedSpinner);
        mFeed.setOnItemSelectedListener(this);

        /**ArrayAdapter<CharSequence> feedAdapter = ArrayAdapter.createFromResource(this,
                R.array.defects_array, android.R.layout.simple_spinner_item);
        mFeed.setAdapter(feedAdapter);*/

        ArrayAdapter<String> feedAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, feedName); //selected item will look like a spinner set from XML
        //feedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFeed.setAdapter(feedAdapter);


        //-----------------------------

        mFeedInGram = findViewById(R.id.feedInGrammValue);

        mNumberOfMeals = (Spinner) findViewById(R.id.numberOfMealsSpinner);
        ArrayAdapter<CharSequence> numberOfMealsAdapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_meals_array, android.R.layout.simple_spinner_item);
        mNumberOfMeals.setAdapter(numberOfMealsAdapter);


        //ListView mit Inhaltstoffen
        feedListView = findViewById(R.id.feed_ration_list_view);
        maddFeedWithGrammBtn = findViewById(R.id.addFeedWithGrammButton);

        // Speichern und zurück Button
        mSaveHorseFeedBtn = findViewById(R.id.save_feed_plan_button);
        mBackToMainBtn = findViewById(R.id.back_to_horse_information_button);


        //Inhaltstoffe-Hinzufügen-Button
        maddFeedWithGrammBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Double> feedWithGramm = new HashMap<>();
                feedWithGramm.put(mNumberOfMeals.getSelectedItem().toString(),Double.parseDouble(mFeedInGram.getText().toString().trim()));
                feedRation.put(mFeed.getSelectedItem().toString(), feedWithGramm);
                Toast.makeText(getApplicationContext(), feedRation.toString(), Toast.LENGTH_LONG).show();
                addFeedItem(view);

            }
        });

        feedItems = new ArrayList<>();
        feedItemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, feedItems);
        feedListView.setAdapter(feedItemsAdapter);

        deleteFeedItem();


        //Zurück-Button zur Main Activity
        mBackToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowHorseActivity.class));
            }
        });


        //Futter-Speichern-Button. Speichert Futter in der Datenbank
        mSaveHorseFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFeedPlanToFirestore();
            }
        });
    }

    private void addFeedPlanToFirestore() {
    }

    private void addFeedItem(View view) {
        String feed = mFeed.getSelectedItem().toString();
        String feedInGramm = mFeedInGram.getText().toString().trim();
        String numberOfMeals = mNumberOfMeals.getSelectedItem().toString();

        feedRationItem = feed + "  " + numberOfMeals + "x tägl. " +feedInGramm + "g";

        if (!(feed.equals("")||feedInGramm.equals("")||numberOfMeals.equals(""))){
            feedItemsAdapter.add(feedRationItem);
            mFeedInGram.setText("");
        }else{
            Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus", Toast.LENGTH_LONG).show();
        }
    }

    //löscht Inhalsstoffe aus der ListView
    private void deleteFeedItem() {
        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
    }

    public ArrayList<String> getFeed() {
        final ArrayList<String> feed = new ArrayList<>();
        db.collection("Feed").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> ids = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        feed.add((String) document.get("name"));

                    }
                }
                //mFeed = (Spinner) findViewById(R.id.feedSpinner);
                //ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this, ids, android.R.layout.simple_spinner_item);
                //mFeed.setAdapter(unitAdapter);
            }
        });
        return feed;
    }



    public  String[] getNumberOfFeeds(ArrayList<String> feed) {
        String[] feedArray = new String[feed.size()];
        Log.d(TAG, "_________________" + feed.size());
        for (int i = 0; i < feed.size(); i++) {
            feedArray[i] = feed.get(i);

        }

    return feedArray;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
