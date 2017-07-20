package com.example.sctma.kegeratorv1;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by SMAYBER8 on 7/20/2017.
 */

public abstract class AbstractActivity extends AppCompatActivity {

    public BroadcastReceiver mMessageReceiver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmMessageReceiver();
        if(mMessageReceiver == null)
        {
            Toast.makeText(this, "RECEIVER NOT INSTANTIATE", Toast.LENGTH_SHORT);
            finish();
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("intentKey"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mMessageReceiver);
    }
    public abstract void setmMessageReceiver();

}
