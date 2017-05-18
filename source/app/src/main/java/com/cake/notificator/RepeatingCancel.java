package com.cake.notificator;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by cake on 18.05.17.
 */

public class RepeatingCancel extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        int NOTIFY_ID = getIntent().getIntExtra("NOTIFY_ID", 0);

        Intent intent = new Intent(getApplicationContext(), Schedule.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), NOTIFY_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        finish();
        super.onCreate(savedInstanceState);
    }
}
