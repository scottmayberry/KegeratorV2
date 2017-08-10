package com.example.sctma.kegeratorv1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

/**
 * Created by SMAYBER8 on 7/14/2017.
 */

public class Util {
    public static final int ADMIN_REQUEST = 1;
    public static final int POUR_REQUEST = 2;
    public static final int USER_INFO_REQUEST = 3;
    public static final int CONTACT_INFO_REQUEST = 4;
    public static final int IMAGE_SELECTION_REQUEST = 5;
    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static boolean isBound = false;

    public static Bitmap imageBitmaps[] = {null, null};


    static DatabaseReference ref;
    static Hashtable<String, User> userHashTable;
    static Hashtable<String, RFID> rfidHashTable;
    static Hashtable<String, Balance> balanceHashTable;
    static KegInfo kegInfo[] = new KegInfo[2];

    static User currentUser;
    static Balance currentBalance;

    static String savedBluetoothAddress;


    static Context mContext;

    static void writeToBluetooth(Context context, String info)
    {
        Intent intent = new Intent(context, BluetoothDataService.class);
        intent.putExtra("WRITE", info);
        context.startService(intent);
    }
    static void writeToBluetooth(Context context, int info)
    {
        Intent intent = new Intent(context, BluetoothDataService.class);
        intent.putExtra("WRITE", context.getString(info));
        context.startService(intent);
    }

}
