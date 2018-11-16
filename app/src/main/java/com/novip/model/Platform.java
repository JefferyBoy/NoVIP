package com.novip.model;

import java.util.List;

public class Platform {

    private int id;
    private String picture_url;
    private String action_url;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getAction_url() {
        return action_url;
    }

    public void setAction_url(String action_url) {
        this.action_url = action_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
