package com.cake.notificator;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import static com.cake.notificator.PersistActivity.NOTIFY_ID_PERSIST;

/**
 * Created by cake on 12.05.17.
 */

public class PersistToggle extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);
        SharedPreferences.Editor mPrefsEditor = mPrefs.edit();

        if (mPrefs.getBoolean("persist", true)) {

            //update text.
            update();

            ////

            mPrefsEditor.putBoolean("persist", false).apply();

            //echo done (only in PersistToggle).
            Toast.makeText(this, getString(R.string.notification_Created), Toast.LENGTH_SHORT).show();

        } else {

            //cancel persistent notification.
            ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFY_ID_PERSIST);

            ////

            mPrefsEditor.putBoolean("persist", true).apply();

            //echo done (only in PersistToggle).
            Toast.makeText(this, getString(R.string.notification_Created), Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void update() {
        Context context = getApplicationContext();
        SharedPreferences mPrefsText = getSharedPreferences("notifications", 0);
        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);

        //get text.
        String bigText = mPrefsText.getString("persist_text", "");
        String titleText = mPrefsText.getString("persist_title", "");

        //set default texts.
        if (bigText.length() == 0) {
            bigText = getString(R.string.persist_Text_Default);
        }
        if (titleText.length() == 0) {
            titleText = getString(R.string.persist_Title_Default);
        }

        //create intent.
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //build notification.
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setAutoCancel(false)
                .setContentTitle(titleText)
                .setContentText(bigText);

        //check if show
        if (mPrefs.getBoolean("persist_showicon", false)) {
            builder.setSmallIcon(R.drawable.statusbaricon);
        } else {
            builder.setSmallIcon(android.R.color.transparent);
        }

        //check if in priority.
        if (mPrefs.getBoolean("persist_prior", false)) {
            builder.setPriority(Notification.PRIORITY_MAX);
        }

        //colorize notification for 21+ api.
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setColor(getResources().getColor(R.color.color_Persist));
        }

        //check vibration.
        if (mPrefs.getBoolean("vibration", false)) {
            builder.setVibrate(new long[]{0, 50});
        }

        //show notification.
        builder.setWhen(System.currentTimeMillis());

        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(bigText).build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID_PERSIST, notification);
    }
}
