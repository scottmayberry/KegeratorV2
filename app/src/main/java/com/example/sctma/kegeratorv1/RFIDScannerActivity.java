package com.example.sctma.kegeratorv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.sctma.kegeratorv1.Util.rfidHashTable;
import static com.example.sctma.kegeratorv1.Util.userHashTable;

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

    boolean cardReadStart;
    StringBuilder cardString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfidscanner);
        cardReadStart = false;
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
                if(message.contains("*")) {
                    cardReadStart = true;
                    cardString = new StringBuilder();
                }
                if(cardReadStart)
                {
                    cardString.append(message);
                    if(message.contains("#"))
                    {
                        for(int i = 0; i < cardString.length();i++)
                            if(cardString.charAt(i) >= '0' && cardString.charAt(i) <= '9' ) {
                                cardString.delete(0, i);
                                break;
                            }
                        for(int i = cardString.length()-1; i >= 0; i--)
                            if(cardString.charAt(i) >= '0' && cardString.charAt(i) <= '9' ) {
                                cardString.delete(i+1, cardString.length());
                                break;
                            }
                        cardReadStart = false;

                        //do something with the read ID
                        if(rfidHashTable.containsKey(cardString.toString()))
                        {
                            String tempID = rfidHashTable.get(cardString.toString()).getPushID();
                            User u = userHashTable.get(tempID);
                            Toast.makeText(getApplicationContext(), u.getName() + " already uses this card." , Toast.LENGTH_SHORT).show();
                            updateUI(ERROR);
                            return;
                        }
                        else
                        {
                            rfid = cardString.toString();
                            updateUI(ACCEPTED);
                        }//the card is not reco
                    }
                }
            }
        };
    }
}
