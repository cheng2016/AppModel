package com.uniaip.android.home.invoice.models;


import java.io.Serializable;

/**
 * 作者: ysc
 * 时间: 2017/1/19
 */

public class CheckInfo implements Serializable {
    private String name;
    private String uid;
    private String address;
    private String city;
    private String phoneNum;
    private String postCode;
    private boolean isSelected;
    private double longitude;//经度
    private double latitude;//纬度


    public CheckInfo(String name, String city, String address, double longitude, double latitude) {
        this.name = name;
        this.city=city;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public CheckInfo(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public CheckInfo() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
