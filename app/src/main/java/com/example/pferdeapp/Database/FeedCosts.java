package com.example.pferdeapp.Database;

public class FeedCosts {

    private String name;
    private String brand; //Marke
    private double price; //Preis
    private double amount; //Menge/Gewicht

    public FeedCosts(String name, String brand, double amount, double price) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.amount = amount;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
