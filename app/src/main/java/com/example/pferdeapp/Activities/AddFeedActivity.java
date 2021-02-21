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
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.Database.Feed;
import com.example.pferdeapp.Database.FeedCosts;
import com.example.pferdeapp.R;
import com.example.pferdeapp.hilfsklassen.HorseFeedHelpClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddFeedActivity extends AppCompatActivity {
    private static final String TAG = "AddFeedActivity";

    /**    private String name;
     private String brand; //Marke
     private String productDescription; //Produktbeschreibung
     private String composition; //Zusammensetzung
     private String feedingRecommendationDescription; //Fütterungsempfehlung
     private Map<String,Double> ingredients; //Inhaltsstoffe*/

    EditText mFeedName, mfeedBrand, mProductDescription, mComposition, mFeedingRecommendationDescription, mIngredientsValue, mAmount, mCosts, mJSONPath;
    Spinner mIngredients, mUnit;
    Button mAddIngredientsBtn, mSaveFeedBtn, mBackToMainBtn, mLoadJSONBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Map<String, Map<Double, String>> ingredients = new HashMap<String, Map<Double, String>>();
    Map<String, Object> ingredients = new HashMap<>();
    List<String> ingredientsList = new ArrayList<String>();
    //Map<String, String> ingredientsUnit = new HashMap<>();
    //Map<Double, String> unit = new HashMap<Double, String>();

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView listView;
    String ingredientsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);
        //Name, Marke, Produktbeschreibung, Zusammensetzung, Fütterungsempfehlung
        mFeedName = findViewById(R.id.feed_name);
        mfeedBrand = findViewById(R.id.feed_brand);
        mProductDescription = findViewById(R.id.feed_product_description);
        mComposition = findViewById(R.id.feed_composition);
        mFeedingRecommendationDescription = findViewById(R.id.feed_recommendation_description);
        mAmount = findViewById(R.id.feed_amount);
        mCosts = findViewById(R.id.feed_costs);

        //Inhaltstoffe
        mIngredients = (Spinner) findViewById(R.id.feed_ingredients_spinner);
        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(this,
                R.array.ingrements_array, android.R.layout.simple_spinner_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIngredients.setAdapter(conditionAdapter);

        mIngredientsValue = findViewById(R.id.feed_ingredients_value);

        mUnit = (Spinner) findViewById(R.id.unit_spinner);
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUnit.setAdapter(unitAdapter);

        mAddIngredientsBtn = findViewById(R.id.add_ingredients_button);

        //ListView mit Inhaltstoffen
        listView = findViewById(R.id.ingredients_list_view);
        mAddIngredientsBtn = findViewById(R.id.add_ingredients_button);

        // Speichern und zurück Button
        mSaveFeedBtn = findViewById(R.id.save_feed_information_button);
        mBackToMainBtn = findViewById(R.id.back_to_CalendarFragment_button);

        //EditText und Button um JSON Datei in der Datenbank hochzuladen
        mLoadJSONBtn = findViewById(R.id.save_json_button);
        //mJSONPath = findViewById(R.id.json_path);

        //Inhaltstoffe-Hinzufügen-Button
        mAddIngredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> ingredientsUnit = new HashMap<>();
                ingredientsUnit.put(mUnit.getSelectedItem().toString(),mIngredientsValue.getText().toString().trim());
                //ingredientsUnit.put("unit",mUnit.getSelectedItem().toString());
                ingredients.put(mIngredients.getSelectedItem().toString(), ingredientsUnit);
                ingredientsList.add(mIngredients.getSelectedItem().toString());
                Toast.makeText(getApplicationContext(), ingredients.toString(), Toast.LENGTH_LONG).show();
                addIngredientItem(view);

            }
        });

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);

        deleteIngredientsItems();


        //Zurück-Button zur Main Activity
        mBackToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        //Futter-Speichern-Button. Speichert Futter in der Datenbank
        mSaveFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Nutzereingaben speichern
                String feedName = mFeedName.getText().toString().trim();
                String feedBrand = mfeedBrand.getText().toString().trim();
                String productDescription = mProductDescription.getText().toString().trim();
                String composition = mComposition.getText().toString().trim();
                String feedingRecommendationDescription = mFeedingRecommendationDescription.getText().toString().trim();
                Double feedAmout = Double.parseDouble(mAmount.getText().toString().trim());
                Double feedPrice = Double.parseDouble(mCosts.getText().toString().trim());

                //Futterdaten der Futterdatenbank hinzufügen
                addFeedToFirestore(feedName, feedBrand, productDescription, composition, feedingRecommendationDescription, feedAmout, feedPrice);
            }
        });

        mLoadJSONBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String jsonPath = mJSONPath.getText().toString().trim();
                readJSON();
            }
        });


    }

    private void readJSON() {
        
        InputStream is = getResources().openRawResource(R.raw.feed_database);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();

        String json = "";

        try {
            while ((json = reader.readLine()) != null){
                stringBuilder.append(json);
            }
            reader.close();
            createJSONObject(stringBuilder.toString());
        }catch (IOException | JSONException e) {
            Toast.makeText(getApplicationContext(),"ERROR", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void createJSONObject(String jsonString) throws JSONException {

        JSONObject obj = new JSONObject(jsonString);
        //String pageName = obj.getJSONObject("address").getString("city");

        String feedID;
        String feedName;
        String feedBrand;
        String productDescription;
        String composition;
        String feedingRecommendationDescription;
        Map<String, String> ingredient = null;
        String unit;
        String value;
        String ingredientsName;
        Double feedAmout;
        Double feedPrice;



        ingredientsList.clear();
        ingredients.clear();

        Iterator<String> keys = obj.keys();
        JSONArray keys2 = obj.names();
        //Läuft über die namen der Futter
        for (int i = 0; i < keys2.length(); i++) {
            feedID = keys2.getString(i);
            feedName = obj.getJSONObject(feedID).getString("name");
            feedBrand = obj.getJSONObject(feedID).getString("brand");
            productDescription = obj.getJSONObject(feedID).getString("productDescription");
            composition = obj.getJSONObject(feedID).getString("composition");
            feedingRecommendationDescription = obj.getJSONObject(feedID).getString("feedingRecommendation");
            feedAmout = Double.valueOf(obj.getJSONObject(feedID).getString("amount"));
            feedPrice = Double.valueOf(obj.getJSONObject(feedID).getString("price"));


            JSONObject ingredientsObject  = obj.getJSONObject(feedID).getJSONObject("ingredients");
            Iterator<String> ingredientsKeys = ingredientsObject.keys();
            JSONArray ingredientsArray = ingredientsObject.names();
            ingredientsList.add(ingredientsArray.getString(0));

            for (int j = 0; j < ingredientsArray.length(); j++) {
                Map<String, String> ingredientsUnit = new HashMap<>();

                ingredientsName = ingredientsArray.getString(i);

                unit = ingredientsObject.getJSONObject(ingredientsName).getString("unit");
                value = ingredientsObject.getJSONObject(ingredientsName).getString("quantity");


                ingredientsUnit.put(unit, value);

                ingredients.put(ingredientsName, ingredientsUnit);
            }
            addFeedToFirestore(feedName, feedBrand, productDescription, composition, feedingRecommendationDescription, feedAmout, feedPrice);
        }
    }


    private void addIngredientItem(View view) {
        String ingredients = mIngredients.getSelectedItem().toString();
        String ingredientsValue = mIngredientsValue.getText().toString().trim();
        String unit = mUnit.getSelectedItem().toString();

        ingredientsItem = ingredients + "  " + ingredientsValue + " " + unit;

        if (!(ingredients.equals("")||ingredientsValue.equals("")||unit.equals(""))){
            itemsAdapter.add(ingredientsItem);
            mIngredientsValue.setText("");
        }else{
            Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus", Toast.LENGTH_LONG).show();
        }
    }

    //löscht Inhalsstoffe aus der ListView
    private void deleteIngredientsItems() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Toast.makeText(getApplicationContext(), items.get(position), Toast.LENGTH_LONG).show();
                //soll Inhaltstoff aus der Map löschen
                ingredients.remove(items.get(position).split("  ")[0]);
                ingredientsList.remove(items.get(position).split("  ")[0]);
                //ingredientsUnit.remove(items.get(position).split("  ")[0]);

                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(), "Inhaltsstoff aus der Liste gelöscht", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addFeedToFirestore(String feedName, String feedBrand,String productDescription,String composition,String feedingRecommendationDescription,Double feedAmout, Double feedPrice) {

        if(!(feedName.equals("")||feedBrand.equals("")||productDescription.equals("") ||composition.equals("")
                ||feedingRecommendationDescription.equals("") ||ingredients.isEmpty()||ingredients.containsKey("Unit")
                ||ingredients.containsKey("Value")||feedAmout.equals("") ||feedPrice.equals(""))){
            /**
             Map<String, Double> ingredients = new HashMap<String, Double>();
             ingredients.put(mIngredients.getSelectedItem().toString(), Double.parseDouble(mIngredientsValue.getText().toString().trim()));
             */

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = null;
            if (user !=null) {
                uid = user.getUid();
            }


            Feed feed = new Feed(feedName, feedBrand, productDescription, composition, feedingRecommendationDescription, ingredients, ingredientsList);
            Toast.makeText(getApplicationContext(), ingredients.toString(), Toast.LENGTH_LONG).show();
            String documentFeedName = feedName + "_" + feedBrand;

            HorseFeedHelpClass.addFeedNameToStringArray(documentFeedName);

            db.collection("Feed").document(documentFeedName).set(feed)
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

            FeedCosts feedCosts = new FeedCosts(feedName, feedBrand, feedAmout, feedPrice);
            String documentFeedCostsName = feedName + "_" + feedBrand;
            db.collection("FeedCosts").document(documentFeedCostsName).set(feedCosts)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddFeedActivity.this, "Futterkosten erfolgreich hochgeladen!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "addFeedCosts: success");
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
        }else{
            Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder für das Futter aus", Toast.LENGTH_LONG).show();
        }



    }

}
