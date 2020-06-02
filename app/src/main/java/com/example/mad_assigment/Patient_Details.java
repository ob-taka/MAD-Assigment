package com.example.mad_assigment;

public class Patient_Details {
    private String name;
    private String email;
    private String phone;
    private int img;

    public Patient_Details(){}

    public Patient_Details(String n,String e,String p,int i){
        this.name = n;
        this.email = e;
        this.phone = p;
        this.img = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
