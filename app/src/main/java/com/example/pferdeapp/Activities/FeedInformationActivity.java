package com.example.pferdeapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class FeedInformationActivity extends AppCompatActivity {
    private static final String TAG = "FeedInformationActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String documentNameString;
    TextView tvFeedName, tvIngredientsName, tvIngredientsValue, tvIngredientsUnit;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_information);

        tvFeedName = findViewById(R.id.feed_information);
        tvIngredientsName = findViewById(R.id.feed_ingredients_name);
        tvIngredientsValue = findViewById(R.id.feed_ingredients_value2);
        tvIngredientsUnit = findViewById(R.id.feed_ingredients_unit);


        if(getIntent().hasExtra("DocumentName") == true) {
            documentNameString = getIntent().getExtras().getString("DocumentName");

           Toast.makeText(this, "DokumentName:"+ documentNameString, Toast.LENGTH_SHORT).show();

           db.collection("Feed").document(documentNameString).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String futtername = document.getString("name");
                            String brand = document.getString("brand");
                            String price = document.getString("price");
                            String amount = document.getString("amount");
                            String productDescription = document.getString("productDescription");
                            String composition = document.getString("composition");
                            Map<String , Object> ingredients = (Map<String, Object>) document.get("ingredients");
                            String ingredientsName = "";
                            String ingredientsValue = "";
                            String IngredientsUnit= "";

                            Map<String, Double> ingredientValues;

                            tvFeedName.setText("Name: " + "\n" + futtername + "\n\n" +
                                    "Marke: " + "\n" + brand + "\n\n" +
                                    //"Preis und Menge: " + "\n" + price + " pro " + amount + "\n\n" +
                                    "Marke: " + "\n" + brand + "\n\n" +
                                    "Produktbeschreibung: " + "\n" + productDescription + "\n\n" +
                                    "Zusammensetzung: " + "\n" + composition + "\n\n");

                            int i = 0;
                            // läuft über alle Inhaltsstoffe eines Futters
                            for (Map.Entry e : ingredients.entrySet()){
                                // schreibt Einheit und Value des Inhaltsstoffes in eine Map
                                ingredientValues = (Map<String, Double>) e.getValue();
                                String name = e.getKey().toString() + "\n";

                                ingredientsName += name ;

                                // Läuft über zwei Elemente Einheit und Value
                                for(Map.Entry m : ingredientValues.entrySet()){
                                    String unit = m.getKey().toString() + "   \n";
                                    String value = m.getValue().toString() + "   \n";

                                    IngredientsUnit += unit ;
                                    ingredientsValue += value ;


                                }
                            }

                            Log.d(TAG, "onComplete: ------------" + ingredientsName);
                            tvIngredientsName.setText(ingredientsName);
                            tvIngredientsValue.setText(ingredientsValue);
                            tvIngredientsUnit.setText(IngredientsUnit);

                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Toast.makeText(FeedInformationActivity.this, documentNameString, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FeedInformationActivity.this, "Dokument existiert nicht", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Toast.makeText(FeedInformationActivity.this, "task fehlgeschlagen", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
           });
        }else{
            Toast.makeText(FeedInformationActivity.this, "Daten wurden nicht übergeben", Toast.LENGTH_SHORT).show();
        }
    }
}
