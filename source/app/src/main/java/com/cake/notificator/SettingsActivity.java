package com.cake.notificator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //read all settings here.
        SharedPreferences mPrefAppSettings = getSharedPreferences("appsettings", 0);

        //set vibration.
        ((Switch) findViewById(R.id.switch_Vibration))
                .setChecked(mPrefAppSettings.getBoolean("vibration", true));

        //set reset silent.
        ((Switch) findViewById(R.id.switch_Reset_Silent))
                .setChecked(mPrefAppSettings.getBoolean("reset_silent", false));

        //set reset delay.
        ((Switch) findViewById(R.id.switch_Reset_Delay))
                .setChecked(mPrefAppSettings.getBoolean("reset_delay", false));
    }

    @Override
    public void onBackPressed() {
        //set all settings here.
        SharedPreferences.Editor mPrefAppSettingsEdit = getSharedPreferences("appsettings",
                0).edit();

        //set vibration.
        if (((Switch) findViewById(R.id.switch_Vibration)).isChecked()) {
            mPrefAppSettingsEdit.putBoolean("vibration", true).apply();
        } else {
            mPrefAppSettingsEdit.putBoolean("vibration", false).apply();
        }

        //set reset silent.
        if (((Switch) findViewById(R.id.switch_Reset_Silent)).isChecked()) {
            mPrefAppSettingsEdit.putBoolean("reset_silent", true).apply();
        } else {
            mPrefAppSettingsEdit.putBoolean("reset_silent", false).apply();
        }

        //set reset delay.
        if (((Switch) findViewById(R.id.switch_Reset_Delay)).isChecked()) {
            mPrefAppSettingsEdit.putBoolean("reset_delay", true).apply();
        } else {
            mPrefAppSettingsEdit.putBoolean("reset_delay", false).apply();
        }

        super.onBackPressed();
    }

    //handle taskbar back click here.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
        }

        return true;
    }
}
