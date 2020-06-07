package com.example.mad_assigment;

public class Modle {
    private String title;
    private String description;
    private String time;
    private String dosage;
    private String detailDes;
    private String food;
    private int img;
    private boolean expanded; // set the state the constrain layout

    public Modle(){}//needed for firebase

    public Modle(String title, String description, String time, String dosage, String detailDes, String img){
        this.title = title;
        this.description = description;
        this.time = time;
        this.dosage = dosage;
        this.detailDes = detailDes;
        this.img = img;
        this.expanded = false; // sat the initial stat of the layout to be inevitable
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

    public String getImg() {
        return img;
    }

}
