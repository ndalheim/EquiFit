package com.example.pferdeapp.hilfsklassen;

public class FeedPlanListModel {

    private String brand;
    private String feedName;
    private String ration;
    private String feedID;

    public FeedPlanListModel(String brand, String feedName, String ration, String feedID) {
        this.brand = brand;
        this.feedName = feedName;
        this.ration = ration;
        this.feedID = feedID;
    }

    public FeedPlanListModel(String brand, String feedName) {
        this.brand = brand;
        this.feedName = feedName;
    }

    public FeedPlanListModel(String brand) {
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

    public String getRation() {
        return ration;
    }

    public void setRation(String ration) {
        this.ration = ration;
    }

    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }
}
