package com.example.mad_assigment;

public class Modle {
    private String title;
    private String description;
    private String time;
    private String dosage;
    private String detailDes;
    private int img;
    private boolean expanded;

    public Modle(String title, String description, String time, String dosage, String detailDes, int img){
        this.title = title;
        this.description = description;
        this.time = time;
        this.dosage = dosage;
        this.detailDes = detailDes;
        this.img = img;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
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

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDetailDes() {
        return detailDes;
    }

    public void setDetailDes(String detailDes) {
        this.detailDes = detailDes;
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
