package com.example.sctma.kegeratorv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.sctma.kegeratorv1.Util.userHashTable;

public class ContactInfo extends AbstractActivity {

    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        ll = (LinearLayout)findViewById(R.id.currentAdminLinearLayout);


        for(String keys: userHashTable.keySet())
        {
            if(userHashTable.get(keys).isAdmin())
            {
                TextView t = new TextView(this);
                t.setText(userHashTable.get(keys).getName());
                t.setGravity(View.TEXT_ALIGNMENT_CENTER);
                ll.addView(t);
            }
        }
    }

    @Override
    public void setmMessageReceiver() {
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                String message = intent.getStringExtra("key");
                //do something with string
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.mContext = getApplicationContext();
        Util.writeToBluetooth(this, R.string.STANDBY_STATE);
    }
}//Contact Info
