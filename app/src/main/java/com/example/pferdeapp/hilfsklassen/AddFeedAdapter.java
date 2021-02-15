package com.example.pferdeapp.hilfsklassen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pferdeapp.R;

import java.util.ArrayList;

public class AddFeedAdapter extends ArrayAdapter<AddFeedListModel> {

    private static final String TAG = "FeedPlanAdapter";

    private Context mContext;
    int mRessource;

    public AddFeedAdapter(Context context, int resource, ArrayList<AddFeedListModel> objects) {
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
        String feedID = getItem(position).getFeedID();
        String ration = "";
        // Kreiere ein FeedPlanListModel Objekt mit diesen Informationen
        AddFeedListModel ingredients = new AddFeedListModel(brand, feedname, feedID);

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


