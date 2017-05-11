package com.uniaip.android.home.models;

/**
 * 作者: ysc
 * 时间: 2017/4/25
 */

public class ModularInfo {
    private String id;
    private String icon;
    private String name;

    public ModularInfo(String id, String icon, String name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
