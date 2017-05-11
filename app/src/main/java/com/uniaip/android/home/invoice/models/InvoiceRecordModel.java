package com.uniaip.android.home.invoice.models;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class InvoiceRecordModel {
    private String img_url;
    private String type_name;
    private String time;
    private String money;
    private String point;
    private String state;
    private String info;

    public InvoiceRecordModel(String img_url, String type_name, String time, String money, String point, String state, String info) {
        this.img_url = img_url;
        this.type_name = type_name;
        this.time = time;
        this.money = money;
        this.point = point;
        this.state = state;
        this.info = info;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "InvoiceRecordModel{" +
                "img_url='" + img_url + '\'' +
                ", type_name='" + type_name + '\'' +
                ", time='" + time + '\'' +
                ", money='" + money + '\'' +
                ", point='" + point + '\'' +
                ", state='" + state + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
