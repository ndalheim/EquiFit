package com.example.pferdeapp.hilfsklassen;

import java.util.ArrayList;
import java.util.List;

public class HorseFeedHelpClass {

    private static ArrayList<String> feedNames = new ArrayList<>();


    public static void addFeedNameToStringArray(String feedID){
        feedNames.add(feedID);

    }

    public static ArrayList<String> getFeedNames() {
        return feedNames;
    }
}
