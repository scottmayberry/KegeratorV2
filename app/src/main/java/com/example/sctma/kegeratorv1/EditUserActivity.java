package com.example.sctma.kegeratorv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class EditUserActivity extends AbstractActivity {

    String key;
    User user;

    EditText nameText;
    EditText emailText;
    EditText venmoText;
    EditText balanceText;
    TextView rfidText;
    CheckBox adminBox;
    Spinner classification;
    Button rfid;

    Button editButton;
    Button deleteButton;
    Button cancelButton;
    Button submitButton;
    Space space1;
    Space space2;

    TextView nameError;
    TextView emailError;
    TextView venmoError;

    boolean addBrother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);


        addBrother = getIntent().getBooleanExtra("ADDUSER", false);


        //setting spinner resources
        classification = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classification_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classification.setAdapter(adapter);

        if(!addBrother) {
            key = getIntent().getStringExtra("KEY");
            user = Util.userHashTable.get(key);

        }
        else {
            user = new User();
            user.setClassification(classification.getItemAtPosition(0).toString());
            user.setAdmin(false);
            key = "NEWUSER";
        }


        nameText = (EditText) findViewById(R.id.nameEditText);
        emailText = (EditText) findViewById(R.id.emailEditText);
        venmoText = (EditText) findViewById(R.id.venmoEditText);
        adminBox = (CheckBox) findViewById(R.id.adminCheckBox);
        adminBox.setChecked(false);
        balanceText = (EditText) findViewById(R.id.balanceEditText);
        rfidText = (TextView) findViewById(R.id.rfidText);



        nameError = (TextView) findViewById(R.id.nameErrorText);
        emailError = (TextView) findViewById(R.id.emailErrorText);
        venmoError = (TextView) findViewById(R.id.usernameErrorText);
        nameError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);
        venmoError.setVisibility(View.GONE);

        rfid = (Button) findViewById(R.id.rfidButton);
        rfid.setTag(user.getRfid());
        deleteButton = (Button) findViewById(R.id.deleteUserButton);
        submitButton = (Button) findViewById(R.id.submitUserButton);
        editButton = (Button) findViewById(R.id.editUserButton);
        cancelButton = (Button) findViewById(R.id.cancelUserButton);
        space1 = (Space) findViewById(R.id.space1);
        space2 = (Space) findViewById(R.id.space2);

        adminBox.setChecked(user.isAdmin());


        removeEditingStuff();
        for (int i = 0; i < classification.getCount(); i++) {
            if (user.getClassification().equals(classification.getItemAtPosition(i).toString()))
                classification.setSelection(i);
        }//for loop
        stopEditing();
        if(addBrother)
        {
            balanceText.setVisibility(View.GONE);
            findViewById(R.id.balanceTitleText).setVisibility(View.GONE);
            startEditing();
        }


    }//on create

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

    private void startEditing() {
        //set all enabled to true
        setFieldsEnabled(true);
        setEditButtonsVisible(true);
    }//begin editing

    private void stopEditing() {
        setFieldsEnabled(false);
        setEditButtonsVisible(false);
    }

    private void setFieldsEnabled(boolean bo) {
        nameText.setEnabled(bo);
        emailText.setEnabled(bo);
        venmoText.setEnabled(bo);
        classification.setEnabled(bo);
        rfid.setEnabled(bo);
        balanceText.setEnabled(bo);
        adminBox.setEnabled(bo);
    }//set fields enabled

    public void rfidButtonPressed(View v)
    {
        Intent intent = new Intent(this, RFIDScannerActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == 5)
        {
            rfidText.setText(data.getStringExtra("RFID"));
        }
        else
        {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setEditButtonsVisible(boolean bo) {
        if (bo)//true
        {
            deleteButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            space1.setVisibility(View.GONE);
            submitButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            space2.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            space1.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            space2.setVisibility(View.GONE);
        }
    }//set buttons visible

    public void deleteButtonClicked(View v) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setTitle("Delete User?");
        adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Util.ref.child("Users").child(key).removeValue();
                finish();
            } });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            } });
        adb.show();
    }

    public void editButtonClicked(View v) {
        startEditing();
    }

    public void cancelButtonClicked(View v) {
        if(addBrother) {
            finish();
            return;
        }
        removeEditingStuff();
    }
    public void removeEditingStuff()
    {
        nameText.setText(user.getName());
        emailText.setText(user.getEmail());
        venmoText.setText(user.getUsername());
        rfidText.setText(user.getRfid());
        if(!addBrother)
            balanceText.setText("" + Util.balanceHashTable.get(user.getUsername()).getBalance());

        nameError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);
        venmoError.setVisibility(View.GONE);
        stopEditing();
    }


    public void submitButtonClicked(View v)
    {
        if(nameText.getText().toString().equals(""))
        {
            nameError.setVisibility(View.VISIBLE);
            return;
        }
        nameError.setVisibility(View.GONE);

        if(emailText.getText().toString().equals(""))
        {
            emailError.setVisibility(View.VISIBLE);
            return;
        }
        emailError.setVisibility(View.GONE);
        if(venmoText.getText().toString().equals(""))
        {
            venmoError.setVisibility(View.VISIBLE);
            return;
        }
        venmoError.setVisibility(View.GONE);
        if(rfidText.getText().toString().equals("rfid") || rfidText.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please scan an rfid card", Toast.LENGTH_SHORT);
            return;
        }
        if(!addBrother)
        {
            if(balanceText.getText().toString().equals("") || balanceText.getText().toString() == null)
            {
                balanceText.setText("" + 0);
                Toast.makeText(this, "Balance was null", Toast.LENGTH_SHORT);
                return;
            }
        }
        User nU = new User(nameText.getText().toString(), rfidText.getText().toString(), venmoText.getText().toString(), classification.getSelectedItem().toString(), emailText.getText().toString(), adminBox.isChecked());
        for(String key : Util.userHashTable.keySet())
        {
            if(!key.equals(this.key) && Util.userHashTable.get(key).getUsername().equals(nU.getUsername())) {
                venmoError.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Same venmo as " + Util.userHashTable.get(key).getName() + ". Cannot double up.", Toast.LENGTH_SHORT);
                return;
            }//username exists
            if(!key.equals(this.key) && Util.userHashTable.get(key).getRfid().equals(nU.getRfid()))
            {
                Toast.makeText(this, "Same rfid as " + Util.userHashTable.get(key).getName() + ". Cannot double up.", Toast.LENGTH_SHORT);
                return;
            }
        }//check through the hashmap
        if(!addBrother) {
            double ba = Double.parseDouble(balanceText.getText().toString());
            Util.ref.child("Users").child(key).setValue(nU);
            String usn = user.getUsername();
            ChargeItem chargeItem = new ChargeItem(ba - Util.balanceHashTable.get(user.getUsername()).getBalance(), "Admin adjustment", Util.df.format(new Date()));
            String temp = Util.ref.child("Balances").child(user.getUsername()).child("Charge").push().getKey();
            Util.ref.child("Balances").child(user.getUsername()).child("Charge").child(temp).setValue(chargeItem);
        }
        else
        {
            key = Util.ref.child("Users").push().getKey();
            Util.ref.child("Users").child(key).setValue(nU);
        }
        finish();

    }

}
