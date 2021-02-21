package com.example.pferdeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pferdeapp.Database.Horse;
import com.google.android.gms.tasks.OnCompleteListener;

import com.example.pferdeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddHorseActivity extends AppCompatActivity{

    private static final String TAG = "AddHorseActivity";

    /**private String horseName; //Name
    private int horseHeight; //Stockmaß
    private int horseWeight; //Gewicht
    private String[] horseCondition; //Trainingszustand
    private String[] defect; //Mängel
    private String[] intolerance; //Intolerant/Allergie*/

    EditText mhorseName, mhorseHeight, mhorseWeight;
    Spinner mhorseCondition, mhorseDefect, mhorseIntolerance;
    Button mSaveHorseBtn, mBackBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String horseNameString, horseWeight, horseHigh, horseCondition, horseDefect, horseIntolerance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_horse);

        mhorseName = findViewById(R.id.horse_name);
        mhorseHeight = findViewById(R.id.horse_height);
        mhorseWeight = findViewById(R.id.horse_weight);

        mhorseCondition = (Spinner) findViewById(R.id.horse_condidion_spinner);
        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(this,
                R.array.condidion_array, android.R.layout.simple_spinner_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mhorseCondition.setAdapter(conditionAdapter);

        mhorseDefect = (Spinner) findViewById(R.id.horse_defect_spinner);
        ArrayAdapter<CharSequence> defectAdapter = ArrayAdapter.createFromResource(this,
                R.array.defects_array, android.R.layout.simple_spinner_item);
        defectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mhorseDefect.setAdapter(defectAdapter);

        mhorseIntolerance = (Spinner) findViewById(R.id.horse_intolerance_spinner);
        ArrayAdapter<CharSequence> intoleranceAdapter = ArrayAdapter.createFromResource(this,
                R.array.defects_array, android.R.layout.simple_spinner_item);
        intoleranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mhorseIntolerance.setAdapter(intoleranceAdapter);

        mSaveHorseBtn = findViewById(R.id.save_horse_information_button);
        mBackBtn = findViewById(R.id.back_to_FeedFragment_button);

        //Falls die View von edit horse aufgerufen wurde, werden die Informationen von dem Pferd in der Ansicht gesetzt
        if(getIntent().hasExtra("HorseName") == true) {
            horseNameString = getIntent().getExtras().getString("HorseName");
            horseWeight = getIntent().getExtras().getString("HorseWeight");
            horseHigh = getIntent().getExtras().getString("HorseHigh");
            horseCondition = getIntent().getExtras().getString("HorseCondition");
            horseDefect = getIntent().getExtras().getString("HorseDefect");
            horseIntolerance = getIntent().getExtras().getString("HorseIntolerance");


            mhorseName.setText(horseNameString);
            mhorseName.setFocusable(false);

            mhorseHeight.setText(horseHigh);
            mhorseWeight.setText(horseWeight);

            mhorseCondition.setSelection(conditionAdapter.getPosition(horseCondition));
            mhorseDefect.setSelection(defectAdapter.getPosition(horseDefect));
            mhorseIntolerance.setSelection(intoleranceAdapter.getPosition(horseIntolerance));

        }





        // Weiterleitung zur Main Activity
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        mSaveHorseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String horseName = mhorseName.getText().toString().trim();
                Double horseHeight = Double.parseDouble(mhorseHeight.getText().toString().trim());
                Double horseWeight = Double.parseDouble(mhorseWeight.getText().toString().trim());
                String horseCondition = mhorseCondition.getSelectedItem().toString();
                String horseDefect = mhorseDefect.getSelectedItem().toString();
                String horseIntolerance = mhorseIntolerance.getSelectedItem().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = null;
                if (user !=null) {
                    uid = user.getUid();
                }

                Horse horse = new Horse(horseName, horseHeight, horseWeight, horseCondition, horseDefect, horseIntolerance, uid);
                String documentName = horseName + "_" + uid;

                db.collection("user").document(uid).collection("Horse").document(horseName).set(horse)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddHorseActivity.this, "Pferd erfolgreich hochgeladen!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "addHorse: success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddHorseActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());

                            }
                        });

                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });




    }
    void setSpinner(Spinner spinner, ArrayAdapter<CharSequence> spinnerAdapter){
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);;
    }

}

