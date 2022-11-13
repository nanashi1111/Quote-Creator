package com.burhanrashid52.imageeditor.models;

/**
 * Created by admin on 8/18/18.
 */

public class Category {

    int resId;
    String name;

    public Category(int resId, String name) {
        this.resId = resId;
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
