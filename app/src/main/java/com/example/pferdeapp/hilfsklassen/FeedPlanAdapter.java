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

public class FeedPlanAdapter extends ArrayAdapter<FeedPlanListModel> {

    private static final String TAG = "FeedPlanAdapter";

    private Context mContext;
    int mRessource;

    public FeedPlanAdapter(Context context, int resource, ArrayList<FeedPlanListModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mRessource =  resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Informationen der Inhaltsstoffe
        String brand = getItem(position).getBrand();
        String feedname = getItem(position).getFeedName();
        String ration = "";
        String feedID = "";

        if(getItem(position).getRation()==null && getItem(position).getFeedID()==null){
            FeedPlanListModel ingredients = new FeedPlanListModel(brand, feedname, ration, feedID);

        }else{
            ration = getItem(position).getRation();
            feedID = getItem(position).getFeedID();
            FeedPlanListModel ingredients = new FeedPlanListModel(brand, feedname, ration, feedID);
        }

        // Kreiere ein FeedPlanListModel Objekt mit diesen Informationen
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mRessource, parent, false);

        TextView tvBrand = (TextView) convertView.findViewById(R.id.txtitem);
        TextView tvFeedName = (TextView) convertView.findViewById(R.id.txtitem2);
        TextView tvRation = (TextView) convertView.findViewById(R.id.txtitem3);

        tvBrand.setText(brand);
        tvFeedName.setText(feedname);
        tvRation.setText(ration);

        return convertView;
    }
}
