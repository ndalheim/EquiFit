package com.example.pferdeapp.Database;

public class Horse {

    private String horseName; //Name
    private int horseHeight; //Stockmaß
    private int horseWeight; //Gewicht
    private String[] horseCondition; //Trainingszustand
    private String[] defect; //Mängel
    private String[] intolerance; //Intolerant/Allergie



    public String getHorseName() {
        return horseName;
    }

    public int getHorseHeight() {
        return horseHeight;
    }

    public int getHorseWeight() {
        return horseWeight;
    }

    public String[] getHorseCondition() {
        return horseCondition;
    }

    public String[] getDefect() {
        return defect;
    }

    public String[] getIntolerance() {
        return intolerance;
    }



    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public void setHorseHeight(int horseHeight) {
        this.horseHeight = horseHeight;
    }

    public void setHorseWeight(int horseWeight) {
        this.horseWeight = horseWeight;
    }

    public void setHorseCondition(String[] horseCondition) {
        this.horseCondition = horseCondition;
    }

    public void setDefect(String[] defect) {
        this.defect = defect;
    }

    public void setIntolerance(String[] intolerance) {
        this.intolerance = intolerance;
    }
}
