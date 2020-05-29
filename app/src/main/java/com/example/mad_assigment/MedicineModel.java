package com.example.mad_assigment;

public class MedicineModel{
    private String medicineTitle;
    private String medicineDesc;
    private String dailyIntakeTime;
    private int medicineImg;

    public MedicineModel(){

    }
    // constructor used in AddPatient and ViewPatient activity (visible to doctor only)
    public MedicineModel(String title , int src){
        medicineTitle = title;
        medicineImg = src;
    }

    // constructor used in --- activity (visible to Patient only)
    public MedicineModel(String medicineTitle, String medicineDesc, String dailyIntakeTime, int medicineImg) {
        this.medicineTitle = medicineTitle;
        this.medicineDesc = medicineDesc;
        this.dailyIntakeTime = dailyIntakeTime;
        this.medicineImg = medicineImg;
    }

    public String getMedicineTitle() {
        return medicineTitle;
    }

    public void setMedicineTitle(String medicineTitle) {
        this.medicineTitle = medicineTitle;
    }

    public String getMedicineDesc() {
        return medicineDesc;
    }

    public void setMedicineDesc(String medicineDesc) {
        this.medicineDesc = medicineDesc;
    }

    public String getDailyIntakeTime() {
        return dailyIntakeTime;
    }

    public void setDailyIntakeTime(String dailyIntakeTime) {
        this.dailyIntakeTime = dailyIntakeTime;
    }

    public int getMedicineImg() {
        return medicineImg;
    }

    public void setMedicineImg(int medicineImg) {
        this.medicineImg = medicineImg;
    }
}
