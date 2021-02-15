package com.example.pferdeapp.hilfsklassen;

import android.widget.ArrayAdapter;

public class IngredientsListModel {

    private String ingredientsName;
    private String value;
    private String range;
    private String info;
    private String color;



    public IngredientsListModel(String ingredientsName, String value, String range, String info, String color) {
        this.ingredientsName = ingredientsName;
        this.value = value;
        this.range = range;
        this.info = info;
        this.color = color;
    }

    public IngredientsListModel(String ingredientsName, String value, String range, String info) {
        this.ingredientsName = ingredientsName;
        this.value = value;
        this.range = range;
        this.info = info;
    }


    public IngredientsListModel(String ingredientsName, String value, String range) {
        this.ingredientsName = ingredientsName;
        this.value = value;
        this.range = range;
    }

    public IngredientsListModel(String ingredientsName, String value) {
        this.ingredientsName = ingredientsName;
        this.value = value;
    }

    public String getIngredientsName() {
        return ingredientsName;
    }

    public void setIngredientsName(String ingredientsName) {
        this.ingredientsName = ingredientsName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
