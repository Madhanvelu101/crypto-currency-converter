package com.cryptocurrency.converter.models;

import java.util.Map;

public class CoinPrice {
    private Map<String, Double> price;

    public Map<String, Double> getPrice() {
        return price;
    }

    public void setPrice(Map<String, Double> price) {
        this.price = price;
    }
}
