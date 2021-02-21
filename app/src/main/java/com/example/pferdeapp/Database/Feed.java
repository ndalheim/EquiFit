package com.example.pferdeapp.Database;

import java.util.List;
import java.util.Map;

public class Feed {

    private String name;
    private String brand; //Marke
    private String productDescription; //Produktbeschreibung
    private String composition; //Zusammensetzung
    private String feedingRecommendationDescription; //Fütterungsempfehlung
    private Map<String, Object> ingredients; //Inhaltsstoffe

    private List<String> ingredientsList;
    private Map<String, String> ingredientValues;
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

    public Feed(String name, String brand, String productDescription, String composition, String feedingRecommendationDescription, Map<String, Object> ingredients, List<String> ingredientsList) {
        this.name = name;
        this.brand = brand;
        this.productDescription = productDescription;
        this.composition = composition;
        this.feedingRecommendationDescription = feedingRecommendationDescription;
        this.ingredients = ingredients;
        this.ingredientsList = ingredientsList;
    }

    public Feed(String name, String brand, String productDescription, String composition, String feedingRecommendationDescription, Map<String, Object> ingredients, List<String> ingredientsList, Map<String, String> ingredientValues) {
        this.name = name;
        this.brand = brand;
        this.productDescription = productDescription;
        this.composition = composition;
        this.feedingRecommendationDescription = feedingRecommendationDescription;
        this.ingredients = ingredients;
        this.ingredientsList = ingredientsList;
        this.ingredientValues = ingredientValues;
    }

    public Map<String, String> getIngredientValues() {
        return ingredientValues;
    }

    public void setIngredientValues(Map<String, String> ingredientValues) {
        this.ingredientValues = ingredientValues;
    }

    public List<String> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<String> ingredientsList) {
        this.ingredientsList = ingredientsList;
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
