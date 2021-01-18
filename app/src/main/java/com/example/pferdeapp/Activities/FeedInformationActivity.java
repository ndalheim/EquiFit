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
    TextView textViewFeedName;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_information);

        textViewFeedName = findViewById(R.id.feedNameTextView);


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
                            String productDescription = document.getString("productDescription");
                            String composition = document.getString("composition");

                            textViewFeedName.setText("Name: " + futtername + "\n" +
                                    "Marke: " + brand + "\n" +
                                    "Produktbeschreibung: " + productDescription + "\n" +
                                    "Zusammensetzung: " + composition + "\n");


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
