package com.example.tltt_application.objects;

import java.io.Serializable;

public class User implements Serializable{
    private String phone;

    private String name;

    private String password;
    private String confirmPassword;
    private String gender;
    private String GPLX;
    private String birth;
    public User() {
    }

    public User(String phone, String name, String password, String confirmPassword, String gender, String GPLX, String birth) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.gender = gender;
        this.GPLX = GPLX;
        this.birth = birth;
    }

    // Getter và Setter cho phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter và Setter cho confirmPassword
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGPLX() {
        return GPLX;
    }

    public void setGPLX(String GPLX) {
        this.GPLX = GPLX;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}