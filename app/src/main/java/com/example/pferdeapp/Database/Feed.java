package com.example.pferdeapp.Database;

import java.util.Map;

public class Feed {

    private String name;
    private String brand; //Marke
    private String productDescription; //Produktbeschreibung
    private String composition; //Zusammensetzung
    //private Map<String,int[]> feedingRecommendation; //Fütterungsempfehlung als Faktor
    private String feedingRecommendationDescription; //Fütterungsempfehlung
    //private Map<String,Map<Double, String>> ingredients; //Inhaltsstoffe
    private Map<String, Double> ingredients; //Inhaltsstoffe
    private Map<String, String> ingredientsUnit; //Inhaltsstoffe

    public Feed() {
    }

    public Feed(String name, String brand, String productDescription, String composition, String feedingRecommendationDescription, Map<String,Double> ingredients, Map<String,String> ingredientsUnit) {
        this.name = name;
        this.brand = brand;
        this.productDescription = productDescription;
        this.composition = composition;
        this.feedingRecommendationDescription = feedingRecommendationDescription;
        this.ingredients = ingredients;
        this.ingredientsUnit = ingredientsUnit;
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

    public Map<String, Double> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Double> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, String> getIngredientsUnit() {
        return ingredientsUnit;
    }

    public void setIngredientsUnit(Map<String, String> ingredientsUnit) {
        this.ingredientsUnit = ingredientsUnit;
    }
}
