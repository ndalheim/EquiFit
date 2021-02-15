package com.example.pferdeapp.hilfsklassen;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ChangeFeedplanDialog extends AppCompatDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        final EditText feedInGramm = new EditText(getContext());
        final EditText numberOfMeals = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Möchtest du die Rationsangaben ändern?")
                .setTitle("Rationsangaben ändern")
                .setView(feedInGramm)
                .setView(numberOfMeals);

        builder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //Editable YouEditTextValue = feedInGramm.getText();
                //OR
                //String YouEditTextValue = feedInGramm.getText().toString();
            }
        });

        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        builder.show();


        return null;

    }


}
