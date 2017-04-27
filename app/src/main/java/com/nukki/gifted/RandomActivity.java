package com.nukki.gifted;

import java.util.UUID;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.io.File;
import static com.nukki.gifted.R.drawable.app_icon_final;
import static com.nukki.gifted.R.drawable.dead;

public class RandomActivity extends AppCompatActivity {
    ImageView gif;
    String link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_activity);
        gif = (ImageView) findViewById(R.id.gif);
        link = getIntent().getStringExtra("url");
        if(link.isEmpty()){
            gif.setImageResource(app_icon_final);

        } else {
            Ion.with(gif)
                    .placeholder(app_icon_final)
                    .error(dead)
                    .load(link);
        }
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void goRefresh(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public File getAlbumStorageDir(String albumName) {
            // Get the directory for the user's public pictures directory.
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
