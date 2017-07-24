package com.example.sctma.kegeratorv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class RFIDScannerActivity extends AbstractActivity {

    private ImageView checkView;
    private TextView scanView;
    private TextView errorView;
    private TextView scanAgainView;
    private final int ERROR = 1;
    private final int SCAN = 2;
    private final int ACCEPTED = 3;
    private final int COMPLETESCAN = 5;
    private String rfid;
    private boolean checkID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfidscanner);
        checkID = getIntent().getBooleanExtra("CHECKID", false);
        checkView = (ImageView)findViewById(R.id.checkmarkImageView);
        scanView = (TextView)findViewById(R.id.scanRequestText);
        errorView = (TextView)findViewById(R.id.badscanTextView);
        scanAgainView = (TextView)findViewById(R.id.scanAgainText);
        updateUI(SCAN);
    }
    public void updateUI(int mode)
    {
        switch(mode)
        {
            case ERROR:
                checkView.setImageDrawable(getDrawable(R.drawable.redx));
                checkView.setVisibility(View.VISIBLE);
                scanView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                scanAgainView.setVisibility(View.VISIBLE);
                break;
            case SCAN:
                checkView.setVisibility(View.GONE);
                scanView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                scanAgainView.setVisibility(View.GONE);
                break;
            case ACCEPTED:
                checkView.setImageDrawable(getDrawable(R.drawable.checkmark));
                checkView.setVisibility(View.VISIBLE);
                scanView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                scanAgainView.setVisibility(View.GONE);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("RFID", rfid);
                        setResult(COMPLETESCAN, intent);
                        finish();
                    }
                }, 2000L);
                break;
            default: return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.writeToBluetooth(this, R.string.RFID_STATE);
    }


    @Override
    public void setmMessageReceiver() {
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                String message = intent.getStringExtra("key");
                //do something with string
                if(checkID)//checkID against database
                {

                }
                else
                {
                    if(message.substring(0,2).equals("$%"))
                    {
                        rfid = message.substring(2, message.length()-2);
                        updateUI(ACCEPTED);
                    }
                    else
                        updateUI(ERROR);
                }//grab scan without checking

            }
        };
    }
}
