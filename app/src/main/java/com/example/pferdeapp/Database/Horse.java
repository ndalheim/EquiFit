package com.example.pferdeapp.Database;

public class Horse {

    private String horseName; //Name
    private int horseHeight; //Stockmaß
    private int horseWeight; //Gewicht
    private String horseCondition; //Trainingszustand
    private String defect; //Mängel
    private String intolerance; //Intolerant/Allergie
    private String uid; //Intolerant/Allergie


    public Horse() {
    }

    public Horse(String horseName, int horseHeight, int horseWeight, String horseCondition, String defect, String intolerance, String uid) {
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

    public int getHorseHeight() {
        return horseHeight;
    }

    public void setHorseHeight(int horseHeight) {
        this.horseHeight = horseHeight;
    }

    public int getHorseWeight() {
        return horseWeight;
    }

    public void setHorseWeight(int horseWeight) {
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
