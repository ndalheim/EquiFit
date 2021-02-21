package com.example.pferdeapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.R;
import com.example.pferdeapp.hilfsklassen.DeleteDialog;
import com.example.pferdeapp.hilfsklassen.FeedPlanAdapter;
import com.example.pferdeapp.hilfsklassen.FeedPlanListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowHorseInformationActivity extends AppCompatActivity {
    private static final String TAG = "ShowHorseActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String horseNameString, userId, horseName, horseHeight, horseWeight, horseCondition, defect, intolerance;
    ListView mfeedListView;
    TextView textViewHorseName, textViewHorseInformation, textViewTextHorseInformation;
    Button goToAddFeedBtn, goBackToHorseFragmentBtn, mCalculateIngrementsBtn;
    ImageButton mEditHorseBtn;
    //private List<String> feedList = new ArrayList<>();
    ArrayList<FeedPlanListModel> feedPlanList;
    FeedPlanAdapter feedPlanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_show_horse);

        final Intent intent = new Intent(getApplicationContext(), AddHorseFeedActivity.class);

        textViewHorseInformation = findViewById(R.id.horseInformationTextView);
        textViewTextHorseInformation = findViewById(R.id.horseInformationTextTextView);
        textViewHorseName = findViewById(R.id.horse_name_text_view);
        mEditHorseBtn = findViewById(R.id.edit_horse_button);


        goToAddFeedBtn = findViewById(R.id.add_horse_feed_button);
        goBackToHorseFragmentBtn = findViewById(R.id.back_to_horse_fragment_button);
        mfeedListView = findViewById(R.id.horse_feed_plan);
        mCalculateIngrementsBtn = findViewById(R.id.calculate_ingredients_button);

        feedPlanList = new ArrayList<>();
        feedPlanAdapter = new FeedPlanAdapter(this, R.layout.list_item, feedPlanList);
        mfeedListView.setAdapter(feedPlanAdapter);


        //Geh zur AddHorseFeedActivity
        goToAddFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        //Geh zur AddHorseFeedActivity
        goBackToHorseFragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });



        if(getIntent().hasExtra("HorseName") == true) {

            horseNameString = getIntent().getExtras().getString("HorseName");
            horseWeight = getIntent().getExtras().getString("HorseWeight");
            horseHeight = getIntent().getExtras().getString("HorseHigh");
            horseCondition = getIntent().getExtras().getString("HorseCondition");
            defect = getIntent().getExtras().getString("HorseDefect");
            intolerance = getIntent().getExtras().getString("HorseIntolerance");

            userId= getIntent().getExtras().getString("UserId");

            mEditHorseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intentEditHorse = new Intent(getApplicationContext(), AddHorseActivity.class);

                    intentEditHorse.putExtra("HorseName", horseNameString);
                    intentEditHorse.putExtra("HorseWeight", horseWeight);
                    intentEditHorse.putExtra("HorseHigh", horseHeight);
                    intentEditHorse.putExtra("HorseCondition", horseCondition);
                    intentEditHorse.putExtra("HorseDefect", defect);
                    intentEditHorse.putExtra("HorseIntolerance", intolerance);

                    startActivity(intentEditHorse);

                }
            });

            mCalculateIngrementsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intentIngredients = new Intent(getApplicationContext(), ShowIngredientsActivity.class);

                    intentIngredients.putExtra("HorseName", horseNameString);
                    intentIngredients.putExtra("UserId", userId);
                    startActivity(intentIngredients);

                }
            });


            intent.putExtra("HorseId", horseNameString);

            Log.d(TAG, horseNameString);

            // geht beim Klicken auf ein ListenItem (Futter) werden die Futterinformationen geöffnet
            mfeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Intent intent = new Intent(getApplicationContext(), FeedInformationActivity.class);
                    intent.putExtra("DocumentName", feedPlanList.get(position).getFeedID());
                    startActivity(intent);
                }
            });


            // Beim  langen Klicken auf ein ListenItem (Futter) wird ein Dialog Fenster geöffnet indem der Nutzer das Futter aus dem Futterplan löschen kann
            mfeedListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DeleteDialog deleteDialog = new DeleteDialog();
                    deleteDialog.setFeedName(feedPlanList.get(i).getFeedID());
                    deleteDialog.setFeedFromHorse(horseNameString);
                    deleteDialog.show(getSupportFragmentManager(), "test");
                    return false;
                }
            });

            //Füllt die ListView mit den Datenbankeinträgen von den Futterplan
            db.collection("user").document(userId).collection("Horse").document(horseNameString).collection("FeedPlan").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    feedPlanList.clear();
                    FeedPlanListModel feedPlan;

                    for (DocumentSnapshot snapshot : documentSnapshot){
                        if(snapshot.get("feedId") == null){
                            feedPlan = new FeedPlanListModel("Füge Futter dem Futterplan hinzu");
                            feedPlanList.add(feedPlan);

                        }else{
                            String[] splitFeedId = snapshot.getString("feedId").split("_");

                            String ration = snapshot.get("numberOfMeals").toString() + "x tägl. " + snapshot.get("feedInGram").toString()  + "g";
                            feedPlan = new FeedPlanListModel(splitFeedId[1],  splitFeedId[0],  ration , snapshot.getString("feedId"));

                            feedPlanList.add(feedPlan);
                        }
                    }
                    feedPlanAdapter.notifyDataSetChanged();
                }
            });

            //Liest die Pferdeinformationen aus der Datenbank aus und zeigt sie in der Activity an
            db.collection("user").document(userId).collection("Horse").document(horseNameString).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            horseName = document.getString("horseName");
                            double helpValue = document.getDouble("horseHeight");
                            horseHeight = String.valueOf((int) helpValue);
                            double helpValue2 = document.getDouble("horseWeight");
                            horseWeight = String.valueOf((int) helpValue2);
                            horseCondition = document.getString("horseCondition");
                            defect = document.getString("defect");
                            intolerance = document.getString("intolerance");

                            textViewHorseName.setText(horseName);

                            textViewTextHorseInformation.setText("Name: \n"
                                    + "Stockmaß: \n"
                                    + "Gewicht: \n"
                                    + "Trainingszustand:     \n"
                                    + "Mangel: \n"
                                    + "Intoleranz: \n");

                            textViewHorseInformation.setText(horseName + "\n"
                                    + horseHeight +"\n"
                                    + horseWeight + " kg" + "\n"
                                    + horseCondition + "\n"
                                    + defect + "\n"
                                    + intolerance + "\n");

                            intent.putExtra("HorseName", horseName);
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Toast.makeText(ShowHorseInformationActivity.this, "Dokument existiert nicht", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Toast.makeText(ShowHorseInformationActivity.this, "task fehlgeschlagen", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }else{
            Toast.makeText(ShowHorseInformationActivity.this, "Daten wurden nicht übergeben", Toast.LENGTH_SHORT).show();
        }

    }
}
