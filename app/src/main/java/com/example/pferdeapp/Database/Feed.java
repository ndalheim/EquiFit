package com.example.pferdeapp.Database;

import java.util.Map;

public class Feed {

    private String name;
    private String brand; //Marke
    private String productDescription; //Produktbeschreibung
    private String composition; //Zusammensetzung
    private String feedingRecommendationDescription; //Fütterungsempfehlung
    private Map<String, Object> ingredients; //Inhaltsstoffe
    //private Map<String,Object> feedingRecommendation; //Fütterungsempfehlung als Faktor

    public Feed() {
    }

    public Feed(String name, String brand, String productDescription, String composition, String feedingRecommendationDescription, Map<String, Object> ingredients) {
        this.name = name;
        this.brand = brand;
        this.productDescription = productDescription;
        this.composition = composition;
        this.feedingRecommendationDescription = feedingRecommendationDescription;
        this.ingredients = ingredients;
    }

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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getFeedingRecommendationDescription() {
        return feedingRecommendationDescription;
    }

    public void setFeedingRecommendationDescription(String feedingRecommendationDescription) {
        this.feedingRecommendationDescription = feedingRecommendationDescription;
    }

    public Map<String, Object> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Object> ingredients) {
        this.ingredients = ingredients;
    }
}
