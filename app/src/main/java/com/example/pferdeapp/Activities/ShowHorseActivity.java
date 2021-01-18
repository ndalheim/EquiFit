package com.example.pferdeapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowHorseActivity extends AppCompatActivity {
    private static final String TAG = "ShowHorseActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String horseNameString;
    TextView textViewHorseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_show_horse);

        textViewHorseName = findViewById(R.id.horseInformationTextView);


        if(getIntent().hasExtra("HorseName") == true) {
            horseNameString = getIntent().getExtras().getString("HorseName");

            Log.d(TAG, horseNameString);

            db.collection("Horse").document(horseNameString).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String horseName = document.getString("horseName");
                            double helpValue = document.getDouble("horseHeight");
                            String horseHeight = String.valueOf((int) helpValue);
                            double helpValue2 = document.getDouble("horseWeight");
                            String horseWeight = String.valueOf((int) helpValue2);
                            String horseCondition = document.getString("horseCondition");
                            String defect = document.getString("defect");
                            String intolerance = document.getString("intolerance");

                            textViewHorseName.setText("Name: " + horseName + "\n"
                                    + "Stockmaß: " + horseHeight + " cm" +"\n"
                                    + "Gewicht: " + horseWeight + " kg" + "\n"
                                    + "Trainingszustand: " + horseCondition + "\n"
                                    + "Mangel: " + defect + "\n"
                                    + "Intoleranz: " + intolerance + "\n");


                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Toast.makeText(ShowHorseActivity.this, "Dokument existiert nicht", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Toast.makeText(ShowHorseActivity.this, "task fehlgeschlagen", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }else{
            Toast.makeText(ShowHorseActivity.this, "Daten wurden nicht übergeben", Toast.LENGTH_SHORT).show();
        }
    }
}
