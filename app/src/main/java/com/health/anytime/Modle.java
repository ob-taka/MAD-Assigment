package com.health.anytime;

import android.view.Display;

public class Modle {
    private String title;
    private String description;
    private String dosage;
    private String time;
    private String unit;
    private boolean expanded; // set the state the constrain layout

    public Modle(){
    }

    public Modle(String title, String description, String dosage, String time){
        this.title = title;
        this.description = description;
        this.dosage = dosage;
        this.time = time;
        this.expanded = false; // sat the initial stat of the layout to be inevitable
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public String getUnit() {
        return unit;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDosage() {
        return dosage;
    }

    public String getTime() {
        return time;
    }
}
