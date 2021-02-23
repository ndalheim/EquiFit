package com.example.pferdeapp.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Spinner;
import android.widget.TextView;

import com.example.pferdeapp.Activities.AddFeedActivity;
import com.example.pferdeapp.Activities.FeedInformationActivity;
import com.example.pferdeapp.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class FeedFragment extends Fragment {
    private static final String TAG = "FeedFragment";

    private Button mAddFeedBtn, mSearchBtn;
    Spinner mSearchIngredientsSpinner, mSortSpinner;
    ListView feedListView;
    View sortedFeedLayout, sortedFeedLayoutScroll;
    TextView tvSortedSequence, tvSortedIngredient, tvSortedFeed, tvSortedFeedValue;
    private List<String> feedList = new ArrayList<>();
    FirebaseUser user;
    String uid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        mSearchIngredientsSpinner = (Spinner) rootView.findViewById(R.id.search_ingredients_spinner);
        ArrayAdapter<CharSequence> searchIngredientsSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.ingrements_array, android.R.layout.simple_spinner_item);
        searchIngredientsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSearchIngredientsSpinner.setAdapter(searchIngredientsSpinnerAdapter);

        mSortSpinner = (Spinner) rootView.findViewById(R.id.sort_spinner);
        ArrayAdapter<CharSequence> sortSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.search_array, android.R.layout.simple_spinner_item);
        sortSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortSpinner.setAdapter(sortSpinnerAdapter);

        feedListView = (ListView) rootView.findViewById(R.id.feed_list_view);

        // View der sortierten Liste nach den Werten der Inhaltsstoffe
        sortedFeedLayout = (View) rootView.findViewById(R.id.sorted_feed_layout);
        tvSortedSequence = (TextView) rootView.findViewById(R.id.sorted_sequence);
        tvSortedIngredient = (TextView) rootView.findViewById(R.id.sorted_ingredient);
        sortedFeedLayoutScroll = (View) rootView.findViewById(R.id.sorted_feed_layout_scroll);
        tvSortedFeed = (TextView) rootView.findViewById(R.id.sorted_feed);
        tvSortedFeedValue = (TextView) rootView.findViewById(R.id.sorted_feed_value);

        mAddFeedBtn = (Button) rootView.findViewById(R.id.add_feed_button);

        mAddFeedBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "goToAddHorseActivity: success");
                startActivity(new Intent(getActivity(), AddFeedActivity.class));
            }

        });

        mSearchBtn = (Button) rootView.findViewById(R.id.sort_button);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String filterIngredient = mSearchIngredientsSpinner.getSelectedItem().toString();
                String sort = mSortSpinner.getSelectedItem().toString();

                filterIngredient(filterIngredient, sort);
                sortedFeedLayout.setVisibility(View.VISIBLE);
                tvSortedSequence.setText(sort);
                tvSortedIngredient.setText(filterIngredient);

            }

        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();



        db.collection("Feed").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                feedList.clear();

                for (DocumentSnapshot snapshot : documentSnapshot){
                        feedList.add(snapshot.getString("name") + "_" + snapshot.get("brand"));

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_selectable_list_item, feedList);
                adapter.notifyDataSetChanged();
                feedListView.setAdapter(adapter);
            }
        });

        //
        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getActivity(), FeedInformationActivity.class);
                intent.putExtra("DocumentName", feedList.get(position));
                startActivity(intent);

            }
        });

        return rootView;
    }

    /**
     * Logout-Button OnClickListener:
     * If user click on the logout button he will be logout.
     */
    public void filterIngredient(final String mFilterIngredient, String mSort){
        feedList.clear();
        db.collection("Feed")
                .whereArrayContains("ingredientsList", mFilterIngredient)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot snapshot: snapshotList){
                            Log.d(TAG, "onSuccess: " + snapshot.getData());

                            sortByIngredient(mFilterIngredient, mSort);
                            //feedList.add(snapshot.getString("name") + "_" + snapshot.get("brand"));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_selectable_list_item, feedList);
                        adapter.notifyDataSetChanged();
                        feedListView.setAdapter(adapter);
                        //sortByIngredient(mFilterIngredient, mSort);
                    }
                });
    }

    public void sortByIngredient(final String ingredientName, String sort){
        db.collection("Feed").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                
                Map<String, String> ingredients;
                Map<String, Double> ingredientValues;
                Map<String, Double> ingredienTest = new HashMap<>();
                String sortedFeedId = "";
                String sortedFeedValue = "";

                feedList.clear();
                for (DocumentSnapshot snapshot : value) {
                        
                    // Schreibt alle Inhaltsstoffe aus der Datenbank in eine Map
                    ingredients = (Map<String, String>) snapshot.get("ingredients");


                    // läuft über alle Inhaltsstoffe eines Futters
                    for (Map.Entry e : ingredients.entrySet()){
                        // schreibt Einheit und Value des Inhaltsstoffes in eine Map
                        ingredientValues = (Map<String, Double>) e.getValue();

                        //schaut sich nur die Werte in der Map von dem Inhaltsstoff an der gerade ausgewählt ist
                        if(ingredientName.equals(e.getKey())){
                            // Läuft über zwei Elemente Einheit und Value
                            for(Map.Entry m : ingredientValues.entrySet()){


                                ingredienTest.put(snapshot.getId(), Double.parseDouble(m.getValue().toString()));
                                //feedList.add(snapshot.getString("name") + "_" + snapshot.get("brand"));
                            }

                            ingredienTest = sortByValue(ingredienTest, sort);
                            for (Map.Entry sortedmap: ingredienTest.entrySet()){
                                if (!(feedList.contains(snapshot.getId()))){
                                    feedList.add(snapshot.getId());

                                }

                            }
                        }
                    }

                }
                for (Map.Entry feed: ingredienTest.entrySet()){
                    sortedFeedId += feed.getKey().toString() + "\n";
                    sortedFeedValue += feed.getValue().toString() + "  \n";
                }


                tvSortedFeed.setText(sortedFeedId);
                tvSortedFeedValue.setText(sortedFeedValue);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_selectable_list_item, feedList);
                adapter.notifyDataSetChanged();
                feedListView.setAdapter(adapter);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, String sort) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        String sortedFeeds= "";
        //Liste nach Werten absteigend Sortieren
        if(sort.equals("absteigend")){
            Collections.reverse(list);
        }

        Map<K, V> result = new LinkedHashMap<>();

        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
            sortedFeeds += entry.getKey().toString() + ": " + entry.getValue().toString();
        }


        return result;
    }

}

