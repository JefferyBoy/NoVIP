package com.novip.model;

import java.util.Date;

public class User{

    private String phone;
    private String password;
    private Date vip_end;
    private int id;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getVip_end() {
        return vip_end;
    }

    public void setVip_end(Date vip_end) {
        this.vip_end = vip_end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
