package com.nukki.gifted;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    public static final String prefs_name = "MyPreferencesFile";
    private RadioGroup radioColorGroup;
    private RadioButton radioButton;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        input = (EditText) findViewById(R.id.input);


        setContentView(R.layout.activity_settings);
        radioColorGroup = (RadioGroup) findViewById(R.id.radioPick);

        radioColorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            // Called when the checked state of a compound button has changed.
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioColorGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);

                SharedPreferences settings = getSharedPreferences(prefs_name,0);
                SharedPreferences.Editor editor = settings.edit();

                String the_pick = radioButton.getText().toString();
                Log.i("+++++++++++++++++++++ ", the_pick);
                editor.putString("setting", the_pick);
                editor.commit();
                checkAbutton();

            }
        }); // end of onChecked change

        checkAbutton();
    }


    public void checkAbutton(){
        SharedPreferences settings = getSharedPreferences(prefs_name,0);
        String btn = settings.getString("setting", "Trending");
        if (btn.equals("Trending")) {
            radioColorGroup.check(R.id.radioTrending);
        }
        if (btn.equals("Random")) {
            radioColorGroup.check(R.id.radioRandom);
        }
        if (btn.equals("Custom")) {
            radioColorGroup.check(R.id.radioCustom);
        }
    }

    public void onClickDone(View view ){
        SharedPreferences settings = getSharedPreferences(prefs_name,0);
        String btn = settings.getString("setting", "Trending");

        if (input != null) {
            radioColorGroup.check(R.id.radioCustom);
        }

        if (btn.equals("Custom")){
            SharedPreferences.Editor editor = settings.edit();
            String input = ((EditText) findViewById(R.id.input)).getText().toString().trim().replace(' ', '+');
            if(!input.isEmpty()){
                editor.putString("search_phrase", input);
                editor.commit();
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this,"What are you looking for?", Toast.LENGTH_LONG);
            }

        } else {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
