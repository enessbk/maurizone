package com.example.maurizone.models;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderID;
    private String buyerID;
    private List<BasketItem> products;
    private double totalAmount;
    private String orderDate;
    private String status;

    // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    public Order() {
    }

    public Order(String orderID, String buyerID, List<BasketItem> products, double totalAmount, String orderDate, String status) {
        this.orderID = orderID;
        this.buyerID = buyerID;
        this.products = products;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public List<BasketItem> getProducts() {
        return products;
    }

    public void setProducts(List<BasketItem> products) {
        this.products = products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
