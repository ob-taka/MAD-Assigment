package com.example.mad_assigment;

import java.util.ArrayList;

public class PatientModel{
  private int patientProfilepic;
  private String patientName;
  private String patientEmail;
  private boolean status;

  public PatientModel(){

  }

  public PatientModel(int pic, String name , String email , boolean status) {
    this.patientProfilepic = pic;
    this.patientName = name;
    this.patientEmail = email;
    this.status = status;
  }

  public int getPatientProfilepic() {
    return patientProfilepic;
  }
  public void setPatientProfilepic(int patientProfilepic) {
    this.patientProfilepic = patientProfilepic;
  }
  public String getPatientName() {
    return patientName;
  }
  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public String getPatientEmail() {
    return patientEmail;
  }
  public void setPatientEmail(String patientEmail) {
    this.patientEmail = patientEmail;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

}
