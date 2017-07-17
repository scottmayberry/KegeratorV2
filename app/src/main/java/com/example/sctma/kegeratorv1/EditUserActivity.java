package com.example.sctma.kegeratorv1;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

public class EditUserActivity extends AppCompatActivity {

    String key;
    User user;

    EditText nameText;
    EditText emailText;
    EditText venmoText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        key = getIntent().getStringExtra("KEY");
        user = Util.userHashTable.get(key);

        //setting spinner resources
        classification = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classification_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classification.setAdapter(adapter);

        nameText = (EditText) findViewById(R.id.nameEditText);
        emailText = (EditText) findViewById(R.id.emailEditText);
        venmoText = (EditText) findViewById(R.id.venmoEditText);

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

        //set all enabled editing
        setFieldsEnabled(false);

        nameText.setText(user.getName());
        emailText.setText(user.getEmail());
        venmoText.setText(user.getUsername());

        for (int i = 0; i < classification.getCount(); i++) {
            if (user.getClassification().equals(classification.getItemAtPosition(i).toString()))
                classification.setSelection(i);
        }//for loop
        stopEditing();


    }//on create

    @Override
    protected void onResume() {
        super.onResume();
        Util.mContext = getApplicationContext();
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
    }//set fields enabled

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
        nameText.setText(user.getName());
        emailText.setText(user.getEmail());
        venmoText.setText(user.getUsername());
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
        User nU = new User(nameText.getText().toString(), rfid.getTag().toString(), venmoText.getText().toString(), classification.getSelectedItem().toString(), emailText.getText().toString());
        if(!nU.getUsername().equals(user.getUsername().toString()))
        {
            for(String key : Util.userHashTable.keySet())
            {
                if(!key.equals(this.key) && Util.userHashTable.get(key).getUsername().equals(user.getUsername())) {
                    venmoError.setVisibility(View.VISIBLE);
                    return;
                }//username exists
            }//check through the hashmap
        }//check for username equalling
        Util.ref.child("Users").child(key).setValue(nU);
        finish();

    }

}
