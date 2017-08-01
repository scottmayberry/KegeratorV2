package com.example.sctma.kegeratorv1;

/**
 * Created by sctma on 7/2/2017.
 */

public class RFID {
    private String rfid;
    private String pushID;
    private String username;

    public RFID() {
    }
    public RFID(String rfid, String pushID, String username) {
        this.rfid = rfid;
        this.pushID = pushID;
        this.username = username;
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

    public String getRfid() {
        return rfid;
    }

    public void setRFID(String RFID) {
        this.rfid = RFID;
    }

    public String getPushID() {
        return pushID;
    }

    public void setPushID(String pushID) {
        this.pushID = pushID;
    }

    @Override
    public String toString() {
        return "RFID: " + rfid + "  PushID: " + pushID + "  Username: " + username;
    }
}
