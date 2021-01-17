package com.example.pferdeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.Database.Feed;
import com.example.pferdeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddFeedActivity extends AppCompatActivity {
    private static final String TAG = "AddFeedActivity";

    /**    private String name;
     private String brand; //Marke
     private String productDescription; //Produktbeschreibung
     private String composition; //Zusammensetzung
     private String feedingRecommendationDescription; //Fütterungsempfehlung
     private Map<String,Double> ingredients; //Inhaltsstoffe*/

    EditText mFeedName, mfeedBrand, mProductDescription, mComposition, mFeedingRecommendation, mFeedingRecommendationDescription, mIngredientsValue;
    Spinner mIngredients;
    Button mSaveFeedBtn, mBackToMainBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);

        mFeedName = findViewById(R.id.feed_name);
        mfeedBrand = findViewById(R.id.feed_brand);
        mProductDescription = findViewById(R.id.feed_product_description);
        mComposition = findViewById(R.id.feed_composition);
        mFeedingRecommendationDescription = findViewById(R.id.feed_recommendation_description);

        mIngredients = (Spinner) findViewById(R.id.feed_ingredients_spinner);
        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(this,
                R.array.ingrements_array, android.R.layout.simple_spinner_item);
        mIngredients.setAdapter(conditionAdapter);

        mIngredientsValue = findViewById(R.id.feed_ingredients_value);

        mSaveFeedBtn = findViewById(R.id.save_feed_information_button);
        mBackToMainBtn = findViewById(R.id.back_to_CalendarFragment_button);


        //weiterleitung zum Kalender Fragment
        mBackToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        mSaveFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String feedName = mFeedName.getText().toString().trim();
                String feedBrand = mfeedBrand.getText().toString().trim();
                String productDescription = mProductDescription.getText().toString().trim();
                String composition = mComposition.getText().toString().trim();
                String feedingRecommendationDescription = mFeedingRecommendationDescription.getText().toString().trim();


                Map<String, Double> ingredients = new HashMap<String, Double>();
                ingredients.put(mIngredients.getSelectedItem().toString(), Double.parseDouble(mIngredientsValue.getText().toString().trim()));

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = null;
                if (user !=null) {
                    uid = user.getUid();
                }


                Feed feed = new Feed(feedName, feedBrand, productDescription, composition, feedingRecommendationDescription, ingredients);
                String documentName = feedName + "_" + uid;
                db.collection("Feed").document(documentName).set(feed)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddFeedActivity.this, "Futter erfolgreich hochgeladen!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "addFeed: success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddFeedActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());

                            }
                        });

                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });


    }


}
