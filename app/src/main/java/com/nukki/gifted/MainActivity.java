package com.nukki.gifted;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends FragmentActivity {

    ViewPager viewPager;
    ImageView g_logo;
    ImageButton settingsB;
    ImageButton refreshB;
    public static final String prefs_name = "MyPreferencesFile";
    ImageButton downloadB;
    String link;
    private ArrayList<String> links;
    Bundle arrayBundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        g_logo = (ImageView) findViewById(R.id.logo);
        g_logo.setImageResource(R.mipmap.logo1);

        settingsB = (ImageButton) findViewById(R.id.settings);
        settingsB.setImageResource(R.drawable.settings);

        downloadB = (ImageButton) findViewById(R.id.download);
        downloadB.setImageResource(R.drawable.down);

        refreshB = (ImageButton) findViewById(R.id.refresh);
        refreshB.setImageResource(R.drawable.refresh);

        viewPager = (ViewPager) findViewById(R.id.gif);


        links = getIntent().getStringArrayListExtra("urls");
        arrayBundle = new Bundle();
        arrayBundle.putStringArrayList("links", links);

        SwipeAdaptor swipeAdaptor = new SwipeAdaptor(getSupportFragmentManager(), arrayBundle);
        viewPager.setAdapter(swipeAdaptor);

    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.exists()) {
            file.mkdir();
        }
        if (!file.mkdirs()) {
            Log.i("AYYYYYY", "Directory not created");
        }
        return file;

    }

    public void doSave(View view) {
        SharedPreferences settings = getSharedPreferences(prefs_name, 0);
        link = settings.getString("current_link", "https://media.giphy.com/media/CQXjjMNCvDVok/giphy.gif");
        File d = getAlbumStorageDir("GIFted");
        File f = new File(d, UUID.randomUUID().toString() + Uri.parse(link).getLastPathSegment());

        Ion.with(getBaseContext()).load(link)
                .write(f)
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        if( e == null ) {
                            Toast.makeText(getBaseContext(), "GIF saved", Toast.LENGTH_SHORT).show();
                            Intent intent =
                                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            intent.setData(Uri.fromFile(file));
                            sendBroadcast(intent);
                        }else {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void goRefresh(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}

