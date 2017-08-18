package com.example.sctma.kegeratorv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import static com.example.sctma.kegeratorv1.Util.currentBalance;
import static com.example.sctma.kegeratorv1.Util.currentUser;
import static com.example.sctma.kegeratorv1.Util.kegInfo;
import static com.example.sctma.kegeratorv1.Util.ref;
import static com.example.sctma.kegeratorv1.Util.writeToBluetooth;
import static java.lang.Thread.sleep;

public class PourActivity extends AbstractActivity {

    private int kegPos;
    private double poured;
    private double cost;
    private TextView pouredText;
    private TextView costText;
    private TextView beersLeftText;
    private double beersLeft;
    Thread commThread;
    double balance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pour);
        kegPos = getIntent().getIntExtra("KegPos",0);
        poured = 0;
        cost = 0.0;
        balance = currentBalance.getBalance();
        pouredText = (TextView)findViewById(R.id.pouredText);
        costText = (TextView)findViewById(R.id.costText);
        beersLeftText = (TextView)findViewById(R.id.beersLeftText);
        beersLeft = kegInfo[kegPos].getBeersLeft();
        beersLeftText.setText("" + roundNumber(beersLeft));
        logOnUser();
        ((ImageView)findViewById(R.id.chosenKeg1Image)).setImageBitmap(Util.imageBitmaps[kegPos]);
        ((TextView)(findViewById(R.id.chosenStyle1Text))).setText(kegInfo[kegPos].getStyle());
        ((TextView)(findViewById(R.id.pricePerBeerText))).setText(kegInfo[kegPos]
                .getRoundedPricePerBeer());
        ((TextView)(findViewById(R.id.pricePerOunceText))).setText(kegInfo[kegPos]
                .getRoundedPricePerOunce());
        ((TextView)(findViewById(R.id.chosenName1Text))).setText(kegInfo[kegPos].getName());

        //addToPoured(12.1);
        updatePouredCostAndBeerLeftText();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

    }

    public void logOnUser()
    {
        ((LinearLayout)findViewById(R.id.loginLinearLayout)).setBackgroundColor(Color.GREEN);
        ((TextView)findViewById(R.id.nameText)).setText(currentUser.getName());
        ((TextView)findViewById(R.id.balanceText)).setText("" + balance);
    }

    @Override
    public void setmMessageReceiver() {
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                String message = intent.getStringExtra("key");
                //do something with string

                //addToPoured();

                //update user info and GUI
            }
        };
    }


    public void addToPoured(double d)
    {
        poured += d;
        cost += d*kegInfo[kegPos].getPricePerOunce();
        beersLeft -= d/12.0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.mContext = getApplicationContext();
        if(kegPos == 0)
            Util.writeToBluetooth(this, R.string.A_POUR_STATE);
        else
            Util.writeToBluetooth(this, R.string.B_POUR_STATE);
        commThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(1500);
                        Util.writeToBluetooth(getApplicationContext(), getString(R.string.BLUETOOTH_CONNECTED));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        commThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Util.writeToBluetooth(this, R.string.STANDBY_STATE);
        commThread.interrupt();
        ChargeItem chargeItem = new ChargeItem(cost, "Bought " + roundNumber(poured) + " ounces of " + kegInfo[kegPos].getName(), (new Date()).getTime(), currentUser.getUsername());
        String key1 = ref.child("Balances").child(currentUser.getUsername()).child("Charge").push().getKey();
        ref.child("Balances").child(currentUser.getUsername()).child("Charge").child(key1).setValue(chargeItem);
        finish();
    }

    public void updatePouredCostAndBeerLeftText()
    {
        pouredText.setText("" + roundNumber(poured));
        costText.setText("" + roundNumber(cost));
        beersLeftText.setText("" + beersLeft);
    }
    public double roundNumber(double d)
    {
        return (Math.round(d*100.0)/100.0);
    }

}
