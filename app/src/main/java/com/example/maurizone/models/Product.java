package com.example.maurizone.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Product implements Serializable {
    private String productID;
    private String sellerID;
    private String name;
    private String description;
    private double price;
    private String category;
    private List<String> images;
    private int stockQuantity;

    // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    public Product() {
    }

    public Product(String productID, String sellerID, String name, String description, double price, String category, List<String> images, int stockQuantity) {
        this.productID = productID;
        this.sellerID = sellerID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.images = images;
        this.stockQuantity = stockQuantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    private List<String> availableColors = Arrays.asList("only color");; // Assuming colors are represented as strings

    public List<String> getAvailableColors() {
        return availableColors;
    }
}
