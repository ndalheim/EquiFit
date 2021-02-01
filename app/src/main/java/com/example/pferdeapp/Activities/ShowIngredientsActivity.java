package com.example.pferdeapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ShowIngredientsActivity extends AppCompatActivity {
    private static final String TAG = "ShowIngredientsActivity";


    Map<String, Double> mcalculatedIngredients = new HashMap<>();
    TextView ingredientsTextView;
    String horseWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ingredients);

        ingredientsTextView = findViewById(R.id.ingredients_text_view);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String horseNameString;
        final String userId;




        if (getIntent().hasExtra("HorseName") == true) {
            horseNameString = getIntent().getExtras().getString("HorseName");
            userId = getIntent().getExtras().getString("UserId");

            db.collection("user").document(userId).collection("Horse").document(horseNameString).collection("FeedPlan").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    Map<String, Double> feedInGramMap = new HashMap<>();
                    Double gramsPerDay;

                    for (DocumentSnapshot snapshot : documentSnapshot) {

                        Double feedInGram = snapshot.getDouble("feedInGram");
                        Double numberOfMeals = snapshot.getDouble("numberOfMeals");
                        String feedId = snapshot.getString("feedId");

                        gramsPerDay = feedInGram * numberOfMeals;


                        feedInGramMap.put(feedId, gramsPerDay);
                        //Log.d(TAG, "_______________________" + feedInGramMap.toString());
                        ingrements(feedId, userId, horseNameString, gramsPerDay, db, horseWeight);
                   }

                }

            });
        } else {
            Toast.makeText(ShowIngredientsActivity.this, "Daten wurden nicht übergeben", Toast.LENGTH_SHORT).show();
        }
    }

    //Rechnet alle Inhaltsstoffe von dem Futterplan von einem Pferd zusammen (Angaben in g pro Tag) und schreibt es in einer Map in die Datenbank zu dem Pferd
    private void ingrements(final String feedId, final String userId, final String horseName, final Double gramsPerDay, FirebaseFirestore db, final String horseWeight ) {

        db.collection("Feed").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Map<String, Double> calculatedIngredients = new HashMap<>();
                String horseWeight = null;

                for (DocumentSnapshot snapshot : value) {

                    if(feedId.equals(snapshot.getId().toString())){
                        //Log.d(TAG, "_______________________###" + snapshot.getId().toString() + " " + gramsPerDay);

                        Map<String, Object> ingredients = new HashMap<>();
                        Map<String, String> ingredientValues = new HashMap<>();
                        String ingredientsString;
                        Double percentInGram, gramPerDay;
                        ingredients = (Map<String, Object>) snapshot.get("ingredients");
                        //Log.d(TAG, "_______________________##" + ingredients);

                        for (Map.Entry e : ingredients.entrySet()){
                            ingredientValues = (Map<String, String>) e.getValue();
                            for(Map.Entry m : ingredientValues.entrySet()){
                                m.getValue();

                                if(m.getKey().equals("%")){
                                    percentInGram = Double.parseDouble(m.getValue().toString()) * 10;
                                }else{
                                    percentInGram = Double.parseDouble(m.getValue().toString());
                                }
                                gramPerDay = percentInGram * gramsPerDay / 1000;

                                if (mcalculatedIngredients.containsKey(e.getKey().toString())){
                                    Double test = mcalculatedIngredients.get(e.getKey().toString());

                                    mcalculatedIngredients.put(e.getKey().toString(), gramPerDay + test);

                                }else{
                                    mcalculatedIngredients.put(e.getKey().toString(), gramPerDay);
                                }


                            }

                            final FirebaseFirestore db2 = FirebaseFirestore.getInstance();

                            //Setzt Inhaltsstoffe von einem Pferd Pro Tag
                            db2.collection("user").document(userId).collection("Horse")
                                    .document(horseName).update("ingredientsPerDay", mcalculatedIngredients);

                            //liest HorseWeight aus der Datenbank aus
                            db2.collection("user").document(userId).collection("Horse").document(horseName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                             Double horseWeight = document.getDouble("horseWeight");

                                            showHorseIngredients(mcalculatedIngredients, horseWeight);
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        } else {
                                            Toast.makeText(ShowIngredientsActivity.this, "Dokument existiert nicht", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Toast.makeText(ShowIngredientsActivity.this, "task fehlgeschlagen", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }

                                }
                            });




                        }


                    }
                }

            }
        });
    }

    //TODO: Map aus der Datenbank auslesen
    private void showHorseIngredients(Map<String, Double> calculatetIngredients, Double horseWeight) {

        String ingentsientsText = "";

        for (Map.Entry e : calculatetIngredients.entrySet()){
            String key = e.getKey().toString();
            String value = e.getValue().toString();
            Double valueAsDouble = Double.parseDouble(e.getValue().toString());
            Double min, max;
            //Log.d(TAG, "_______________________#ingredients   " + key);



            switch (key) {
                case "Calcium":
                    min = 25.0;
                    max = 35.0;
                    ingentsientsText = checkIngredientsValue(key, valueAsDouble, min, max, ingentsientsText);
                    //Log.d(TAG, "_______________________#ingredients  Calcium");
                    break;
                case "Zink":
                    min = 70 * horseWeight;
                    max = 90 * horseWeight;
                    ingentsientsText = checkIngredientsValue(key, valueAsDouble, min, max, ingentsientsText);
                    break;
                case "Kupfer":
                    min = 10 * horseWeight;
                    max = 15 * horseWeight;
                    ingentsientsText = checkIngredientsValue(key, valueAsDouble, min, max, ingentsientsText);
                    break;
                case "Natrium":
                    min = 2 * horseWeight;
                    max = 7 * horseWeight;
                    ingentsientsText = checkIngredientsValue(key, valueAsDouble, min, max, ingentsientsText);
                    break;
                case "Chlorid":
                    min = 8 * horseWeight;
                    max = 17 * horseWeight;
                    ingentsientsText = checkIngredientsValue(key, valueAsDouble, min, max, ingentsientsText);
                    break;
                case "Selen":
                    min = 1.1;
                    max = 1.8;
                    ingentsientsText = checkIngredientsValue(key, valueAsDouble, min, max, ingentsientsText);
                    break;
                case "Carnitin":
                    min = 3.0;
                    max = 10.0;
                    ingentsientsText = checkIngredientsValue(key, valueAsDouble, min, max, ingentsientsText);
                    break;
                default:
                    double roundOff = Math.round(valueAsDouble * 100.0) / 100.0;
                    ingentsientsText+= key + ": " + roundOff + "\n";
                    break;
            }

        }


        ingredientsTextView.setText(ingentsientsText);
        //Log.d(TAG, "_______________________#ingramm" + ingentsientsText);
    }

    private String checkIngredientsValue(String key, Double value,  Double x, Double y, String ingentsientsText) {

        double roundOff = Math.round(value * 100.0) / 100.0;
        Log.d(TAG, "_______________________#ingramm" + roundOff);
        if ((value > x) && (value < y)){
            ingentsientsText+= key + ": " + roundOff + "Wert perfekt\n";
        }else if((value < x)){
            ingentsientsText += key + ": " + roundOff + " Wert zu niedrig\n";
        }else{
            ingentsientsText += key + ": " + roundOff + " Wert zu hoch\n";
        }

        return ingentsientsText;
    }


}
