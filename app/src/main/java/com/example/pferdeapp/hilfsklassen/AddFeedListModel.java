package com.example.pferdeapp.hilfsklassen;

public class AddFeedListModel {
    private String brand;
    private String feedName;
    private String feedID;

    public AddFeedListModel(String brand, String feedName, String feedID) {
        this.brand = brand;
        this.feedName = feedName;
        this.feedID = feedID;
    }

    public AddFeedListModel(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }
}
