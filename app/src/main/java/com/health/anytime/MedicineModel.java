package com.health.anytime;

public class MedicineModel{
    private int medid;
    private String medicineTitle;
    private String medicineDsec;
    private String medicineImg;
    private double quantity;

    public MedicineModel(){

    }

    public MedicineModel(int id , String medicineTitle, String medicineImg , String medicineDsec , double qty) {
        this.medid = id;
        this.medicineTitle = medicineTitle;
        this.medicineImg = medicineImg;
        this.medicineDsec = medicineDsec;
        this.quantity = qty;
    }

    public String getMedicineDsec() {
        return medicineDsec;
    }

    public void setMedicineDsec(String medicineDsec) {
        this.medicineDsec = medicineDsec;
    }

    public int getMedid() {
        return medid;
    }

    public String getMedicineTitle() {
        return medicineTitle;
    }

    public void setMedicineTitle(String medicineTitle) {
        this.medicineTitle = medicineTitle;
    }

    public String getMedicineImg() {
        return medicineImg;
    }

    public void setMedicineImg(String medicineImg) {
        this.medicineImg = medicineImg;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
