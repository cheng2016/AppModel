package com.uniaip.android.home.invoice.models;

import android.text.TextUtils;

/**
 * 作者: ysc
 * 时间: 2017/2/13
 */

public class ConsumeTypeInfo {
    private String id;
    private int type;
    private String name;
    private String least;
    private String maximum;

    public ConsumeTypeInfo(String id, int type, String name, String least, String maximum) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.least = least;
        this.maximum = maximum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeast() {
        return least;
    }

    public void setLeast(String least) {
        this.least = least;
    }

    public String getMaximum() {
        if (null != maximum && !TextUtils.equals(maximum, "")) {
            return maximum;
        } else {
            return "3000";
        }

    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
