package com.example.pferdeapp.hilfsklassen;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.pferdeapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class IngredientsAdapter extends ArrayAdapter<IngredientsListModel>{

    private static final String TAG = "IngredientsListAdapter";

    private ArrayList<IngredientsListModel> ingredients;
    private Context mContext;
    int mRessource;

    private static class ViewHolder {
        TextView tvIngredientsName;
        TextView tvValue;
        TextView tvRange;

    }

    public IngredientsAdapter(Context context, int resource, ArrayList<IngredientsListModel> objects) {
        super(context, R.layout.list_item, objects);
        this.mContext = context;
        this.mRessource =  resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        IngredientsListModel ingredientsListModel = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            viewHolder.tvIngredientsName = (TextView) convertView.findViewById(R.id.txtitem);
            viewHolder.tvValue = (TextView) convertView.findViewById(R.id.txtitem2);
            viewHolder.tvRange = (TextView) convertView.findViewById(R.id.txtitem3);

            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvIngredientsName.setText(ingredientsListModel.getIngredientsName());
        viewHolder.tvValue.setText(ingredientsListModel.getValue());
        viewHolder.tvRange.setText(ingredientsListModel.getRange());


        // Setzt die Farben von einem ListItem
        if(!(ingredientsListModel.getColor() ==null)){
            if (ingredientsListModel.getColor().equals("green")) {
                convertView.setBackgroundColor(Color.GREEN);
            }else if (ingredientsListModel.getColor().equals("red")) {
                convertView.setBackgroundColor(Color.RED);
            }else if (ingredientsListModel.getColor().equals("yellow")) {
                convertView.setBackgroundColor(Color.YELLOW);
            }else if (ingredientsListModel.getColor().equals("black")) {
                convertView.setBackgroundColor(Color.parseColor("#888888"));
            }
        }else{
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

}


