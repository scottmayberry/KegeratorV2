package com.example.sctma.kegeratorv1;

/**
 * Created by sctma on 7/2/2017.
 */

public class User {
    private String name;
    private String rfid;
    private String username;
    private String classification;
    private String email;
    private boolean admin;

    public User() {
    }

    public User(String name, String rfid, String username, String classification, String email) {
        this.name = name;
        this.rfid = rfid;
        this.username = username;
        this.classification = classification;
        this.email = email;
        admin = false;
    }
    public User(String name, String rfid, String username, String classification, String email, boolean admin) {
        this.name = name;
        this.rfid = rfid;
        this.username = username;
        this.classification = classification;
        this.email = email;
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
