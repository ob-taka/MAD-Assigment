package com.example.mad_assigment;

public class Modle {
    private String title;
    private String description;
    private String time;
    private String dosage;
    private String detailDes;
    // private int img;
    private boolean expanded;

    public Modle(){}//needed for firebase

    public Modle(String title, String description, String time, String dosage, String detailDes/*, int img*/){
        this.title = title;
        this.description = description;
        this.time = time;
        this.dosage = dosage;
        this.detailDes = detailDes;
        // this.img = img;
        this.expanded = false;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getDosage() {
        return dosage;
    }

    public String getDetailDes() {
        return detailDes;
    }

    public String getDescription() {
        return description;
    }

    /*public int getImg() {
        return img;
    }*/

}
