package com.app.smartopd.user_module;

public class UserHomeModel {

    private String name;
    private String speciality;

    // Required constructor
    public UserHomeModel(String name, String speciality) {
        this.name = name;
        this.speciality = speciality;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getSpeciality() {
        return speciality;
    }
}
