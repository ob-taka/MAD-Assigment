package com.example.mad_assigment;

import android.net.Uri;

import java.net.URL;
import java.util.ArrayList;

public class PatientModel{
  private String patientProfilepic;
  private String patientName;
  private String patientEmail;
  private boolean status;
  private String role;

  public PatientModel(){

  }

  public PatientModel(String pic, String name , String email , boolean status) {
    this.patientProfilepic = pic;
    this.patientName = name;
    this.patientEmail = email;
    this.status = status;
  }

  public String getPatientProfilepic() {
    return patientProfilepic;
  }

  public void setPatientProfilepic(String patientProfilepic) {
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

  public String getRole() {
    return role;
  }

}
