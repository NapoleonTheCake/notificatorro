package com.cake.notificator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //read all settings here.
        SharedPreferences mPrefAppSettings = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        //set vibration.
        Switch switch_Vibration = (Switch) findViewById(R.id.switch_Vibration);
        switch_Vibration.setChecked(mPrefAppSettings.getBoolean("vibration", false));
    }

    @Override
    public void onBackPressed() {
        //set all settings here.
        SharedPreferences.Editor mPrefAppSettingsEdit = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext()).edit();

        //set vibration.
        Switch switch_Vibration = (Switch) findViewById(R.id.switch_Vibration);
        if (switch_Vibration.isChecked()) {
            mPrefAppSettingsEdit.putBoolean("vibration", true).apply();
        } else {
            mPrefAppSettingsEdit.putBoolean("vibration", false).apply();
        }

        super.onBackPressed();
    }
}
