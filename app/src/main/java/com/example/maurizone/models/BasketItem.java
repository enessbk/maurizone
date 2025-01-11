package com.example.maurizone.models;

import java.io.Serializable;

public class BasketItem implements Serializable {
    private String userId;
    private String productId;
    private String productName;
    private String productImageUrl;
    private double productPrice;
    private String productOption; // Example: color, size, etc.
    private int quantity;

    // Default constructor required for calls to DataSnapshot.getValue(BasketItem.class)
    public BasketItem() {
    }

    public BasketItem(String productId, String productName, String productImageUrl, double productPrice,  int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productOption = productOption;
        this.quantity = quantity;
    }
    public BasketItem(String productId, String productName, String productImageUrl, double productPrice, String productOption, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productOption = productOption;
        this.quantity = quantity;
    }

    public BasketItem(String userId, String productId, String productName, String productImageUrl, double productPrice, String productOption, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productOption = productOption;
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductOption() {
        return productOption;
    }

    public void setProductOption(String productOption) {
        this.productOption = productOption;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
