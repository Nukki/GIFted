package com.nukki.gifted;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {
    TextView error;
    String problem;
    ImageView logo;
    ImageButton settings;
    ImageButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_activity);
        logo = (ImageView) findViewById(R.id.gif);
        logo.setImageResource(R.drawable.dead);

        error = (TextView) findViewById(R.id.instruction);
        problem = getIntent().getStringExtra("problem");
        if (problem.equals("JSON")){
            error.setText("No GIFs found");
            refresh = (ImageButton) findViewById(R.id.refresh);
            refresh.setVisibility(View.INVISIBLE);
        }
        if (problem.equals("Internet")) {
            error.setText("No Internet connection");
            settings = (ImageButton) findViewById(R.id.settings);
            settings.setVisibility(View.INVISIBLE);
        }
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

    public void goRefresh(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}
