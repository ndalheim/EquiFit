package com.example.pferdeapp.hilfsklassen;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.pferdeapp.Fragments.HorseFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class DeleteDialog extends DialogFragment {


//public class DeleteDialog extends AppCompatDialogFragment {


    String horseName = "";
    String feedName = "";
    String feedFromHorse = "";

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedFromHorse() {
        return feedFromHorse;
    }

    public void setFeedFromHorse(String feedFromHorse) {
        this.feedFromHorse = feedFromHorse;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setTitle("Aus der Datenbank löschen")
                .setMessage("Möchtest du " + horseName + feedName +  " wirklich löschen?")
                .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();


                        if(!(horseName=="")){
                            FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
                            String uid2 = user2.getUid();
                            db.collection("user").document(uid2).collection("Horse").document(getHorseName())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });
                        }else if(!(feedName=="")){
                            FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
                            String uid2 = user2.getUid();
                            db.collection("user").document(uid2).collection("Horse").document(getFeedFromHorse()).collection("FeedPlan").document(feedName)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });

                        }

                    }
                })
                .setNegativeButton("Abbrechen",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                })
                .show();

        TextView textView = (TextView) builder.findViewById(android.R.id.message);
        textView.setTextSize(14);

        return builder;
    }
}
