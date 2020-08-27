package com.createsapp.earningapp.model;

public class ProfileModel {
    private String name, email, image;
    private int coins;

    public ProfileModel() {
    }

    public ProfileModel(String name, String email, String image, int coins) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.coins = coins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
