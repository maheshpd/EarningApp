package com.createsapp.earningapp.model;

public class AmazonModel {
    private String id, amazonCode;

    public AmazonModel() {
    }

    public AmazonModel(String id, String amazonCode) {
        this.id = id;
        this.amazonCode = amazonCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmazonCode() {
        return amazonCode;
    }

    public void setAmazonCode(String amazonCode) {
        this.amazonCode = amazonCode;
    }
}
