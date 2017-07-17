package com.example.sctma.kegeratorv1;

/**
 * Created by sctma on 7/2/2017.
 */

public class RFID {
    private String rfid;
    private String pushID;

    public RFID() {
    }
    public RFID(String rfid, String pushID) {
        this.rfid = rfid;
        this.pushID = pushID;
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
}
