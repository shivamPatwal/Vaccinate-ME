package com.example.bottonnaviagtion.ui.home;


public class vaccine {
    String CentreName;
    String Address;
    String Paid;
    int MinAge;
    int Dose1;
    int Dose2;
    String fee;
    String Vaccine;
    boolean availDose1 = false;
    boolean availDose2 = false;

    public vaccine(String name, String address, String fee_type, int available_capacity_dose1, int available_capacity_dose2, int min_age_limit, String vaccine) {
        CentreName = name;
        Address = address;
        Paid = fee_type;
        Dose1 = available_capacity_dose1;
        Dose2 = available_capacity_dose2;
        MinAge = min_age_limit;
        Vaccine = vaccine;
        if (available_capacity_dose1 != 0) {
            availDose1 = true;
        }
        if (available_capacity_dose2 != 0) {
            availDose2 = true;
        }
    }

    public boolean getavailDose1() {
        return availDose1;
    }

    public boolean getavailDose2() {
        return availDose2;
    }

    public int getDose1() {
        return Dose1;
    }

    public int getDose2() {
        return Dose2;
    }

    public int getMinAge() {
        return MinAge;
    }

    public String getAddress() {
        return Address;
    }

    public String getCentreName() {
        return CentreName;
    }

    public String getFee() {
        return fee;
    }

    public String getPaid() {
        return Paid;
    }

    public String getVaccine() {
        return Vaccine;
    }


}
