package com.example.sctma.kegeratorv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class ImageSelectionActivity extends AbstractActivity {
    DropboxAPI<AndroidAuthSession> mApi;
    ArrayList<File> filesToDelete;
    File imageFile = null;
    int kegPos;

    private final int imageSi = 220;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);

        kegPos = getIntent().getIntExtra("KegPos", 0);

        filesToDelete = new ArrayList<>();


        AndroidAuthSession session = buildSession();
        session.setOAuth2AccessToken(getString(R.string.APP_ACCESS_TOKEN));
        mApi = new DropboxAPI<AndroidAuthSession>(session);

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

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(getString(R.string.APP_KEY),getString(R.string.APP_SECRET));
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        return session;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.mContext = getApplicationContext();
        AndroidAuthSession session = mApi.getSession();
        String bo = session.getOAuth2AccessToken();

        try {
            new Thread(new Runnable() {
                public void run() {
                    // a potentially  time consuming task
                    addImageButtons();
                }
            }).start();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "Failed Authentication", Toast.LENGTH_SHORT).show();
            finish();
        }
        Util.writeToBluetooth(this, R.string.STANDBY_STATE);
    }
    public void addImageButtons()
    {
        try {
            DropboxAPI.Entry dirent = null;
            dirent = mApi.metadata("/", 100, null, true, null);
            ArrayList<DropboxAPI.Entry> files = new ArrayList<DropboxAPI.Entry>();
            ArrayList<String> dir = new ArrayList<String>();
            ArrayList<File> fil = new ArrayList<>();
            int i = 0;
            for (DropboxAPI.Entry ent : dirent.contents) {
                if(ent.mimeType.toLowerCase().equals("image/jpeg") || ent.mimeType.toLowerCase().equals("image/png"))
                {
                    //image/jpeg
                    //image/png
                    final File file = new File(getApplicationContext().getFilesDir(), "imageTemp" + i);
                    if(!file.exists())
                        file.createNewFile();
                   // File file = new File(ent.path);
                   // if(!file.exists())
                   //     file.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(file);
                    DropboxAPI.DropboxFileInfo info = mApi.getFile(ent.path,null, outputStream, null);
                    filesToDelete.add(file);
                    if(i%2 == 0)
                        ((LinearLayout)findViewById(R.id.topLinearLayout)).post(new Runnable(){

                            @Override
                            public void run() {
                                ImageButton imageButton = new ImageButton(getApplicationContext());
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                                imageButton.setLayoutParams(new ActionBar.LayoutParams(imageSi,imageSi));
                                imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
                                imageButton.setTag(file.getName());
                                imageButton.setImageBitmap(bitmap);
                                imageButton.setOnClickListener(imageButtonClicked);
                                ((LinearLayout)findViewById(R.id.topLinearLayout)).addView(imageButton);
                            }
                        });
                    else
                        ((LinearLayout)findViewById(R.id.bottomLinearLayout)).post(new Runnable(){

                            @Override
                            public void run() {
                                ImageButton imageButton = new ImageButton(getApplicationContext());
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                                imageButton.setLayoutParams(new ActionBar.LayoutParams(imageSi,imageSi));
                                imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
                                imageButton.setTag(file.getName());
                                imageButton.setImageBitmap(bitmap);
                                imageButton.setOnClickListener(imageButtonClicked);
                                ((LinearLayout)findViewById(R.id.bottomLinearLayout)).addView(imageButton);
                            }
                        });
                    //Log.i("DbExampleLog", "The file's rev is: " + info.getMetadata().rev);
                }
                files.add(ent);// Add it to the list of thumbs we can choose from
                //dir = new ArrayList<String>()
                dir.add(new String(files.get(i++).path));
            }

        }catch(DropboxException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private View.OnClickListener imageButtonClicked = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            for(int i = 0; i < filesToDelete.size();i++)
            {
                String name = filesToDelete.get(i).getName();
                if(name.equals(v.getTag().toString()))
                {
                    imageFile = filesToDelete.get(i);
                }
                else
                    filesToDelete.get(i).delete();
            }
            Intent intent = new Intent();
            intent.putExtra("FILE", imageFile);
            setResult(3, intent);
            finish();
        }//on click
    };

}
