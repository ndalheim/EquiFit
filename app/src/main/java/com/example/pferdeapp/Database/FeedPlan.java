package com.example.pferdeapp.Database;

import java.util.Map;

public class FeedPlan {

    String feedId;
    Double numberOfMeals;
    Double feedInGram;

    public FeedPlan() {
    }

    public FeedPlan(String feedId, Double numberOfMeals, Double feedInGram) {
        this.feedId = feedId;
        this.numberOfMeals = numberOfMeals;
        this.feedInGram = feedInGram;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public Double getNumberOfMeals() {
        return numberOfMeals;
    }

    public void setNumberOfMeals(Double numberOfMeals) {
        this.numberOfMeals = numberOfMeals;
    }

    public Double getFeedInGram() {
        return feedInGram;
    }

    public void setFeedInGram(Double feedInGram) {
        this.feedInGram = feedInGram;
    }
}
