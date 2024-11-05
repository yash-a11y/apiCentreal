package com.example.apiCentreal.rogers.models;



public class Phone {
    private String phoneName;
    private String price;
    private String condition;
    private String fullPrice;
    private String imageUrl;

    // Constructors, Getters, and Setters
    public Phone() {}

    public Phone(String phoneName, String price, String condition, String fullPrice, String imageUrl) {
        this.phoneName = phoneName;
        this.price = price;
        this.condition = condition;
        this.fullPrice = fullPrice;
        this.imageUrl = imageUrl;
    }
}
