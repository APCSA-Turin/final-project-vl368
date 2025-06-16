package com.example;

import java.util.ArrayList;

public class Product {
    private String name;
    private double price;
    private String url;
    private String email;
    private String imageUrl;
    private double targetPrice;
    private boolean alertSent;
    private ArrayList<Double> priceHistory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        priceHistory = new ArrayList<>();
    }

    public void setPrice(double price) {
        this.price = price;
        priceHistory.add(price);
    }

    public double getPrice() {
        return price;
    }

    public ArrayList<Double> getPriceHistory() {
        return priceHistory;
    }

    public double getOriginal() {
        return priceHistory.get(0).doubleValue();
    }

    public double getNewest() {
        return priceHistory.get(priceHistory.size() - 1).doubleValue();
    }
    
    public double getPriceDrop() {
        if (priceHistory.size() <= 1) {
            return 0;
        }
        
        double original = getOriginal();
        double newest = getNewest();

        return (1 - (newest / original)) * 100;
    }

    public void setTargetPrice(double target) {
        this.targetPrice = target;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAlertSent(boolean b) {
        alertSent = b;
    }

    public boolean isAlertSent() {
        return alertSent;
    }
}
