package com.example.mad_assigment;

public class Modle {
    private String title;
    private String description;
    private String time;
    private int img;

    public Modle(){

    }

    public Modle(String t , String d , String time , int i){
        this.title = t;
        this.description = d;
        this.time = time;
        this.img = i;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }


}
