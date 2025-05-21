package com.example.tltt_application.objects;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    private String pickupDate;
    private String pickupTime;
    private String returnDate;
    private String returnTime;
    private String city;
    private String carName;
    private String carImageUrl;
    private String userName;
    private String phone;
    private String method;
    private int status;
    private Date createdAt;
    private double totalPrice;
    private String userId;
    public Booking() {
    }

    // Constructor đầy đủ (bao gồm totalPrice)
    public Booking(String pickupDate, String pickupTime, String returnDate, String returnTime,
                   String city, String carName, String carImageUrl, String userName,
                   String phone, String method, int status, Date createdAt, double totalPrice, String userId) {
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
        this.city = city;
        this.carName = carName;
        this.carImageUrl = carImageUrl;
        this.userName = userName;
        this.phone = phone;
        this.method = method;
        this.status = status;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.userId = userId;
    }

    // Getters và Setters (bao gồm totalPrice)
    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarImageUrl() {
        return carImageUrl;
    }

    public void setCarImageUrl(String carImageUrl) {
        this.carImageUrl = carImageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}