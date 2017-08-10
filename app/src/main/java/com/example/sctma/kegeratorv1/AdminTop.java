package com.example.sctma.kegeratorv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class AdminTop extends AbstractActivity {

    CardView user;
    CardView keg;
    Timer logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_top);
        user = (CardView)(findViewById(R.id.userAdminTopCardView));
        keg = (CardView)(findViewById(R.id.beerAdminTopCardView));
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user on click listener
                Intent intent = new Intent(getApplicationContext(), UserAdmin.class);
                startActivity(intent);
            }
        });
        keg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //keg on click listener
                Intent intent = new Intent(getApplicationContext(), BeerAdmin.class);
                startActivity(intent);
            }
        });

        logout = new Timer();
        logout.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 15000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.adminmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.bluetoothMenuItem:
                break;
            case R.id.newClassYearMenuItem:

                break;
            default:
        }


        return super.onOptionsItemSelected(item);
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
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.mContext = getApplicationContext();
        Util.writeToBluetooth(this, R.string.STANDBY_STATE);

    }
}
