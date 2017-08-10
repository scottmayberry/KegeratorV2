package com.example.sctma.kegeratorv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static com.example.sctma.kegeratorv1.Util.balanceHashTable;
import static com.example.sctma.kegeratorv1.Util.rfidHashTable;
import static com.example.sctma.kegeratorv1.Util.userHashTable;

public class EditBluetoothActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bluetooth);
    }

    @Override
    public void setmMessageReceiver() {
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent

            }
        };
    }
}
