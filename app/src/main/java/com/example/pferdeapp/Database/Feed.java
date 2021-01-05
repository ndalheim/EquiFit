package com.example.pferdeapp.Database;

import java.util.Map;

public class Feed {

    private String name;
    private String brand; //Marke
    private String productDescription; //Produktbeschreibung
    private String[] composition; //Zusammensetzung
    private Map<String,Integer> ingredients; //Inhaltsstoffe
    private Map<String,int[]> feedingRecommendation; //Fütterungsempfehlung als Faktor
    private String feedingRecommendationDescription; //Fütterungsempfehlung


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProdictDescription() {
        return productDescription;
    }

    public void setProdictDescription(String prodictDescription) {
        this.productDescription = prodictDescription;
    }

    public String[] getComposition() {
        return composition;
    }

    public void setComposition(String[] composition) {
        this.composition = composition;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, int[]> getFeedingRrcommendation() {
        return feedingRecommendation;
    }

    public void setFeedingRrcommendation(Map<String, int[]> feedingRrcommendation) {
        this.feedingRecommendation = feedingRrcommendation;
    }

    public String getFeedingRrcommendationDescription() {
        return feedingRecommendationDescription;
    }

    public void setFeedingRrcommendationDescription(String feedingRrcommendationDescription) {
        this.feedingRecommendationDescription = feedingRrcommendationDescription;
    }
}
