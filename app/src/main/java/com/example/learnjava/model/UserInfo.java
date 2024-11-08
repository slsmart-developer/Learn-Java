package com.example.learnjava.model;

import android.widget.EditText;

public class UserInfo {

    private String email;
    private String fname;
    private String lname;
    private String mobile;

    public UserInfo(String email, String name, String nic, String mobile, String postcode) {
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
    }

    public UserInfo() {
    }

    public UserInfo(String email, String fname, String lname, String mobileno) {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


}
