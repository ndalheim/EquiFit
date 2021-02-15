package com.example.pferdeapp.hilfsklassen;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class IngredientsDialog extends AppCompatDialogFragment {

    String titel = "";
    String message = "";

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setTitle(titel)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();

        TextView textView = (TextView) builder.findViewById(android.R.id.message);
        textView.setTextSize(14);

        return builder;
    }
}
