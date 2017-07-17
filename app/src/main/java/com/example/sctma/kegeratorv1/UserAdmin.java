package com.example.sctma.kegeratorv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class UserAdmin extends AppCompatActivity {

    ArrayList<LinearLayout> scrollViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

        scrollViews = new ArrayList<LinearLayout>();
        scrollViews.add((LinearLayout) findViewById(R.id.seniorLinearLayout));
        scrollViews.add((LinearLayout) findViewById(R.id.juniorLinearLayout));
        scrollViews.add((LinearLayout) findViewById(R.id.sophomoreLinearLayout));
        scrollViews.add((LinearLayout) findViewById(R.id.freshmanLinearLayout));
        scrollViews.add((LinearLayout) findViewById(R.id.otherLinearLayout));




        for(String key : Util.userHashTable.keySet())
        {
            User user = Util.userHashTable.get(key);
            int in = getClassAsInt(user.getClassification());

            Button b = new Button(this);
            b.setText(user.getName());
            b.setTag(key);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to edit menu
                    String key = (String)v.getTag();
                    Intent intent = new Intent(getApplicationContext(), EditUserActivity.class);
                    intent.putExtra("KEY", key);
                    startActivity(intent);
                }
            });
            scrollViews.get(in).addView(b);

        }//search through hashtable
    }
    @Override
    public void onResume()
    {
        super.onResume();
        Util.mContext = getApplicationContext();
        for(int i = 0; i < scrollViews.size();i++)
            scrollViews.get(i).removeAllViews();

        for(String key : Util.userHashTable.keySet())
        {
            User user = Util.userHashTable.get(key);
            int in = getClassAsInt(user.getClassification());

            Button b = new Button(this);
            b.setText(user.getName());
            b.setTag(key);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to edit menu
                    String key = (String)v.getTag();
                    Intent intent = new Intent(getApplicationContext(), EditUserActivity.class);
                    intent.putExtra("KEY", key);
                    startActivity(intent);
                }
            });
            scrollViews.get(in).addView(b);
        }//search through hashtable
    }
    private int getClassAsInt(String cl)
    {
        switch(cl)
        {
            case "Senior": return 0;
            case "Junior": return 1;
            case "Sophomore": return 2;
            case "Freshman": return 3;
            case "Other": return 4;
            default: return 4;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.useradminmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.add_new_user:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//onOptionsitemSelected
}
