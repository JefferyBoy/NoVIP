package com.novip.model;


import java.util.Date;
import java.util.List;

public class VParser  {
    private String name;
    private String nick_name;
    private String url;
    private String note;
    private Date date;
    private String platform_ids;

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlatform_ids() {
        return platform_ids;
    }

    public void setPlatform_ids(String platform_ids) {
        this.platform_ids = platform_ids;
    }
}
