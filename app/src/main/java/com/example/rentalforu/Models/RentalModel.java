package com.example.rentalforu.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class RentalModel {
    int referenceNo;

    String rentalProfile;
    String propertyType;
    String bedroomsType;
    String date;
    String price;
    String furnitureType;
    String remarks;
    String reporterName;

    String reporterEmail;

    String reporterProfile;

    public RentalModel(){}
    public RentalModel(int referenceNo, String propertyType, String bedroomsType, String date, String price, String furnitureType, String remarks, String reporterName) {
        this.referenceNo = referenceNo;
        this.propertyType = propertyType;
        this.bedroomsType = bedroomsType;
        this.date = date;
        this.price = price;
        this.furnitureType = furnitureType;
        this.remarks = remarks;
        this.reporterName = reporterName;
    }

    public RentalModel(int referenceNo, String rentalProfile, String propertyType, String bedroomsType, String date, String price, String furnitureType, String remarks, String reporterName, String reporterEmail, String reporterProfile) {
        this.referenceNo = referenceNo;
        this.rentalProfile = rentalProfile;
        this.propertyType = propertyType;
        this.bedroomsType = bedroomsType;
        this.date = date;
        this.price = price;
        this.furnitureType = furnitureType;
        this.remarks = remarks;
        this.reporterName = reporterName;
        this.reporterEmail = reporterEmail;
        this.reporterProfile = reporterProfile;
    }



    public int getReferenceNo() {
        return referenceNo;
    }

    public String getRentalProfile(){ return rentalProfile;}
    public String getPropertyType() {
        return propertyType;
    }

    public String getBedroomsType() {
        return bedroomsType;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String getFurnitureType() {
        return furnitureType;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getReporterName() {
        return reporterName;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public String getReporterProfile() {
        return reporterProfile;
    }
}
