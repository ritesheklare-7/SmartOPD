package com.app.smartopd.user_module.Models;

public class UserHomeModel {

    private String name;
    private String speciality;
    private boolean isAvailable;
    private boolean sessionFull;

    public UserHomeModel() {
        // Required for Firebase
    }

    public UserHomeModel(String name, String speciality,
                         boolean isAvailable, boolean sessionFull) {
        this.name = name;
        this.speciality = speciality;
        this.isAvailable = isAvailable;
        this.sessionFull = sessionFull;
    }

    public String getName() {
        return name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isSessionFull() {
        return sessionFull;
    }
}
