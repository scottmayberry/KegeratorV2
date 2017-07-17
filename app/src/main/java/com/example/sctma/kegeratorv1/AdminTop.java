package com.example.sctma.kegeratorv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class AdminTop extends AppCompatActivity {

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
    protected void onResume() {
        super.onResume();
        Util.mContext = getApplicationContext();
    }
}
