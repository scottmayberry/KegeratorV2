package com.example.sctma.kegeratorv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.sctma.kegeratorv1.Util.ADMIN_REQUEST;
import static com.example.sctma.kegeratorv1.Util.balanceHashTable;
import static com.example.sctma.kegeratorv1.Util.currentBalance;
import static com.example.sctma.kegeratorv1.Util.currentUser;
import static com.example.sctma.kegeratorv1.Util.kegInfo;
import static com.example.sctma.kegeratorv1.Util.mContext;
import static com.example.sctma.kegeratorv1.Util.ref;
import static com.example.sctma.kegeratorv1.Util.rfidHashTable;
import static com.example.sctma.kegeratorv1.Util.savedBluetoothAddress;
import static com.example.sctma.kegeratorv1.Util.userHashTable;
import static com.example.sctma.kegeratorv1.Util.writeToBluetooth;
import static java.lang.Thread.sleep;

public class MainActivity extends AbstractActivity {


    CardView keg1;
    CardView keg2;

    Timer timer;

    boolean cardReadStart;
    StringBuilder cardString;

    ChildEventListener userListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            User user = new User((String)dataSnapshot.child("name").getValue(),
                    (String) dataSnapshot.child("rfid").getValue(),
                    (String) dataSnapshot.child("username").getValue(),
                    (String) dataSnapshot.child("classification").getValue(),
                    (String) dataSnapshot.child("email").getValue(),
                    (boolean) dataSnapshot.child("admin").getValue(),
                    (String) key);
            userHashTable.put(key, user);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            User user = userHashTable.get(dataSnapshot.getKey());
            user.setName((String)dataSnapshot.child("name").getValue());
            user.setRfid((String) dataSnapshot.child("rfid").getValue());
            user.setUsername((String) dataSnapshot.child("username").getValue());
            user.setClassification((String) dataSnapshot.child("classification").getValue());
            user.setEmail((String) dataSnapshot.child("email").getValue());
            user.setAdmin((boolean) dataSnapshot.child("admin").getValue());
            user.setPushID((String) dataSnapshot.getKey());
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            userHashTable.remove(dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ChildEventListener rfidListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            rfidHashTable.put(key,
                    new RFID((String) dataSnapshot.child("rfid").getValue(),
                            (String) dataSnapshot.child("pushID").getValue(),
                            (String) dataSnapshot.child("username").getValue()));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            RFID user = rfidHashTable.get(dataSnapshot.getKey());
            user.setRFID((String)dataSnapshot.child("rfid").getValue());
            user.setPushID((String) dataSnapshot.child("pushID").getValue());
            user.setUsername((String) dataSnapshot.child("username").getValue());
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            rfidHashTable.remove(dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    ChildEventListener balanceListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            balanceHashTable.put(dataSnapshot.getKey(), new Balance(getDoubleFromDatabase(dataSnapshot.child("Balance").child("balance").getValue())));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            balanceHashTable.put(dataSnapshot.getKey(), new Balance(getDoubleFromDatabase(dataSnapshot.child("Balance").child("balance").getValue())));
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            balanceHashTable.remove(dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ChildEventListener kegListeners = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            int i = Integer.parseInt(dataSnapshot.getKey().toString());
            if(i > 1)
                return;
            kegInfo[i] = new KegInfo((String)dataSnapshot.child("Name").getValue(),
                    (String)dataSnapshot.child("KegSize").getValue(),
                    (String)dataSnapshot.child("Style").getValue(),
                    getDoubleFromDatabase(dataSnapshot.child("Spent").getValue()),
                    getDoubleFromDatabase(dataSnapshot.child("Fee").getValue()),
                    getDoubleFromDatabase(dataSnapshot.child("Saving").getValue()),
                    (String)dataSnapshot.child("Purchaser").getValue(),
                    (boolean)dataSnapshot.child("active").getValue(),
                    getDoubleFromDatabase(dataSnapshot.child("beersLeft").getValue()));
            updateKegCardInfo(i);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            int i = Integer.parseInt(dataSnapshot.getKey().toString());
            if(i > 1)
                return;
            if(kegInfo[i] == null)
                return;
            kegInfo[i].setBeersLeft(getDoubleFromDatabase(dataSnapshot.child("beersLeft").getValue()));
            kegInfo[i].setName((String)dataSnapshot.child("Name").getValue());
            kegInfo[i].setKegSize((String)dataSnapshot.child("KegSize").getValue());
            kegInfo[i].setStyle((String)dataSnapshot.child("Style").getValue());
            kegInfo[i].setSpent(getDoubleFromDatabase(dataSnapshot.child("Spent").getValue()));
            kegInfo[i].setFee(getDoubleFromDatabase(dataSnapshot.child("Fee").getValue()));
            kegInfo[i].setSavings(getDoubleFromDatabase(dataSnapshot.child("Saving").getValue()));
            kegInfo[i].setPurchaser((String)dataSnapshot.child("Purchaser").getValue());
            kegInfo[i].setActive((boolean)dataSnapshot.child("active").getValue());
            updateKegCardInfo(i);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    public void updateKegCardInfo(int i)
    {
        if(i == 0)
        {
            if(kegInfo[i] == null) {
                keg1.setVisibility(View.INVISIBLE);
                return;
            }
            if(kegInfo[i].isActive())
                keg1.setVisibility(View.VISIBLE);
            else
                keg1.setVisibility(View.INVISIBLE);
        }
        else
        {
            if(kegInfo[i] == null) {
                keg2.setVisibility(View.INVISIBLE);
                return;
            }
            if(kegInfo[i].isActive())
                keg2.setVisibility(View.VISIBLE);
            else
                keg2.setVisibility(View.INVISIBLE);
        }
        int nT = getResources().getIdentifier("name" + (i+1) + "Text", "id", getPackageName());
        int sT = getResources().getIdentifier("style" + (i+1) + "Text", "id", getPackageName());
        int bL = getResources().getIdentifier("beersLeft" + (i+1) + "Text", "id", getPackageName());
        int pO = getResources().getIdentifier("perOunce" + (i+1) + "Text", "id", getPackageName());
        int pB = getResources().getIdentifier("perBeer" + (i+1) + "Text", "id", getPackageName());
        ((TextView) findViewById(nT)).setText(kegInfo[i].getName());
        ((TextView) findViewById(sT)).setText(kegInfo[i].getStyle());
        ((TextView) findViewById(bL)).setText(kegInfo[i].getRoundedBeersLeft());
        ((TextView) findViewById(pO)).setText(kegInfo[i].getRoundedPricePerOunce());
        ((TextView) findViewById(pB)).setText(kegInfo[i].getRoundedPricePerBeer());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userHashTable = new Hashtable<>();
        rfidHashTable = new Hashtable<>();
        balanceHashTable = new Hashtable<>();

        mContext = getApplicationContext();

        getBluetoothMacAddress();

        setKegImages();

        //cardviews
        keg1 = (CardView)(findViewById(R.id.keg1CardView));
        keg2 = (CardView)(findViewById(R.id.keg2CardView));

        cardReadStart = false;
        cardString = new StringBuilder();

        logOut();



        //click listeners for the buttons
        keg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click listener to keg 1 screen
                String ra = "12:201:182:203";
                RFID rad = rfidHashTable.get(ra);
                User u = userHashTable.get(rad.getPushID());
                Balance b = balanceHashTable.get(u.getUsername());
                if(Util.currentUser == null)
                    logOn(u,b);
                else
                    logOut();

                /*Intent intent = new Intent(getApplicationContext(), PourActivity.class);
                intent.putExtra("KegPos", 0);
                startActivity(intent);*/
            }
        });
        keg2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //on click listener to keg 2 screen
                if(Util.currentUser == null) {
                    Toast.makeText(getApplicationContext(), "Please log in first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Util.currentBalance.getBalance() < 3) {
                    Toast.makeText(getApplicationContext(), "Balance must be greater than $3 to begin pouring", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), PourActivity.class);
                intent.putExtra("KegPos", 1);
                startActivity(intent);
            }
        });
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("intentKey"));
        if(!Util.isBound) {
            startService(new Intent(this, BluetoothDataService.class));
            Util.isBound = true;
        }//Util isBound

        updateKegCardInfo(0);
        updateKegCardInfo(1);

        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").addChildEventListener(userListener);
        ref.child("RFID").addChildEventListener(rfidListener);
        ref.child("Kegs").addChildEventListener(kegListeners);
        ref.child("Balances").addChildEventListener(balanceListener);

    }
    private void getBluetoothMacAddress() {
        if(savedBluetoothAddress != null)
            return;
        String filename = "BLUETOOTH_MAC_ADDRESS.txt";
        File file = new File(this.getFilesDir(), filename);
        try {
            if (!file.exists()) {
                FileOutputStream outputStream;
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(("00:14:03:06:27:45").getBytes());
                outputStream.close();
                savedBluetoothAddress = "00:14:03:06:27:45";
            } else {
                try {
                    InputStream inputStream = this.openFileInput(filename);

                    if (inputStream != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();

                        while ((receiveString = bufferedReader.readLine()) != null) {
                            stringBuilder.append(receiveString);
                        }

                        inputStream.close();
                        savedBluetoothAddress = stringBuilder.toString();
                        Toast.makeText(this, "MAC ADDRESS " + savedBluetoothAddress, Toast.LENGTH_LONG).show();
                    }
                } catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void logOn(User u, Balance b)
    {
        Util.currentUser = u;
        Util.currentBalance = b;
        Toast.makeText(getApplicationContext(), "Welcome " + u.getName(), Toast.LENGTH_SHORT).show();
        logOnView(true);
        invalidateOptionsMenu();

    }
    private void logOut()
    {
        if(Util.currentUser != null)
            Toast.makeText(getApplicationContext(), Util.currentUser.getName() + " logged out", Toast.LENGTH_SHORT).show();
        Util.currentBalance = null;
        Util.currentUser = null;
        logOnView(false);
        invalidateOptionsMenu();
    }
    public void clickLogoutButton(View v)
    {
        logOut();
    }
    private void logOnView(boolean b)
    {
        if(b)
        {
            ((TextView)findViewById(R.id.loginTextNotification)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.loginNameText)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.loginNameText)).setText(Util.currentUser.getName());
            ((TextView)findViewById(R.id.loginBalanceText)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.loginBalanceText)).setText("" + Util.currentBalance.getBalance());
            ((TextView)findViewById(R.id.balanceTitleText)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.logoutButton)).setVisibility(View.VISIBLE);
            ((Space)findViewById(R.id.logOnSpace)).setVisibility(View.VISIBLE);
            ((LinearLayout)findViewById(R.id.loginLinearLayout)).setVisibility(View.VISIBLE);
            ((LinearLayout)findViewById(R.id.loginLinearLayout)).setBackgroundColor(Color.GREEN);
        }
        else
        {
            ((TextView)findViewById(R.id.loginTextNotification)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.loginNameText)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.loginBalanceText)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.balanceTitleText)).setVisibility(View.GONE);
            ((Space)findViewById(R.id.logOnSpace)).setVisibility(View.GONE);
            ((Button)findViewById(R.id.logoutButton)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.loginLinearLayout)).setVisibility(View.VISIBLE);
            ((LinearLayout)findViewById(R.id.loginLinearLayout)).setBackgroundColor(Color.RED);
        }

    }
    @Override
    public void setmMessageReceiver() {
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                String message = intent.getStringExtra("key");
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
                            Toast.makeText(getApplicationContext(), "Welcome " + u.getName() , Toast.LENGTH_SHORT).show();
                            if(Util.currentUser == null)
                                logOn(u, balanceHashTable.get(u.getUsername()));
                            else
                            {
                                logOut();
                                logOn(u, balanceHashTable.get(u.getUsername()));
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(), "RFID not recognized", Toast.LENGTH_SHORT).show();
                    }
                }
            }//on Receive
        };
    }


    public void setKegImages()
    {
        File file;
        if(Util.imageBitmaps[0] == null)
        {

            file = new File(getFilesDir(),"image" + 0);
            if(file.exists())
            {
                //save image bitmap as that bullshit
                Util.imageBitmaps[0] = BitmapFactory.decodeFile(file.getPath());
                ((ImageView)findViewById(R.id.keg1Image)).setImageBitmap(Util.imageBitmaps[0]);
            }
        }
        else
            ((ImageView)findViewById(R.id.keg1Image)).setImageBitmap(Util.imageBitmaps[0]);
        if(Util.imageBitmaps[1] == null)
        {

            file = new File(getFilesDir(),"image" + 1);
            if(file.exists())
            {
                //save image bitmap as that bullshit
                Util.imageBitmaps[1] = BitmapFactory.decodeFile(file.getPath());
                ((ImageView)findViewById(R.id.keg2Image)).setImageBitmap(Util.imageBitmaps[1]);
            }
        }
        else
            ((ImageView)findViewById(R.id.keg2Image)).setImageBitmap(Util.imageBitmaps[1]);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Util.mContext = getApplicationContext();
        writeToBluetooth(this, R.string.RFID_STATE);
    }//on resume

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        if(currentUser == null) {
            menu.findItem(R.id.menuAdmin).setEnabled(false);
            menu.findItem(R.id.menuUserHistory).setVisible(false);
        }
        else {
            menu.findItem(R.id.menuUserHistory).setVisible(true);
            if (currentUser.isAdmin())
                menu.findItem(R.id.menuAdmin).setEnabled(true);
            else
                menu.findItem(R.id.menuAdmin).setEnabled(false);
        }

        return true;
    }//on create options menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menuAdmin:
                intent = new Intent(this, AdminTop.class);
                startActivityForResult(intent, ADMIN_REQUEST);
                return true;
            case R.id.menuContactInfo:
                intent = new Intent(this, ContactInfo.class);
                startActivity(intent);
                return true;
            case R.id.menuUserHistory:
                intent = new Intent(this, UserHistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//onOptionsitemSelected

    private double getDoubleFromDatabase(Object o)
    {
        if(o.getClass().getName().toString().toLowerCase().equals("java.lang.long"))
            return ((Long)o).doubleValue();
        else
            //if(o.getClass().getName().toString().toLowerCase().equals("java.lang.double"))
            return ((Double)o).doubleValue();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
