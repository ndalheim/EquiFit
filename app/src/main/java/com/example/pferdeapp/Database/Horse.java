package com.example.pferdeapp.Database;

public class Horse {

    private String horseName; //Name
    private Double horseHeight; //Stockmaß
    private Double horseWeight; //Gewicht
    private String horseCondition; //Trainingszustand
    private String defect; //Mängel
    private String intolerance; //Intolerant/Allergie
    private String uid; //User-ID


    public Horse() {
    }

    public Horse(String horseName, Double horseHeight, Double horseWeight, String horseCondition, String defect, String intolerance, String uid) {
        this.horseName = horseName;
        this.horseHeight = horseHeight;
        this.horseWeight = horseWeight;
        this.horseCondition = horseCondition;
        this.defect = defect;
        this.intolerance = intolerance;
        this.uid = uid;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public Double getHorseHeight() {
        return horseHeight;
    }

    public void setHorseHeight(Double horseHeight) {
        this.horseHeight = horseHeight;
    }

    public Double getHorseWeight() {
        return horseWeight;
    }

    public void setHorseWeight(Double horseWeight) {
        this.horseWeight = horseWeight;
    }

    public String getHorseCondition() {
        return horseCondition;
    }

    public void setHorseCondition(String horseCondition) {
        this.horseCondition = horseCondition;
    }

    public String getDefect() {
        return defect;
    }

    public void setDefect(String defect) {
        this.defect = defect;
    }

    public String getIntolerance() {
        return intolerance;
    }

    public void setIntolerance(String intolerance) {
        this.intolerance = intolerance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
