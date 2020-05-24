package com.example.mad_assigment;

import java.util.ArrayList;

public class patientModel{
    private int patientProfilepic;
    private String patientName;
    private ArrayList<patientModel> pmedicineList;
    public patientModel(int pic , String name ,ArrayList<patientModel> list ){
        this.patientProfilepic = pic;
        this.patientName = name;
        this.pmedicineList = list;
    }

    public int getPatientProfilepic() {
        return patientProfilepic;
    }

    public String getPatientName() {
        return patientName;
    }

    public ArrayList<patientModel> getPmedicineList() {
        return pmedicineList;
    }
}
