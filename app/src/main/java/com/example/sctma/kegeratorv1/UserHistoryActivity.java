package com.example.sctma.kegeratorv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Date;

import static com.example.sctma.kegeratorv1.Util.currentUser;
import static com.example.sctma.kegeratorv1.Util.ref;

public class UserHistoryActivity extends AppCompatActivity {

    LinearLayout uH;
    String usern;
    ChildEventListener cel = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            LinearLayout ll = new LinearLayout(getApplicationContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            usern = currentUser.getUsername();


            Long l = (Long)dataSnapshot.child("time").getValue();
            String info = (String)dataSnapshot.child("info").getValue();
            double amount = getDoubleFromDatabase(dataSnapshot.child("amount").getValue());
            Date d = new Date(l);

            TextView textView1 = new TextView(getApplicationContext());
            textView1.setLayoutParams(param);
            textView1.setText("" + d.toString());


            TextView textView2 = new TextView(getApplicationContext());
            textView2.setLayoutParams(param);
            textView2.setText("" + info);

            TextView textView3 = new TextView(getApplicationContext());
            textView3.setLayoutParams(param);
            textView3.setText("" + amount);

            ll.addView(textView1);
            //ll.addView(new Space(getApplicationContext()));
            ll.addView(textView2);
            //ll.addView(new Space(getApplicationContext()));
            ll.addView(textView3);


            uH.addView(ll);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);

        uH = (LinearLayout)findViewById(R.id.userHistoryLinearLayout);

        ref.child("Balances").child(currentUser.getUsername()).child("Charge").addChildEventListener(cel);
    }
    private double getDoubleFromDatabase(Object o)
    {
        if(o.getClass().getName().toString().toLowerCase().equals("java.lang.long"))
            return ((Long)o).doubleValue();
        else
            //if(o.getClass().getName().toString().toLowerCase().equals("java.lang.double"))
            return ((Double)o).doubleValue();
    }

    @Override
    protected void onDestroy() {
        ref.child("Balances").child(usern).child("Charge").removeEventListener(cel);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
