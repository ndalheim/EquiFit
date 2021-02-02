package com.example.pferdeapp.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.R;
import com.example.pferdeapp.hilfsklassen.IngredientsDialog;
import com.example.pferdeapp.hilfsklassen.IngredientsListAdapter;
import com.example.pferdeapp.hilfsklassen.IngredientsListModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowIngredientsActivity extends AppCompatActivity {
    private static final String TAG = "ShowIngredientsActivity";


    Map<String, Double> mcalculatedIngredients = new HashMap<>();
    TextView ingredientsTextView;
    ListView mIngredientsListView;
    String horseWeight;
    //String[] ingredientsItems;
    ArrayList<IngredientsListModel> ingredientsList;
    IngredientsListAdapter ingredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ingredients);

        ingredientsTextView = findViewById(R.id.ingredients_text_view);
        mIngredientsListView = findViewById(R.id.ingredients_list_view);

        ingredientsList = new ArrayList<>();
        ingredientsAdapter = new IngredientsListAdapter(this, R.layout.list_item, ingredientsList);
        mIngredientsListView.setAdapter(ingredientsAdapter);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String horseNameString;
        final String userId;




        if (getIntent().hasExtra("HorseName") == true) {
            horseNameString = getIntent().getExtras().getString("HorseName");
            userId = getIntent().getExtras().getString("UserId");

            // Liest den Futterplan eines Pferdes aus der Datenbank aus
            db.collection("user").document(userId).collection("Horse")
                    .document(horseNameString).collection("FeedPlan")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    Map<String, Double> feedInGramMap = new HashMap<>();
                    Double gramsPerDay;

                    // Läuft über alle Futter-Dokumente eines Futterplans
                    for (DocumentSnapshot snapshot : documentSnapshot) {

                        Double feedInGram = snapshot.getDouble("feedInGram");
                        Double numberOfMeals = snapshot.getDouble("numberOfMeals");
                        String feedId = snapshot.getString("feedId");

                        // Berechnung des Futters in Gramm pro Tag
                        gramsPerDay = feedInGram * numberOfMeals;

                        // Gramm Pro Tag wird mit der FutterID einer Map hinzugefügt
                        feedInGramMap.put(feedId, gramsPerDay);

                        // Methode für weitere Berechnungen aufrufen
                        ingredients(feedId, userId, horseNameString, gramsPerDay, db, horseWeight);
                   }

                }

            });
        } else {
            Toast.makeText(ShowIngredientsActivity.this, "Daten wurden nicht übergeben", Toast.LENGTH_SHORT).show();
        }
    }

    //Rechnet alle Inhaltsstoffe von dem Futterplan von einem Pferd zusammen (Angaben in g pro Tag) und schreibt es in einer Map in die Datenbank zu dem Pferd
    private void ingredients(final String feedId, final String userId, final String horseName, final Double gramsPerDay, FirebaseFirestore db, final String horseWeight ) {

        // Ruft Collection Feed auf
        db.collection("Feed").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Map<String, Double> calculatedIngredients = new HashMap<>();
                String horseWeight = null;

                // Durchsucht die Futter-Datenbank nach dem Futter aus dem Futterplan
                for (DocumentSnapshot snapshot : value) {
                    if(feedId.equals(snapshot.getId().toString())){

                        Map<String, Object> ingredients = new HashMap<>();
                        Map<String, String> ingredientValues = new HashMap<>();
                        String ingredientsString;
                        Double percentInGram, gramPerDay;

                        // Schreibt alle Inhaltsstoffe aus der Datenbank in eine Map
                        ingredients = (Map<String, Object>) snapshot.get("ingredients");

                        // läuft über alle Inhaltsstoffe eines Futters
                        for (Map.Entry e : ingredients.entrySet()){
                            // schreibt Einheit und Value des Inhaltsstoffes in eine Map
                            ingredientValues = (Map<String, String>) e.getValue();

                            // Läuft über zwei Elemente Einheit und Value
                            for(Map.Entry m : ingredientValues.entrySet()){
                                m.getValue();

                                // prüft ob die einheit % ist
                                if(m.getKey().equals("%")){
                                    // Falls ja wird der Wert *10 gerechnet um die Grammzahl zu berechnen
                                    percentInGram = Double.parseDouble(m.getValue().toString()) * 10;
                                }else{
                                    // Ansonsten ist der Wert bereits in Gramm angegeben
                                    percentInGram = Double.parseDouble(m.getValue().toString());
                                }

                                // Berechnet für einen Inhaltsstoff die Grammanzahl Pro Tag
                                gramPerDay = percentInGram * gramsPerDay / 1000;

                                // Prüft ob das Pferd diesen Inhaltsstoff schon von einem anderen Futter bekommt
                                if (mcalculatedIngredients.containsKey(e.getKey().toString())){
                                    //Falls ja, werden beide Werte addiert und der Map hinzugefügt
                                    Double previousIngredients = mcalculatedIngredients.get(e.getKey().toString());
                                    mcalculatedIngredients.put(e.getKey().toString(), gramPerDay + previousIngredients);

                                }else{
                                    // Falls nein, wird er der Wert der Map hinzugefügt
                                    mcalculatedIngredients.put(e.getKey().toString(), gramPerDay);
                                }


                            }

                            final FirebaseFirestore db2 = FirebaseFirestore.getInstance();

                            // Schreibt Inhaltsstoffe von einem Pferd Pro Tag in die Datenbank
                            db2.collection("user").document(userId).collection("Horse")
                                    .document(horseName).update("ingredientsPerDay", mcalculatedIngredients);

                            // Liest HorseWeight aus der Datenbank aus
                            db2.collection("user").document(userId).collection("Horse").document(horseName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Double horseWeight = document.getDouble("horseWeight");

                                             // Ruft Methode auf die die Menge der Inhaltsstoffe
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
    // Methode überprüft ob Inhaltsstoffe im Rahmen liegen (Falls ein Rahmen gegeben ist)
    private void showHorseIngredients(Map<String, Double> calculatetIngredients, Double horseWeight) {

        ingredientsList.clear();
        String info = "";
        double roundOff;
        IngredientsListModel ingredients;

        for (Map.Entry e : calculatetIngredients.entrySet()){
            String key = e.getKey().toString();
            String value = e.getValue().toString();
            Double valueAsDouble = Double.parseDouble(e.getValue().toString());
            Double min, max;


            switch (key) {
                case "Calcium":
                    min = 25.0;
                    max = 35.0;
                    info = "Calciummangel liegt vor, wenn sich folgende Anzeichen abzeichnen:\n" +
                            "\n" +
                            "Muskelprobleme, Muskelkrämpfe und Muskelverspannungen\n" +
                            "Zahnprobleme von dauerhafter Natur\n" +
                            "Knochenprobleme, wie eine Veränderung der Knochendichte (Röntgenuntersuchung) ";
                    checkIngredientsValue(key, valueAsDouble, min, max, info);
                    break;
                case "Zink":
                    min = 70 * horseWeight / 100;
                    max = 90 * horseWeight / 100;
                    info = "";
                    checkIngredientsValue(key, valueAsDouble, min, max, info);
                    break;
                case "Kupfer":
                    min = 10 * horseWeight / 100;
                    max = 15 * horseWeight / 100;
                    info = "Kupfer ist ein Spurenelement und wird von jedem Pferd in ausreichender Menge benötigt, besonders jedoch von Zuchtstuten und Turnierpferden. Kupfer erfüllt viele Aufgaben im Organismus des Pferdekörpers:\n" +
                            "\n" +
                            "Kupfer stärkt Bänder, Sehnen, Knorpel und Gelenke und das Bindegewebe und ist damit für die Funktion des ganzen Bewegungsapparates in hohem Masse verantwortlich.\n" +
                            "Leiden Zuchtstuten während der Trächtigkeit unter Kupfermangel, kann sich dies negativ auf die Knochendichte des Fohlens auswirken. Knochenchips im späteren Entwicklungsstadium können die Folge sein.\n" +
                            "Kupfer fördert das Haut- und Hufwachstum\n" +
                            "Kupfer mindert die Allergie- und Infektanfälligkeit\n" +
                            "Kupfer fördert Konzentration und Leistungsschwäche\n" +
                            "Tritt Kupfermangel auf, kommt es zu Imbalancen in diesen Bereichen. Definitiv festgestellt kann ein Kupfermangel nur über einen erweiterten Bluttest.\n" +
                            "Das Schwierigkeit bei der Behebung von Kupfermangel ist, dass Kupfer die Verstoffwechslung von Zink hemmen kann. Dies wiederum kann zu Zinkmangel führen, was unbedingt zu vermeiden ist.";
                    checkIngredientsValue(key, valueAsDouble, min, max, info);
                    break;
                case "Natrium":
                    min = 2 * horseWeight / 100;
                    max = 7 * horseWeight / 100;
                    info = "Natriummangel\n" +
                            "\n" +
                            "Natrium ist ein Elektrolyt und geht in hohem Maße bei der Schweißausscheidung verloren, aber auch bei Durchfall – kurz: bei jeder Form des Flüssigkeitsverlustes. Natrium wird in Form von Salzen aufgenommen, meist über Leckschalen und Lecksteine.  Steht dem Pferd nicht ausreichend Natrium zur Selbstversorgung zur Verfügung oder wird Natrium vermehrt ausgeschieden entsteht Natriummangel. Bei Natriummangel\n" +
                            "\n" +
                            "verlangsamen sich Herzschlag und Puls\n" +
                            "die Durchblutung wird geschwächt\n" +
                            "das Pferd hat übermäßigen Durst und ist sehr matt\n" +
                            "einhergehend mit Flüssigkeitsverlust ist die Haut rissig und das Fell wird stumpfer\n" +
                            "Das vermehrte Händelecken kann ein Zeichen für Salz-, respektive Natriummangel sein, muß aber nicht.";
                    checkIngredientsValue(key, valueAsDouble, min, max, info);
                    break;
                case "Chlorid":
                    min = 8 * horseWeight / 100;
                    max = 17 * horseWeight / 100;
                    info = "";
                    checkIngredientsValue(key, valueAsDouble, min, max, info);
                    break;
                case "Selen":
                    min = 1.1;
                    max = 1.8;
                    info = "Symptome für Selenmangel\n" +
                            "\n" +
                            "Allgemeine Mattheit\n" +
                            "Muskelprobleme bis hin zu Krämpfen\n" +
                            "Schlechtes Haarkleid\n" +
                            "Zellschäden\n" +
                            "Schlechtes Immunsystem\n" +
                            "Eindeutig feststellen läßt sich Selenmangel anhand eines Blutbildes, das der Tierarzt ins Labor schickt.";
                    checkIngredientsValue(key, valueAsDouble, min, max, info);
                    break;
                case "Carnitin":
                    min = 3.0;
                    max = 10.0;
                    info = "";
                    checkIngredientsValue(key, valueAsDouble, min, max, info);
                    break;
                case "Eisen":
                    info = "Folgende Anzeichen deuten auf einen Eisenmangel hin:\n" +
                            "\n" +
                            "plötzliche Antriebslosigkeit, Mattheit, Leistungsschwäche, Konditionsprobleme\n" +
                            "Appetitlosigkeit und Gewichtsabnahme (Untergewicht)\n" +
                            "Fellprobleme\n" +
                            "Immunprobleme, Anfälligkeit";
                    clickable(info);
                    roundOff = Math.round(valueAsDouble * 100.0) / 100.0;
                    ingredients = new IngredientsListModel(key, Double.toString(roundOff));
                    ingredientsList.add(ingredients);
                    ingredientsAdapter.notifyDataSetChanged();
                    break;
                default:
                    roundOff = Math.round(valueAsDouble * 100.0) / 100.0;
                    ingredients = new IngredientsListModel(key, Double.toString(roundOff));
                    ingredientsList.add(ingredients);
                    ingredientsAdapter.notifyDataSetChanged();
                    break;
            }

        }

    }

    // Methode zeigt die Menge der einzelnen Inhalstoffe pro tag in einer Liste an mit eventuelller Fehlermeldung
    private void checkIngredientsValue(String key, Double value,  Double x, Double y, String mInfo) {

        double roundOff = Math.round(value * 100.0) / 100.0;
        IngredientsListModel ingredients;

        // Wert Perfekt
        if ((value >= x) && (value <= y)){
            // Wenn eine Info vorhanden ist
            if(!mInfo.isEmpty()){
                ingredients = new IngredientsListModel(key, Double.toString(roundOff) + "g", x + " - " + y, mInfo);
                clickable(mInfo);
            }else{
                ingredients = new IngredientsListModel(key, Double.toString(roundOff) + "g", x + " - " + y);
            }

            ingredientsList.add(ingredients);
            ingredientsAdapter.notifyDataSetChanged();
            
        // Wert zu niedrig    
        }else if((value < x)){
            // Wenn eine Info vorhanden ist
            if(!mInfo.isEmpty()){
                ingredients = new IngredientsListModel(key, Double.toString(roundOff) + "g", x + " - " + y, mInfo);
                clickable(mInfo);
            }else{
                ingredients = new IngredientsListModel(key, Double.toString(roundOff) + "g", x + " - " + y);
            }

            ingredientsList.add(ingredients);
            ingredientsAdapter.notifyDataSetChanged();

        // Wert zu hoch    
        }else{
            // Wenn eine Info vorhanden ist
            if(!mInfo.isEmpty()){
                ingredients = new IngredientsListModel(key, Double.toString(roundOff) + "g", x + " - " + y, mInfo);
                clickable(mInfo);
            }else{
                ingredients = new IngredientsListModel(key, Double.toString(roundOff) + "g", x + " - " + y);
            }

            ingredientsList.add(ingredients);
            ingredientsAdapter.notifyDataSetChanged();
        }

    }

    // Methode macht Listeneinträge clickable, falls sie eine Info besitzen
    private void clickable(final String mInfo) {

        mIngredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                IngredientsDialog informationDialog = new IngredientsDialog();
                Log.d(TAG, "onItemClick: ______________" + ingredientsList.get(position).getInfo());
                if(ingredientsList.get(position).getInfo() == null){
                    informationDialog.setMessage("Es sind keine Informationen zu diesem Inhaltsstoff Verfügbar");
                }else{
                    informationDialog.setMessage(ingredientsList.get(position).getInfo());
                }

                informationDialog.show(getSupportFragmentManager(), "#################beispiel Dialog");

            }
        });

    }


}
