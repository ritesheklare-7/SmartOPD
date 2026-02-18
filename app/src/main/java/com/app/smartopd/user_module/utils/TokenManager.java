package com.app.smartopd.user_module.utils;

public class TokenManager {

    private static TokenManager instance;

    private boolean hasToken = false;
    private int myToken = -1;
    private int nowServing = 1;

    private String doctorName;
    private String speciality;

    private TokenManager() {}

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    // BOOK TOKEN
    public void bookToken(String doctor, String speciality) {
        this.hasToken = true;
        this.myToken = nowServing + 4; // simulated queue
        this.doctorName = doctor;
        this.speciality = speciality;
    }

    public boolean hasToken() {
        return hasToken;
    }

    public int getMyToken() {
        return myToken;
    }

    public int getNowServing() {
        return nowServing;
    }

    public void advanceQueue() {
        if (hasToken && nowServing < myToken) {
            nowServing++;
        }
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void clearToken() {
        hasToken = false;
        myToken = -1;
        doctorName = null;
        speciality = null;
    }
}
