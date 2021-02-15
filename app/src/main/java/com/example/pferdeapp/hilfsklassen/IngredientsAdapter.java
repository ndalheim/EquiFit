package com.example.pferdeapp.hilfsklassen;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pferdeapp.R;

import java.util.ArrayList;


public class IngredientsAdapter extends ArrayAdapter<IngredientsListModel> {

    private static final String TAG = "IngredientsListAdapter";

    private Context mContext;
    int mRessource;

    public IngredientsAdapter(Context context, int resource, ArrayList<IngredientsListModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mRessource =  resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Informationen der Inhaltsstoffe
        String name = getItem(position).getIngredientsName();
        String val = getItem(position).getValue();
        String range = getItem(position).getRange();
        String color = getItem(position).getColor();

        // Kreiere ein IngredientsListModel Objekt mit diesen Informationen
        IngredientsListModel ingredients = new IngredientsListModel(name, val, range);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mRessource, parent, false);

        TextView tvIngredientsName = (TextView) convertView.findViewById(R.id.txtitem);
        TextView tvValue = (TextView) convertView.findViewById(R.id.txtitem2);
        TextView tvRange = (TextView) convertView.findViewById(R.id.txtitem3);

        tvIngredientsName.setText(name);
        tvValue.setText(val);
        tvRange.setText(range);

        if(!(color ==null)){
            if (color.equals("green")) {
                convertView.setBackgroundColor(Color.GREEN);
            }else if (color.equals("red")) {
                convertView.setBackgroundColor(Color.RED);
            }else if (color.equals("yellow")) {
                convertView.setBackgroundColor(Color.YELLOW);
            }else if (color.equals("black")) {
                convertView.setBackgroundColor(Color.parseColor("#888888"));
            }
        }else{
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }



        return convertView;
    }
}


