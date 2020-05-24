package com.example.mad_assigment;

public class medicineModel{
    private String medicineTitle;
    private int medicineImg;

    public medicineModel(String title , int src){
        medicineTitle = title;
        medicineImg = src;
    }

    public String getTitle() {
        return medicineTitle;
    }

    public void setTitle(String title) {
        this.medicineTitle = title;
    }

    public int getImg() {
        return medicineImg;
    }

    public void setImg(int src) {
        this.medicineImg = src;
    }
}
