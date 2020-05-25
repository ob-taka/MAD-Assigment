package com.example.mad_assigment;

import java.util.ArrayList;

public class PatientModel{
  private int patientProfilepic;
  private String patientName;

  public PatientModel(int pic, String name) {
    this.patientProfilepic = pic;
    this.patientName = name;
  }

  public int getPatientProfilepic() {
    return patientProfilepic;
  }

  public String getPatientName() {
    return patientName;
  }
}
